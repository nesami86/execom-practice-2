package spring.facebook.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import spring.facebook.database.UserRepository;
import spring.facebook.entities.User;

/**
 * Gets converted users from database
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private ConvertUsers convertUsers;

	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found!");
		}
		return convertUsers.convertUser(user);
	}
}