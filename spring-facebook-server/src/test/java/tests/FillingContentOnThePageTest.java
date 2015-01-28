package tests;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.social.facebook.api.FeedOperations;
import org.springframework.social.facebook.api.UserOperations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;

import spring.facebook.databaseStaffs.DatabaseIO;
import spring.facebook.entities.User;
import spring.facebook.utils.Authentications;
import spring.facebook.utils.Checks;
import spring.facebook.utils.FillingContentOnThePage;
import spring.facebook.utils.UserDetailsServiceImpl;
import spring.facebook.utils.WorksWithEntities;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, FillingContentOnThePageTest.TestConfig.class})
public class FillingContentOnThePageTest {

	@Configuration
	static class TestConfig {
				
		@Bean
		public FillingContentOnThePage getFillingContentOnThePageInstance() {
			return new FillingContentOnThePage();
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
		public WorksWithEntities getWorksWithEntitiesMock() {
			return mock(WorksWithEntities.class);
		}
		
		@Bean
		public UserDetailsServiceImpl getUsersDetailsServiceImplMock() {
			return mock(UserDetailsServiceImpl.class);
		}
	}
	
	@Autowired
	private FillingContentOnThePage fillingContentOnThePage;
	
	@Autowired
	private User user;
	
	@Autowired
	private Model model;
	
	@Autowired
	private Facebook facebook;
	
	@Autowired
	private UserOperations userOperations;
	
	@Autowired
	private FacebookProfile facebookProfile;
	
	@Autowired
	private FeedOperations feedOperations;
	
	@Autowired
	private DatabaseIO databaseIO;
	
	@Before
	public void setUp() {
		reset(model);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user,null);
	    SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	public void fillFacebookHomePageFromConnectionTest() {
		when(facebook.userOperations()).thenReturn(userOperations);
		when(userOperations.getUserProfile()).thenReturn(facebookProfile);
		when(facebook.feedOperations()).thenReturn(feedOperations);
		
		fillingContentOnThePage.fillFacebookHomePageFromConnection(model, facebook);
		
		verify(model).addAttribute(facebook.userOperations().getUserProfile());
		verify(model).addAttribute("feed", facebook.feedOperations().getHomeFeed());
	}
	
	@Test
	public void fillFacebookHomePageFromDatabaseTest() {
		fillingContentOnThePage.fillFacebookHomePageFromDatabase(model, "facebookId");
		verify(model).addAttribute("user", databaseIO.getUserFromDbByEmail("facebookId"));
	}
	
	@Test
	public void fillFacebookHomePageFromUserTest() {
		fillingContentOnThePage.fillFacebookHomePageFromUser(model, user);
		verify(model).addAttribute("user", user);
	}
	
	@Test
	public void fillUsersProfilePageFromDatabaseTest() {
		fillingContentOnThePage.fillUsersProfilePage(model, "email");
		verify(model).addAttribute("user", databaseIO.getUserFromDbByEmail("email"));
	}
		
	@Test
	public void fillProtectedPageTest() {
		fillingContentOnThePage.fillProtectedPage(model, "AAA");
		verify(model).addAttribute("username", "AAA");
	}
}