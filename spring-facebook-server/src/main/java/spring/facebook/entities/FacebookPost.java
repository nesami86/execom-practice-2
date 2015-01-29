package spring.facebook.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="facebook_posts")
public class FacebookPost {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long facebookPostId;
    
    private String realFacebookId;
        
    @ManyToMany(mappedBy="facebookHomePagePosts", fetch=FetchType.LAZY, 
        cascade={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
    private List<User> usersWithThisPost;
    
    private String authorsName;
    private Date dateCreated;
    private String text;
    private String imageUrl;
    
    @OneToMany(mappedBy="facebookPost", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("comm_date_created ASC")
    private List<Comment> comments;
    
    public FacebookPost() {
        
    }

    public long getFacebookPostId() {
        return facebookPostId;
    }

    public void setFacebookPostId(long facebookPostId) {
        this.facebookPostId = facebookPostId;
    }

    public String getRealFacebookId() {
        return realFacebookId;
    }

    public void setRealFacebookId(String realFacebookId) {
        this.realFacebookId = realFacebookId;
    }

    public List<User> getUsersWithThisPost() {
        return usersWithThisPost;
    }

    public void setUsersWithThisPost(List<User> usersWithThisPost) {
        this.usersWithThisPost = usersWithThisPost;
    }

    public String getAuthorsName() {
        return authorsName;
    }

    public void setAuthorsName(String authorsName) {
        this.authorsName = authorsName;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    
    public boolean isUserInTheListOfUsers(User user) {
        for (User u : this.usersWithThisPost) {
            if (u.getFacebookId().equals(user.getFacebookId())) {
                return true;
            }
        }
        return false;
    }
}