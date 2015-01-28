package spring.facebook.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import spring.facebook.entities.FacebookPost;

@Component
public interface FacebookPostRepository extends CrudRepository<FacebookPost, Long> {
	
	FacebookPost findByRealFacebookId(String realFacebookId);
	
	FacebookPost findByFacebookPostId(long facebookPostId);
}