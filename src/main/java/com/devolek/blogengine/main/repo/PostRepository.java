package com.devolek.blogengine.main.repo;

import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.Tag;
import com.devolek.blogengine.main.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    List<Post> findAllByTagsContains(Tag tag);

    List<Post> findAllByTimeContaining(Calendar time);

    List<Post> findAllByModerationStatus(ModerationStatus moderationStatus);

    List<Post> findAllByUser(User user);

    @Query(value = "select count(p) from Post p where " +
            "p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= current_time")
    int getAvailablePostCount();

    @Query(value = "select p from Post p where " +
            "p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= current_time " +
            "order by p.time desc")
    List<Post> getRecentPosts (Pageable pageable);

    @Query(value = "select p from Post p where " +
            "p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= current_time " +
            "order by p.comments.size desc")
    List<Post> getPopularPosts (Pageable pageable);

    @Query(value = "select p from Post p join p.postVotes v " +
            "where p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= current_time " +
            "group by p " +
            "order by sum (case when v.value = 1 then 1 else 0 end) desc")
    List<Post> getBestPosts (Pageable pageable);

    @Query(value = "select p from Post p where " +
            "p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= current_time " +
            "order by p.time asc")
    List<Post> getEarlyPosts (Pageable pageable);

    @Query(value = "select p from Post p where " +
            "p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= current_time and " +
            "(:query is null or p.title like %:query%) or " +
            "p.text like %:query% " +
            "order by p.time desc")
    List<Post> search(@Param("query")String query, Pageable pageable);

    @Query(value = "select count (p) from Post p where " +
            "p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= current_time and " +
            "(:query is null or p.title like %:query%) or " +
            "p.text like %:query% " +
            "order by p.time desc")
    int searchCount(@Param("query")String query);
}
