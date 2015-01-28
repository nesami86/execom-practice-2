package tests;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.FeedOperations;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.ui.Model;

import spring.facebook.database.CommentRepository;
import spring.facebook.database.FacebookPostRepository;
import spring.facebook.database.UserRepository;
import spring.facebook.entities.Comment;
import spring.facebook.entities.FacebookPost;
import spring.facebook.entities.User;
import spring.facebook.utils.ConvertUsers;

import static org.mockito.Mockito.mock;

@Configuration
public class TestBeans {
	
	@Bean
	public Facebook getFacebookMock() {
		return mock(Facebook.class);
	}
	
	@Bean
	public Model getModelMock() {
		return mock(Model.class);
	}
	
	@Bean
	public ConvertUsers getConvertUsersMock() {
		return mock(ConvertUsers.class);
	}
	
	@Bean
	public UserRepository getUserRepositoryMock() {
		return mock(UserRepository.class);
	}
	
	@Bean
	public FacebookPostRepository getFacebookPostRepositoryMock() {
		return mock(FacebookPostRepository.class);
	}
	
	@Bean
	public CommentRepository getCommentRepositoryMock() {
		return mock(CommentRepository.class);
	}
	
	@Bean
	public User getUserMock() {
		return mock(User.class);
	}
		
	@Bean
	public ConnectionRepository getConnectionRepositoryMock() {
		return mock(ConnectionRepository.class);
	}
		
	@Bean
	public org.springframework.security.core.userdetails.User getSpringSecurityUserMock() {
		return mock(org.springframework.security.core.userdetails.User.class);
	}
	
	@Bean
	public FacebookProfile getFacebookProfileMock() {
		return mock(FacebookProfile.class);
	}
	
	@Bean
	public UserOperations getUserOperationsMock() {
		return mock(UserOperations.class);
	}
	
	@Bean
	public FeedOperations getFeedOperationsMock() {
		return mock(FeedOperations.class);
	}
	
	@Bean
	public FacebookPost getFacebookPostMock() {
		return mock(FacebookPost.class);
	}
	
	@Bean
	public Comment getCommentMock() {
		return mock(Comment.class);
	}
	
	@Bean
	public Post getPostMock() {
		return mock(Post.class);
	}
}