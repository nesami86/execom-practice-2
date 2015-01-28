package spring.facebook.utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Comment;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.Post;
import org.springframework.stereotype.Component;

import spring.facebook.databaseStaffs.DatabaseIO;
import spring.facebook.entities.FacebookPost;
import spring.facebook.entities.User;

@Component
public class WorksWithEntities {

	@Autowired
	private Checks checks;
	
	@Autowired
	private DatabaseIO databaseIO;
	
	@Autowired
	private Authentications authentications;
	
	public User synchronizeFacebookUser(Facebook facebook, String email) {
		User user = databaseIO.getUserFromDbByEmail(email);
		user.setFacebookId(getFacebookProfile(facebook).getId());
		user.setFacebookHomePagePosts(getFacebookUsersHomePagePosts(facebook, user));
		
		authentications.destroyFacebookConnection();
		databaseIO.saveUserToDatabase(user);
		deleteUnsynchronizedFacebookUser(getFacebookProfile(facebook).getId());
		
		return user;
	}
	
	public User createUserFromFacebookUser(Facebook facebook) {
		User user = new User();
		user.setFirstName(getFacebookProfileName(facebook).split(" ")[0]);
		user.setLastName(getFacebookProfileName(facebook).split(" ")[1]);
		user.setEmail(getFacebookProfile(facebook).getId());
		user.setPassword(getFacebookProfileName(facebook));
		user.setFacebookId(getFacebookProfile(facebook).getId());
		user.setFacebookHomePagePosts(getFacebookUsersHomePagePosts(facebook, user));
		
		if (!checks.doesUserAlreadyExistInDatabase(user.getEmail())) {
			databaseIO.saveUserToDatabase(user);
		}
		return user;
	}
	
	public FacebookProfile getFacebookProfile(Facebook facebook) {
		return facebook.userOperations().getUserProfile();
	}
	
	public String getFacebookProfileName(Facebook facebook) {
		return getFacebookProfile(facebook).getName();
	}
	
	public List<FacebookPost> getFacebookUsersHomePagePosts(Facebook facebook, User user) {
		List<FacebookPost> posts = new ArrayList<FacebookPost>();
		
		for (Post post : facebook.feedOperations().getHomeFeed()) {
			FacebookPost facebookPost = databaseIO.getPostFromDatabaseByFacebookId(post.getId().split("_")[1]);
			
			if (facebookPost != null && !facebookPost.isUserInTheListOfUsers(user)) {
				facebookPost.getUsersWithThisPost().add(user);
			} else if (facebookPost == null) {
				facebookPost = createFacebookPost(post, user);
			}	
			posts = addPost(posts, facebookPost);
		}
		return posts;
	}
	
	@SuppressWarnings("deprecation")
	public FacebookPost createFacebookPost(Post post, User user) {
		FacebookPost facebookPost = new FacebookPost();
		facebookPost.setRealFacebookId(post.getId().split("_")[1]);
		facebookPost.setUsersWithThisPost(getUsersWithThisPost(user));
		facebookPost.setAuthorsName(post.getFrom().getName());
		facebookPost.setDateCreated(post.getCreatedTime());
		facebookPost.setText(post.getMessage());
		facebookPost.setImageUrl(post.getPicture());
		facebookPost.setComments(getPostsComments(post, facebookPost));
		return facebookPost;
	}
	
	public List<FacebookPost> addPost(List<FacebookPost> posts, FacebookPost facebookPost) {
		if (facebookPost.getText() != null && !isPostInTheListAlready(posts, facebookPost)) {
			posts.add(facebookPost);
		}
		return posts;
	}
	
	public boolean isPostInTheListAlready(List<FacebookPost> posts, FacebookPost facebookPost) {
		for (FacebookPost post : posts) {
			if (post.getRealFacebookId().equals(facebookPost.getRealFacebookId())) {
				return true;
			}
		}
		return false;
	}
			
	public List<User> getUsersWithThisPost(User user) {
		List<User> users = new ArrayList<User>();
		users.add(user);
		return users;
	}
	
	public List<spring.facebook.entities.Comment> getPostsComments(Post post, FacebookPost facebookPost) {
		List<spring.facebook.entities.Comment> comments = new ArrayList<spring.facebook.entities.Comment>();
		
		for (Comment comment : post.getComments()) {
			spring.facebook.entities.Comment comm = new spring.facebook.entities.Comment();
			comm.setFacebookPost(facebookPost);
			comm.setAuthorsName(comment.getFrom().getName());
			comm.setDateCreated(comment.getCreatedTime());
			comm.setText(comment.getMessage());
			
			comments.add(comm);
		}
		return comments;
	}
	
	public void deleteUnsynchronizedFacebookUser(String email) {
		User user = databaseIO.getUserFromDbByEmail(email);
		if (user != null) {
			databaseIO.deleteUserFromDatabase(user.getUserId());
		}
	}
	
	public void deleteComment(long commentId) {
		databaseIO.deleteComment(commentId);
	}
	
	public void addComment(String text, long postId, String username) {
		FacebookPost post = databaseIO.getPostFromDatabaseById(postId);
		User user = databaseIO.getUserFromDbByEmail(username);
		spring.facebook.entities.Comment comment = createComment(text, post, user);
		databaseIO.saveCommentToDatabase(comment);
	}
	
	public spring.facebook.entities.Comment createComment(String text, FacebookPost post, User user) {
		spring.facebook.entities.Comment comment = new spring.facebook.entities.Comment();
		comment.setText(text);
		comment.setFacebookPost(post);
		comment.setDateCreated(Calendar.getInstance().getTime());
		comment.setAuthorsName(user.getFirstName() + " " + user.getLastName());
		return comment;
	}
	
	public void removePostFromUsersHomePage(long postId, String email) {
		User user = databaseIO.getUserFromDbByEmail(email);
		user.removePostWithGivenId(postId);
		databaseIO.saveUserToDatabase(user);
	}
		
	public void deletePost(long postId) {
		FacebookPost post = databaseIO.getPostFromDatabaseById(postId);
		List<User> users = post.getUsersWithThisPost();
		for (int i = 0; i < post.getUsersWithThisPost().size(); i++) {
			User user = post.getUsersWithThisPost().get(i);
			user.removePostWithGivenId(postId);
		}
		databaseIO.saveUsersToDatabase(users);
		databaseIO.deletePost(postId);
	}
	
	public void createPost(String text, String username) {
		List<User> allUsers = databaseIO.getAllUsersFromDatabase();
		User user = findUserInList(allUsers, username);
		
		FacebookPost post = new FacebookPost();
		post.setAuthorsName(user.getFirstName() + " " + user.getLastName());
		post.setDateCreated(Calendar.getInstance().getTime());
		post.setText(text);
		post.setUsersWithThisPost(allUsers);
		
		addPostToAllUsers(allUsers, post);
	}
	
	public User findUserInList(List<User> users, String username) {
		for (User user : users) {
			if (user.getEmail().equals(username)) {
				return user;
			}
		}
		return null;
	}
	
	public void addPostToAllUsers(List<User> users, FacebookPost post) {
		for (User user : users) {
			user.getFacebookHomePagePosts().add(post);
		}
		databaseIO.saveUsersToDatabase(users);
	}
}