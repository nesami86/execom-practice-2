package tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import package1.databaseStaffs.DatabaseIO;
import package1.entities.User;
import package1.utils.Authentications;
import package1.utils.Checks;
import package1.utils.ConvertUsers;
import package1.utils.UserDetailsServiceImpl;
import package1.utils.WorksWithEntities;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, AuthenticationsTest.TestConfig.class})
public class AuthenticationsTest {

	@Component
	static class TestConfig {
						
		@Bean
		public Authentications getAuthenticationInstance() {
			return new Authentications();
		}
		
		@Bean
		public DatabaseIO getDatabaseIOMock() {
			return mock(DatabaseIO.class);
		}
		
		@Bean
		public Checks getChecksMock(){
			return mock(Checks.class);
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
	private Authentications authentications;
	
	@Autowired
	private User user;
	
	@Autowired
	private org.springframework.security.core.userdetails.User userDetails;
	
	@Autowired 
	private UserDetailsService userDetailsService;
	
	@Autowired
	private Facebook facebook;
	
	@Autowired
	private ConvertUsers convertUsers;
			
	@Autowired
	private WorksWithEntities worksWithEntities;
	
	@Autowired
	private DatabaseIO databaseIO;
	
	@Autowired
	private FacebookProfile facebookProfile;
	
	@Autowired
	private UserOperations userOperations;
		
	@Test
	public void authenticateUserTest() {
		Authentications spy = spy(authentications);
		
		when(userDetailsService.loadUserByUsername(user.getEmail())).thenReturn(userDetails);
		spy.authenticateUser(user);
		
		verify(spy).createUsersToken(userDetails);
	}
	
	@Test
	public void deauthenticateUserTest() {
		Authentications spy = spy(authentications);
		
		spy.deauthenticateUser();
		
		verify(spy).destroyFacebookConnection();
	}
		
	@Test
	public void authenticateFacebookUserTest1() {
		Authentications spy = spy(authentications);
		
		doReturn(userOperations).when(facebook).userOperations();
		doReturn(facebookProfile).when(userOperations).getUserProfile();
		doReturn("1").when(facebookProfile).getId();
		doReturn(null).when(databaseIO).getUserFromDbByFacebookId("1");
		doReturn(user).when(worksWithEntities).createUserFromFacebookUser(facebook);
		doReturn(userDetails).when(convertUsers).convertUser(user);
		spy.authenticateFacebookUser(facebook);
		
		verify(spy).createUsersToken(userDetails);
	}
	
	@Test
	public void authenticateFacebookUserTest2() {
		Authentications spy = spy(authentications);
		
		doReturn(userOperations).when(facebook).userOperations();
		doReturn(facebookProfile).when(userOperations).getUserProfile();
		doReturn("1").when(facebookProfile).getId();
		doReturn(user).when(databaseIO).getUserFromDbByFacebookId("1");
		doReturn(userDetails).when(convertUsers).convertUser(user);
		spy.authenticateFacebookUser(facebook);
		
		verify(spy).createUsersToken(userDetails);
	}
}