package spring.facebook.entities;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="comments")
public class Comment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long commentId;
    
    @ManyToOne(fetch=FetchType.LAZY, cascade={ CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH })
    @JoinColumn(name="facebook_post_id")
    private FacebookPost facebookPost;
    
    private String commAuthorsName;
    private Date commDateCreated;
    private String commText;
    
    public Comment() {
        
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public FacebookPost getFacebookPost() {
        return facebookPost;
    }

    public void setFacebookPost(FacebookPost facebookPost) {
        this.facebookPost = facebookPost;
    }

    public String getCommAuthorsName() {
        return commAuthorsName;
    }

    public void setCommAuthorsName(String commAuthorsName) {
        this.commAuthorsName = commAuthorsName;
    }

    public Date getCommDateCreated() {
        return commDateCreated;
    }

    public void setCommDateCreated(Date commDateCreated) {
        this.commDateCreated = commDateCreated;
    }

    public String getCommText() {
        return commText;
    }

    public void setCommText(String commText) {
        this.commText = commText;
    }
}