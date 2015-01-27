package package1.utils;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import package1.entities.User;

/**
 * Translates users from entities to spring security users
 */
@Component
public class ConvertUsers {

	@Transactional(readOnly = true)
	public org.springframework.security.core.userdetails.User convertUser(User user) {
		String username = user.getEmail();
		String password = user.getPassword();

		Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority("user"));

		return new org.springframework.security.core.userdetails.User(username, password, authorities);
	}
}