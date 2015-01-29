package spring.facebook.database;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import spring.facebook.entities.User;

@Component
public interface UserRepository extends CrudRepository<User, Long> {

    User findByEmail(String email);
    
    User findByFacebookId(String facebookId);
    
    @Override
    List<User> findAll();
}