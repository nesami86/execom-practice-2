package package1.databaseStaffs;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import package1.entities.User;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String email);
	
	User findByFacebookId(String facebookId);
	
	List<User> findAll();
}