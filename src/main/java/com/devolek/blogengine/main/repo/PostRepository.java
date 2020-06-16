package com.devolek.blogengine.main.repo;

import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAllByTagsContains(Tag tag);

    List<Post> findAllByTitleContainingOrTextContaining(String title, String text);

    List<Post> findAllByTimeContaining(Calendar time);

    List<Post> findAllByModerationStatus(ModerationStatus moderationStatus);

    List<Post> findAllByModeratorId(int moderatorId);

    List<Post> findAllByUser(User user);
}
