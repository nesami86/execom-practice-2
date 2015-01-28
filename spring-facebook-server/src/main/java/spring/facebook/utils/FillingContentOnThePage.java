package spring.facebook.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import spring.facebook.databaseStaffs.DatabaseIO;
import spring.facebook.entities.User;

@Component
public class FillingContentOnThePage {
		
	@Autowired
	private DatabaseIO databaseIO;
	
	public void fillFacebookHomePageFromConnection(Model model, Facebook facebook) {		
		model.addAttribute(facebook.userOperations().getUserProfile());
        model.addAttribute("feed", facebook.feedOperations().getHomeFeed());
	}
	
	public void fillFacebookHomePageFromDatabase(Model model, String facebookId) {			
		model.addAttribute("user", databaseIO.getUserFromDbByEmail(facebookId));
	}
	
	public void fillFacebookHomePageFromUser(Model model, User user) {
		model.addAttribute("user", user);
	}
	
	public void fillUsersProfilePage(Model model, String email) {
		model.addAttribute("user", databaseIO.getUserFromDbByEmail(email));
	}
		
	public void fillProtectedPage(Model model, String username) {
		model.addAttribute("username", username);
	}
}