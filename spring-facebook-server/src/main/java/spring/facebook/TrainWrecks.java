package spring.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Component;

@Component
public class TrainWrecks {

    @Autowired
    private ConnectionRepository connectionRepository;
    
    public String getAuthenticatedUsersName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    public String getFacebookConnectionId() {
        return connectionRepository.findPrimaryConnection(Facebook.class).getKey().getProviderId();
    }
    
    public Authentication getNewToken(UserDetails userDetails) {
        return new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
    }
}