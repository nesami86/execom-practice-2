package tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import package1.databaseStaffs.DatabaseIO;
import package1.entities.User;
import package1.utils.Authentications;
import package1.utils.Checks;
import package1.utils.UserDetailsServiceImpl;
import package1.utils.WorksWithEntities;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, ChecksTest.TestConfig.class})
public class ChecksTest {

	@Configuration
	static class TestConfig {
				
		@Bean
		public Checks getChecksInstance() {
			return new Checks();
		}
		
		@Bean
		public DatabaseIO getDatabaseIOMock() {
			return mock(DatabaseIO.class);
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
	private Checks checks;
	
	@Autowired
	private Facebook facebook;
	
	@Autowired
	private DatabaseIO databaseIO;
	
	@Autowired
	private User user;
	
	@Autowired
	private Authentications authentications;
		
	@Test
	public void isFacebookAuthorizedTest1() {
		when(checks.isFacebookAuthorized(facebook)).thenReturn(true);
		assertEquals(true, checks.isFacebookAuthorized(facebook));
	}
	
	@Test
	public void isFacebookAuthorizedTest2() {
		when(checks.isFacebookAuthorized(facebook)).thenReturn(false);
		assertEquals(false, checks.isFacebookAuthorized(facebook));
	}
	
	@Test
	public void verifyRegistrationTest1() {
		Checks spy = spy(checks);
	
		when(spy.verifyUsersFirstName(anyString())).thenReturn("");
		when(spy.verifyUsersLastName(anyString())).thenReturn("");
		when(spy.verifyUsersEmail(anyString())).thenReturn("");
		when(spy.verifyUsersPassword(anyString())).thenReturn("");
		spy.verifyRegistration(user);
		
		verify(spy).saveUser(user);
		assertEquals("", spy.verifyRegistration(user));
	}
	
	@Test
	public void verifyRegistrationTest2() {
		Checks spy = spy(checks);
	
		when(spy.verifyUsersFirstName(anyString())).thenReturn("first name is empty ");
		when(spy.verifyUsersLastName(anyString())).thenReturn("");
		when(spy.verifyUsersEmail(anyString())).thenReturn("");
		when(spy.verifyUsersPassword(anyString())).thenReturn("");
		
		assertEquals("first name is empty ", spy.verifyRegistration(user));
	}
	
	@Test
	public void verifyUsersFirstNameTest1() {	
		assertEquals("first name is empty ", checks.verifyUsersFirstName(""));
	}
	
	@Test
	public void verifyUsersFirstNameTest2() {
		assertEquals("", checks.verifyUsersFirstName("AAA"));
	}
	
	@Test
	public void verifyUsersLastNameTest1() {
		assertEquals("last name is empty ", checks.verifyUsersLastName(""));
	}
	
	@Test
	public void verifyUsersLastNameTest2() {
		assertEquals("", checks.verifyUsersLastName("AAA"));
	}
	
	@Test
	public void verifyUsersEmailTest1() {
		assertEquals("email is empty ", checks.verifyUsersEmail(""));
	}
	
	@Test
	public void verifyUsersEmailTest2() {
		when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(user);
		assertEquals("email is already taken ", checks.verifyUsersEmail("AAA"));
	}
	
	@Test
	public void verifyUsersEmailTest3() {
		when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(null);
		assertEquals("", checks.verifyUsersEmail("AAA"));
	}
	
	@Test
	public void verifyUsersPasswordTest1() {
		assertEquals("password is empty", checks.verifyUsersPassword(""));
	}
	
	@Test
	public void verifyUsersPasswordTest2() {
		assertEquals("", checks.verifyUsersPassword("AAA"));
	}
	
	@Test
	public void saveUserTest() {		
		checks.saveUser(user);
		verify(databaseIO).saveUserToDatabase(user);
	}
	
	@Test
	public void authenticateUserTest() {
		checks.authenticateUser(user);
		verify(authentications).authenticateUser(user);
	}
	
	@Test
	public void doesUserAllreadyExistInDatabaseTest1() {
		when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(user);
		when(databaseIO.getUserFromDbByFacebookId(anyString())).thenReturn(null);
		
		assertEquals(true, checks.doesUserAlreadyExistInDatabase("AAA"));
	}
	
	@Test
	public void doesUserAllreadyExistInDatabaseTest2() {
		when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(null);
		when(databaseIO.getUserFromDbByFacebookId(anyString())).thenReturn(user);
		
		assertEquals(true, checks.doesUserAlreadyExistInDatabase("anonymousUser"));
	}
	
	@Test
	public void doesUserAllreadyExistInDatabaseTest3() {
		when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(user);
		when(databaseIO.getUserFromDbByFacebookId(anyString())).thenReturn(user);
		
		assertEquals(true, checks.doesUserAlreadyExistInDatabase("AAA"));
	}
	
	@Test
	public void doesUserAllreadyExistInDatabaseTest4() {
		when(databaseIO.getUserFromDbByEmail(anyString())).thenReturn(null);
		when(databaseIO.getUserFromDbByFacebookId(anyString())).thenReturn(null);
		
		assertEquals(false, checks.doesUserAlreadyExistInDatabase("anonymousUser"));
	}
}