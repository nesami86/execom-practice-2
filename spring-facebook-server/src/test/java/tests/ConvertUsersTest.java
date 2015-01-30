package tests;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring.facebook.database.DatabaseIO;
import spring.facebook.entities.User;
import spring.facebook.utils.Authentications;
import spring.facebook.utils.Checks;
import spring.facebook.utils.ConvertUsers;
import spring.facebook.utils.UserDetailsServiceImpl;
import spring.facebook.utils.WorksWithEntities;
import static org.junit.Assert.assertEquals;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestBeans.class, ConvertUsersTest.TestConfig.class})
public class ConvertUsersTest {

    @Configuration
    static class TestConfig {
            
        @Bean
        public ConvertUsers getConvertUsersInstance() {
            return new ConvertUsers();
        }
        
        @Bean
        public Checks getChecksMock() {
            return mock(Checks.class);
        }
        
        @Bean
        public DatabaseIO getDatabaseIOMock() {
            return mock(DatabaseIO.class);
        }
        
        @Bean
        public Authentications getAuthenticationsMock() {
            return mock(Authentications.class);
        }
        
        @Bean
        public WorksWithEntities getWorksWithEntitiesMock() {
            return mock(WorksWithEntities.class);
        }
        
        @Bean
        public UserDetailsServiceImpl getUsersDetailsServiceImplMock() {
            return mock(UserDetailsServiceImpl.class);
        }
    }
    
    @Autowired
    private ConvertUsers convertUsers;
    
    @Autowired
    private User user;
    
    @Test
    public void convertUserTest() {
        when(user.getEmail()).thenReturn("AAA");
        when(user.getPassword()).thenReturn("BBB");
        
        assertEquals("AAA", convertUsers.convertUser(user).getUsername());
    }
}