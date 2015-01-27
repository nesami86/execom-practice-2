package package1.databaseStaffs;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import package1.entities.FacebookPost;

@Component
public interface FacebookPostRepository extends CrudRepository<FacebookPost, Long> {
	
	FacebookPost findByRealFacebookId(String realFacebookId);
	
	FacebookPost findByFacebookPostId(long facebookPostId);
}