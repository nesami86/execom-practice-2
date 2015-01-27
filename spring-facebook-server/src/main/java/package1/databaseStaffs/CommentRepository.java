package package1.databaseStaffs;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;

import package1.entities.Comment;

@Component
public interface CommentRepository extends CrudRepository<Comment, Long> {

	Comment findByCommentId(String commentId);
}