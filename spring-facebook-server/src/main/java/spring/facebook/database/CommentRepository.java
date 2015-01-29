package spring.facebook.database;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import spring.facebook.entities.Comment;

@Component
public interface CommentRepository extends CrudRepository<Comment, Long> {

    Comment findByCommentId(String commentId);
}