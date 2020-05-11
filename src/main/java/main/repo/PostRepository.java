package main.repo;

import main.enums.ModerationStatus;
import main.model.Post;
import main.model.Tag;
import main.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.xml.crypto.Data;
import java.util.Calendar;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAllByTagsContains(Tag tag);

    List<Post> findAllByTitleContainingOrTextContaining(String title, String text);

    List<Post> findAllByTimeContaining(Calendar time);

    List<Post> findAllByModerationStatusAndModeratorId(ModerationStatus moderationStatus, int moderatorId);

    List<Post> findAllByUser(User user);
}
