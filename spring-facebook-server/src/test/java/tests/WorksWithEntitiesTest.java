package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.FeedOperations;
import org.springframework.social.facebook.api.Post;
import org.springframework.social.facebook.api.Reference;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring.facebook.database.DatabaseIO;
import spring.facebook.entities.Comment;
import spring.facebook.entities.FacebookPost;
import spring.facebook.entities.User;
import spring.facebook.utils.Authentications;
import spring.facebook.utils.Checks;
import spring.facebook.utils.UserDetailsServiceImpl;
import spring.facebook.utils.WorksWithEntities;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, WorksWithEntitiesTest.TestConfig.class})
public class WorksWithEntitiesTest {

    @Configuration
    static class TestConfig {
            
        @Bean
        public WorksWithEntities getWorksWithEntitiesInstance() {
            return new WorksWithEntities();
        }
        
        @Bean
        public DatabaseIO getDatabaseIOMock() {
            return mock(DatabaseIO.class);
        }
        
        @Bean
        public Checks getChecksMock() {
            return mock(Checks.class);
        }
        
        @Bean
        public Authentications getAuthenticationsMock() {
            return mock(Authentications.class);
        }
        
        @Bean
        public UserDetailsServiceImpl getUsersDetailsServiceImplMock() {
            return mock(UserDetailsServiceImpl.class);
        }
    }
    
    @Autowired
    private WorksWithEntities worksWithEntities;
        
    @Autowired
    private Facebook facebook;
    
    @Autowired
    private FeedOperations feedOperations;
    
    @Autowired
    private UserOperations userOperations;
    
    @Autowired
    private FacebookProfile facebookProfile;
    
    @Autowired
    private Checks checks;
    
    @Autowired
    private DatabaseIO databaseIO;
    
    @Autowired
    private User user;
    
    @Autowired
    private FacebookPost facebookPost;
    
    @Autowired
    private Post post;
    
    @Autowired
    private Comment comment;
    
    @Autowired
    private Reference reference;
    
    @Before
    public void setUp() {
        reset(databaseIO);
    }
    
    @Test
    public void getFacebookProfileTest() {
        when(facebook.userOperations()).thenReturn(userOperations);
        when(userOperations.getUserProfile()).thenReturn(facebookProfile);
        
        assertEquals(facebookProfile, worksWithEntities.getFacebookProfile(facebook));
    }
    
    @Test
    public void getFacebookProfileName() {
        when(facebook.userOperations()).thenReturn(userOperations);
        when(userOperations.getUserProfile()).thenReturn(facebookProfile);
        when(facebookProfile.getName()).thenReturn("AAA");
        
        assertEquals("AAA", worksWithEntities.getFacebookProfileName(facebook));
    }
        
    @Test
    public void createFacebookPostTest() {
        when(post.getId()).thenReturn("AA_AA");
        when(post.getFrom()).thenReturn(reference);
        when(reference.getName()).thenReturn("AAA");
        
        assertEquals("AA", worksWithEntities.createFacebookPost(post, user).getRealFacebookId());
    }
    
    @Test
    public void addPostTest1() {
        List<FacebookPost> posts = new ArrayList<FacebookPost>();
        when(facebookPost.getText()).thenReturn(null);
        
        assertEquals(posts, worksWithEntities.addPost(posts, facebookPost));
    }
    
    @Test
    public void addPostTest2() {
        List<FacebookPost> posts = new ArrayList<FacebookPost>();
        List<FacebookPost> spy = spy(posts);
        
        when(facebookPost.getText()).thenReturn("AAA");
        worksWithEntities.addPost(spy, facebookPost);
        
        verify(spy).add(facebookPost);
        assertEquals(posts, worksWithEntities.addPost(posts, facebookPost));
    }
    
    @Test
    public void isPostInTheListAlreadyTest1() {
        List<FacebookPost> posts = new ArrayList<FacebookPost>();
        assertEquals(false, worksWithEntities.isPostInTheListAlready(posts, facebookPost));
    }
    
    @Test
    public void isPostInTheListAlreadyTest2() {
        List<FacebookPost> posts = new ArrayList<FacebookPost>();
        posts.add(facebookPost);
        when(facebookPost.getRealFacebookId()).thenReturn("111");
        
        assertEquals(true, worksWithEntities.isPostInTheListAlready(posts, facebookPost));
    }
    
    @Test
    public void getUsersWithThisPostTest() {
        List<User> users = new ArrayList<User>();
        users.add(user);
        
        assertEquals(users, worksWithEntities.getUsersWithThisPost(user));
    }
    
    @Test
    public void deleteUnsynchronizedFacebookUserTest() {
        when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(user);
        when(user.getUserId()).thenReturn((long) 1);
        worksWithEntities.deleteUnsynchronizedFacebookUser(anyString());
        
        verify(databaseIO).deleteUserFromDatabase(1);
    }
    
    @Test
    public void deleteCommentTest() {
        worksWithEntities.deleteComment(1);
        verify(databaseIO).deleteComment(1);
    }
    
    @Test
    public void addCommentTest() {
        WorksWithEntities spy = spy(worksWithEntities);
        
        when(databaseIO.getPostFromDatabaseById(anyLong())).thenReturn(facebookPost);
        when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(user);
        doReturn(comment).when(spy).createComment("AAA", facebookPost, user);
        spy.addComment("AAA", 1, "AAA");        
        
        verify(databaseIO).saveCommentToDatabase(comment);
    }
    
    @Test
    public void createCommentTest() {   
        assertEquals("AAA", worksWithEntities.createComment("AAA", facebookPost, user).getCommText());
    }
    
    @Test
    public void removePostFromUsersHomePageTest() {
        when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(user);
        worksWithEntities.removePostFromUsersHomePage(1, anyString());
        
        verify(databaseIO).saveUserToDatabase(user);
    }
    
    @Test
    public void deletePostTest() {
        List<User> users = new ArrayList<User>();
        List<User> spy = spy(users);
        
        when(databaseIO.getPostFromDatabaseById(1)).thenReturn(facebookPost);
        when(facebookPost.getUsersWithThisPost()).thenReturn(spy);
        when(spy.size()).thenReturn(3);
        doReturn(user).when(spy).get(anyInt());
        worksWithEntities.deletePost(1);
        
        verify(user, times(3)).removePostWithGivenId(1);
        verify(databaseIO).deletePost(1);
    }
    
    @Test
    public void createLocalPostTest() {
        WorksWithEntities spy = spy(worksWithEntities);
        List<User> users = new ArrayList<User>();
        
        when(spy.findUserInList(users, "Username")).thenReturn(user);
        when(user.getFirstName()).thenReturn("First name");
        when(user.getLastName()).thenReturn("Last name");
        spy.createLocalPost("AAA", "Username");
        
        verify(spy).addPostToAllUsers(eq(users), any(FacebookPost.class));
    }
    
    @Test
    public void findUserInListTest1() {
        List<User> users = new ArrayList<User>();
        assertEquals(null, worksWithEntities.findUserInList(users, "AAA"));
    }
    
    @Test
    public void findUserInListTest2() {
        List<User> users = new ArrayList<User>();
        users.add(user);
        when(user.getEmail()).thenReturn("AAA");
        
        assertEquals(user, worksWithEntities.findUserInList(users, "AAA"));
    }
    
    @Test
    public void addPostToAllUsersTest() {
        List<User> users = new ArrayList<User>();
        worksWithEntities.addPostToAllUsers(users, facebookPost);
        
        verify(databaseIO).saveUsersToDatabase(users);
    }
}