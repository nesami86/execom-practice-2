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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ui.Model;

import package1.WebController;
import package1.databaseStaffs.DatabaseIO;
import package1.entities.User;
import package1.utils.Authentications;
import package1.utils.Checks;
import package1.utils.FillingContentOnThePage;
import package1.utils.UserDetailsServiceImpl;
import package1.utils.WorksWithEntities;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, WebControllerTest.TestConfig.class})
public class WebControllerTest {
	
	@Configuration
	static class TestConfig {
				
		@Bean
		public WebController getWebControllerInstance() {
			return new WebController();
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
		public Authentications getAuthenticationsMock() {
			return mock(Authentications.class);
		}
		
		@Bean
		public WorksWithEntities getWorksWithEntitiesMock() {
			return mock(WorksWithEntities.class);
		}
		
		@Bean
		public FillingContentOnThePage getFillingContentOnThePageMock() {
			return mock(FillingContentOnThePage.class);
		}
		
		@Bean
		public UserDetailsServiceImpl getUsersDetailsServiceImplMock() {
			return mock(UserDetailsServiceImpl.class);
		}
	}

	@Autowired
	private WebController webController;
			
	@Autowired 
	private Model model;
	
	@Autowired
	private Checks checks;
	
	@Autowired
	private User user;
	
	@Autowired
	private Facebook facebook;
	
	@Autowired
	private Authentications authentications;
		
	@Before
	public void setUp() {
		reset(checks);
		
		Authentication auth = new UsernamePasswordAuthenticationToken(user,null);
	    SecurityContextHolder.getContext().setAuthentication(auth);
	}
	
	@Test
	public void initialPageSelectorTest1() {
		when(checks.isFacebookAuthorized(facebook)).thenReturn(true);
		when(checks.doesUserAlreadyExistInDatabase(webController.getAuthenticatedUsersName())).thenReturn(false);
		
		assertEquals("hello", webController.initialPageSelector(model));
	}
	
	@Test
	public void initialPageSelectorTest2() {
		when(checks.isFacebookAuthorized(facebook)).thenReturn(true);
		when(checks.doesUserAlreadyExistInDatabase(webController.getAuthenticatedUsersName())).thenReturn(true);
		
		assertEquals("hello2", webController.initialPageSelector(model));
	}
	
	@Test
	public void initialPageSelectorTest3() {
		when(checks.isFacebookAuthorized(facebook)).thenReturn(false);
		assertEquals("redirect:/connect/facebook", webController.initialPageSelector(model));
	}
	
	@Test
	public void getLoginFormTest() {
		assertEquals("login", webController.getLoginForm());
	}
	
	@Test
	public void getRegisterFormTest() {
		assertEquals("register", webController.getRegisterForm());
	}
		
	@Test
	public void registerTest1() {
		when(checks.verifyRegistration(user)).thenReturn("");
		assertEquals("", webController.register(user, model));
	}
	
	@Test
	public void registerTest2() {
		when(checks.verifyRegistration(user)).thenReturn("email is already taken ");
		assertEquals("email is already taken ", webController.register(user, model));
	}
	
	@Test
	public void getProfilePageTest() {
		assertEquals("profile", webController.getProfilePage(model));
	}
		
	@Test
	public void getFacebookHomePageTest() {
		assertEquals("hello2", webController.getFacebookHomePage(model));
	}
	
	@Test
	public void getProtectedPageTest() {
		assertEquals("protectedPage", webController.getProtectedPage(model));
	}
	
	@Test
	public void logoutTest() {
		assertEquals("redirect:/", webController.logout());
	}
	
	@Test
	public void deleteCommentTest() {
		assertEquals("hello2", webController.deleteComment("1", model));
	}
	
	@Test
	public void addCommentTest() {
		assertEquals("hello2", webController.addComment("AAA", "1", model));
	}
	
	@Test
	public void removePostTest() {
		assertEquals("hello2", webController.removePost("1", model));
	}
	
	@Test
	public void deletePostTest() {
		assertEquals("hello2", webController.deletePost("1", model));
	}
	
	@Test
	public void addPostTest() {
		assertEquals("hello2", webController.addPost("AAA", model));
	}
}