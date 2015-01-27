package tests;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import package1.databaseStaffs.CommentRepository;
import package1.databaseStaffs.DatabaseIO;
import package1.databaseStaffs.FacebookPostRepository;
import package1.databaseStaffs.UserRepository;
import package1.entities.Comment;
import package1.entities.FacebookPost;
import package1.entities.User;
import package1.utils.Authentications;
import package1.utils.Checks;
import package1.utils.UserDetailsServiceImpl;
import package1.utils.WorksWithEntities;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, DatabaseIOTest.TestConfig.class})
public class DatabaseIOTest {

	@Configuration
	static class TestConfig {
				
		@Bean
		public DatabaseIO getDatabaseIOInstance() {
			return new DatabaseIO();
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
		public WorksWithEntities getWorksWithEntitiesMock() {
			return mock(WorksWithEntities.class);
		}
		
		@Bean
		public UserDetailsServiceImpl getUsersDetailsServiceImplMock() {
			return mock(UserDetailsServiceImpl.class);
		}
	}
	
	@Autowired
	private DatabaseIO databaseIO;
	
	@Autowired
	private User user;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private FacebookPostRepository facebookPostRepository;
	
	@Autowired
	private FacebookPost facebookPost;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private Comment comment;
	
	@Test
	public void saveUserToDatabaseTest() {
		databaseIO.saveUserToDatabase(user);
		verify(userRepository).save(user);
	}
	
	@Test
	public void saveUsersToDatabaseTest() {
		List<User> users = new ArrayList<User>();
		databaseIO.saveUsersToDatabase(users);
		
		verify(userRepository).save(users);
	}
	
	@Test
	public void getUserFromDbByEmailTest() {
		when(userRepository.findByEmail(anyString())).thenReturn(user);
		assertEquals(user, databaseIO.getUserFromDbByEmail(anyString()));
	}
	
	@Test
	public void getUserFromDbByFacebookIdTest() {
		when(userRepository.findByFacebookId(anyString())).thenReturn(user);
		assertEquals(user, databaseIO.getUserFromDbByFacebookId(anyString()));
	}
	
	@Test
	public void getAllUsersFromDatabaseTest() {
		List<User> users = new ArrayList<User>();
		when(userRepository.findAll()).thenReturn(users);
		
		assertEquals(users, databaseIO.getAllUsersFromDatabase());
	}
	
	@Test
	public void deleteUserFromDatabaseTest() {
		databaseIO.deleteUserFromDatabase(1);
		verify(userRepository).delete((long) 1);
	}
	
	@Test
	public void getPostFromDatabaseByFacebookIdTest() {
		databaseIO.getPostFromDatabaseByFacebookId(anyString());
		verify(facebookPostRepository).findByRealFacebookId(anyString());
	}
	
	@Test
	public void getPostFromDatabaseByIdTest() {
		databaseIO.getPostFromDatabaseById(1);
		verify(facebookPostRepository).findByFacebookPostId(1);
	}
	
	@Test
	public void deletePostTest() {
		databaseIO.deletePost(1);
		verify(facebookPostRepository).delete((long) 1);
	}
	
	@Test
	public void saveCommentToDatabaseTest() {
		databaseIO.saveCommentToDatabase(comment);
		verify(commentRepository).save(comment);
	}
	
	@Test
	public void getCommentFromDatabaseTest() {
		databaseIO.getCommentFromDatabase("AAA");
		verify(commentRepository).findByCommentId("AAA");
	}
	
	@Test
	public void deleteCommentTest() {
		databaseIO.deleteComment(10);
		verify(commentRepository).delete((long) 10);
	}
}