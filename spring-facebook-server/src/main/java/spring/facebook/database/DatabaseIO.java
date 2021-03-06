package spring.facebook.database;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import spring.facebook.entities.Comment;
import spring.facebook.entities.FacebookPost;
import spring.facebook.entities.User;

@Component
public class DatabaseIO {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private FacebookPostRepository facebookPostRepository;
    
    @Autowired
    private CommentRepository commentRepository;
    
    public void saveUserToDatabase(User user) {
        userRepository.save(user);
    }
    
    public void saveUsersToDatabase(List<User> users) {
        userRepository.save(users);
    }
    
    public User getUserFromDbByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    
    public User getUserFromDbByFacebookId(String facebookId) {
        return userRepository.findByFacebookId(facebookId);
    }
    
    public List<User> getAllUsersFromDatabase() {
        return userRepository.findAll();
    }
    
    public void deleteUserFromDatabase(long id) {
        userRepository.delete(id);
    }
    
    public FacebookPost getPostFromDatabaseByFacebookId(String id) {
        return facebookPostRepository.findByRealFacebookId(id);
    }
    
    public FacebookPost getPostFromDatabaseById(long id) {
        return facebookPostRepository.findByFacebookPostId(id);
    }
    
    public void deletePost(long postId) {
        facebookPostRepository.delete(postId);
    }
            
    public void saveCommentToDatabase(Comment comment) {
        commentRepository.save(comment);
    }
    
    public Comment getCommentFromDatabase(String id) {
        return commentRepository.findByCommentId(id);
    }
    
    public void deleteComment(long commentId) {
        commentRepository.delete(commentId);
    }
}