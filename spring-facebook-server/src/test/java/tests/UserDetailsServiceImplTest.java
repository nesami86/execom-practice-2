package tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import package1.databaseStaffs.DatabaseIO;
import package1.databaseStaffs.UserRepository;
import package1.entities.User;
import package1.utils.Authentications;
import package1.utils.Checks;
import package1.utils.ConvertUsers;
import package1.utils.FillingContentOnThePage;
import package1.utils.UserDetailsServiceImpl;
import package1.utils.WorksWithEntities;
import static org.mockito.Matchers.anyString;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, UserDetailsServiceImplTest.TestConfig.class})
public class UserDetailsServiceImplTest {

	@Configuration
	static class TestConfig {
				
		@Bean
		public UserDetailsServiceImpl getUserDetailsServiceImplInstance() {
			return new UserDetailsServiceImpl();
		}
		
		@Bean
		public DatabaseIO getDatabaseIOMock() {
			return mock(DatabaseIO.class);
		}
		
		@Bean
		public FillingContentOnThePage getFillingContentOnThePageMock() {
			return mock(FillingContentOnThePage.class);
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
	}
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;
	
	@Autowired
	private ConvertUsers convertUsers;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private User user;
	
	@Test
	public void loadUserByUsernameTest1() {
		when(userRepository.findByEmail(anyString())).thenReturn(user);
		userDetailsServiceImpl.loadUserByUsername(anyString());
		
		verify(convertUsers).convertUser(user);
	}
	
	@Test(expected=UsernameNotFoundException.class)
	public void loadUserByUsernameTest2() {
		when(userRepository.findByEmail(anyString())).thenReturn(null);
		userDetailsServiceImpl.loadUserByUsername(anyString());
	}
}