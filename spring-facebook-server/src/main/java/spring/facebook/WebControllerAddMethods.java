package spring.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import spring.facebook.utils.FillingContentOnThePage;

@Component
public class WebControllerAddMethods {

    @Autowired
    private FillingContentOnThePage fillingContentOnThePage;
    
    public String getAuthenticatedUsersName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    public String getHello2Page(Model model) {
        fillingContentOnThePage.fillFacebookHomePageFromDatabase(model, getAuthenticatedUsersName());
        return "hello2";
    }
}