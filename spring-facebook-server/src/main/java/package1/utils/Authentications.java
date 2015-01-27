package package1.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

import package1.TrainWrecks;
import package1.databaseStaffs.DatabaseIO;
import package1.entities.User;

@Component
public class Authentications extends TrainWrecks {

	@Autowired
    private UserDetailsServiceImpl userDetailsService;
	
	@Autowired
	private ConnectionRepository connectionRepository;
	
	@Autowired
	private ConvertUsers convertUsers;
	
	@Autowired
	private WorksWithEntities worksWithEntities;
	
	@Autowired
	private DatabaseIO databaseIO;
		
	public void authenticateUser(User user) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());
		createUsersToken(userDetails);
	}
	
	public void createUsersToken(UserDetails userDetails) {
		Authentication authentication = getNewToken(userDetails);
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}
	
	public void deauthenticateUser() {
		destroyFacebookConnection();
		SecurityContextHolder.clearContext();
	}
	
	public void destroyFacebookConnection() {
		if (connectionRepository.findConnections(Facebook.class).size() > 0) {
			connectionRepository.removeConnections(getFacebookConnectionId());
		}
	}
	
	public void authenticateFacebookUser(Facebook facebook) {
		User user = databaseIO.getUserFromDbByFacebookId(facebook.userOperations().getUserProfile().getId());
		if (user == null) {
			user = worksWithEntities.createUserFromFacebookUser(facebook);
		}	
		
		UserDetails userDetails = convertUsers.convertUser(user);
		
		/**
		 * 	because a new session is created after authentication we had to 
		 * 	terminate facebook connection first but we can still get data from database
		 */
		destroyFacebookConnection();
		
		createUsersToken(userDetails);
	}
}