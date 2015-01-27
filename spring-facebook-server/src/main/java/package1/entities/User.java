package package1.entities;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long userId;
	
	private String firstName;
	private String lastName;
	private String email;
	private String password;
	private String facebookId;
	
	@ManyToMany(fetch=FetchType.LAZY, 
		cascade={ CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinTable(name="users_facebook_posts", 
		joinColumns={ @JoinColumn(name="user_id") }, inverseJoinColumns={ @JoinColumn(name="facebook_post_id") })
	@OrderBy("date_created DESC")
	private List<FacebookPost> facebookHomePagePosts;
		
	public User() {
		
	}
	
	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
		
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}

	public String getFacebookId() {
		return facebookId;
	}

	public void setFacebookId(String facebookId) {
		this.facebookId = facebookId;
	}

	public List<FacebookPost> getFacebookHomePagePosts() {
		return facebookHomePagePosts;
	}

	public void setFacebookHomePagePosts(List<FacebookPost> facebookHomePagePosts) {
		this.facebookHomePagePosts = facebookHomePagePosts;
	}
	
	public void removePostWithGivenId(long id) {
		this.getFacebookHomePagePosts().remove(getPostsPositionInList(id));
	}
	
	public int getPostsPositionInList(long id) {
		for (int i = 0; i < this.getFacebookHomePagePosts().size(); i++) {
			
			if (this.getFacebookHomePagePosts().get(i).getFacebookPostId() == id) {
				return i;
			}
		}
		return 0;
	}
}