package spring.facebook.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

import spring.facebook.databaseStaffs.DatabaseIO;
import spring.facebook.entities.User;

@Component
public class Checks {
			
	@Autowired
	private DatabaseIO databaseIO;
	
	@Autowired
	private Authentications authentications;
	
	public boolean isFacebookAuthorized(Facebook facebook) {
		return facebook.isAuthorized();
	}
		
	public String verifyRegistration(User user) {
		String errors = "";
		
		errors += verifyUsersFirstName(user.getFirstName());
		errors += verifyUsersLastName(user.getLastName());
		errors += verifyUsersEmail(user.getEmail());
		errors += verifyUsersPassword(user.getPassword());
		
		if (errors.isEmpty()) {
			saveUser(user);
			authenticateUser(user);
		}
		return errors;
	}
	
	public String verifyUsersFirstName(String firstName) {	
		return firstName.isEmpty() ? "first name is empty " : "";
	}
	
	public String verifyUsersLastName(String lastName) {
		return lastName.isEmpty() ? "last name is empty " : "";
	}
	
	public String verifyUsersEmail(String email) {
		if (email.isEmpty()) {
			return "email is empty ";
		} else if (databaseIO.getUserFromDbByEmail(email) != null) {
			return "email is already taken ";
		}
		return "";
	}
		
	public String verifyUsersPassword(String password) {
		return password.isEmpty() ? "password is empty" : "";
	}
	
	public void saveUser(User user) {
		databaseIO.saveUserToDatabase(user);
	}
	
	public void authenticateUser(User user) {
		authentications.authenticateUser(user);
	}
	
	public boolean doesUserAlreadyExistInDatabase(String email) {
		if (databaseIO.getUserFromDbByEmail(email)!=null || databaseIO.getUserFromDbByFacebookId(email)!=null) {
			return true;
		}
		return false;
	}
}