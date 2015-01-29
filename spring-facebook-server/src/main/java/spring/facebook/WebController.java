package spring.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.facebook.entities.User;
import spring.facebook.utils.Authentications;
import spring.facebook.utils.Checks;
import spring.facebook.utils.FillingContentOnThePage;
import spring.facebook.utils.WorksWithEntities;

@Controller
@RequestMapping("/")
public class WebController extends TrainWrecks {

    @Autowired
    private Facebook facebook;
    
    @Autowired
    private Checks checks;
    
    @Autowired
    private FillingContentOnThePage fillingContentOnThePage;
    
    @Autowired
    private Authentications authentications;
    
    @Autowired
    private WorksWithEntities worksWithEntities;
        
    @RequestMapping(value="/", method = RequestMethod.GET)
    public String initialPageSelector(Model model) {
        boolean userExists = checks.doesUserAlreadyExistInDatabase(getAuthenticatedUsersName());
        
        if (checks.isFacebookAuthorized(facebook) && !userExists) {
            authentications.authenticateFacebookUser(facebook);
            fillingContentOnThePage.fillFacebookHomePageFromConnection(model, facebook);
            return "hello";
        } else if (checks.isFacebookAuthorized(facebook) && userExists) {
            User user = worksWithEntities.synchronizeFacebookUser(facebook, getAuthenticatedUsersName());
            fillingContentOnThePage.fillFacebookHomePageFromUser(model, user);
            return "hello2";
        }
        return "redirect:/connect/facebook";
    }
    
    @RequestMapping("/login")
    public String getLoginForm() {
        return "login";
    }
    
    @RequestMapping("/registerForm")
    public String getRegisterForm() {
        return "register";
    }
    
    @RequestMapping("/register")
    public @ResponseBody String register(@RequestBody User user) {
        return checks.verifyRegistration(user);
    }
    
    @RequestMapping("/profile")
    public String getProfilePage(Model model) {
        fillingContentOnThePage.fillUsersProfilePage(model, getAuthenticatedUsersName());
        return "profile";
    }
        
    @RequestMapping("/facebookHome")
    public String getFacebookHomePage(Model model) {
        return getHello2Page(model);
    }
    
    @RequestMapping("/protectedPage")
    public String getProtectedPage(Model model) {
        fillingContentOnThePage.fillProtectedPage(model, getAuthenticatedUsersName());
        return "protectedPage";
    }
        
    @RequestMapping("/signout")
    public String logout() {
        authentications.deauthenticateUser();
        return "redirect:/";
    }
    
    @RequestMapping("/deleteComment")
    public String deleteComment(@RequestParam("commentId") String id, Model model) {
        worksWithEntities.deleteComment(Long.parseLong(id));
        return getHello2Page(model);
    }
    
    @RequestMapping("/addComment")
    public String addComment(@RequestParam("text") String text, @RequestParam("postId") String postId, Model model) {
        worksWithEntities.addComment(text, Long.parseLong(postId), getAuthenticatedUsersName());        
        return getHello2Page(model);
    }
    
    @RequestMapping("/removePost")
    public String removePost(@RequestParam("postId") String id, Model model) {
        worksWithEntities.removePostFromUsersHomePage(Long.parseLong(id), getAuthenticatedUsersName());
        return getHello2Page(model);
    }
    
    @RequestMapping("/deletePost")
    public String deletePost(@RequestParam("postId") String id, Model model) {
        worksWithEntities.deletePost(Long.parseLong(id));
        return getHello2Page(model);
    }
    
    @RequestMapping("/addPost")
    public String addPost(@RequestParam("text") String text, Model model) {
        worksWithEntities.createPost(text, getAuthenticatedUsersName());
        return getHello2Page(model);
    }
    
    public String getHello2Page(Model model) {
        fillingContentOnThePage.fillFacebookHomePageFromDatabase(model, getAuthenticatedUsersName());
        return "hello2";
    }
}