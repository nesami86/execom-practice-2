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
    
    private String authorsName;
    private Date dateCreated;
    private String text;
    
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
}