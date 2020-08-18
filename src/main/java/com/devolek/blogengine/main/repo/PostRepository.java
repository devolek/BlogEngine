package com.devolek.blogengine.main.repo;

import com.devolek.blogengine.main.enums.ModerationStatus;
import com.devolek.blogengine.main.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;


@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
    String defaultCondition = "p.isActive = 1 and " +
            "p.moderationStatus = com.devolek.blogengine.main.enums.ModerationStatus.ACCEPTED and " +
            "p.time <= sysdate()";

    @Query(value = "select count(p) from Post p where " +
            defaultCondition)
    int getAvailablePostCount();

    @Query(value = "select p from Post p where " +
            defaultCondition +
            " and (?1 is null or (p.time between ?1 and ?2))" +
            " order by p.time desc")
    List<Post> getAvailablePosts(Calendar dateFrom, Calendar dateTo);

    @Query(value = "select p.time from Post p where " +
            defaultCondition +
            " order by p.time desc")
    List<Calendar> getAvailableDate();

    @Query(value = "select p from Post p where " +
            defaultCondition +
            " order by p.time desc")
    Page<Post> getRecentPosts(Pageable pageable);

    @Query(value = "select p from Post p where " +
            defaultCondition +
            " order by p.comments.size desc")
    Page<Post> getPopularPosts(Pageable pageable);

    @Query(value = "select p from Post p join p.postVotes v where " +
            defaultCondition +
            " group by p " +
            "order by sum (case when v.value = 1 then 1 else 0 end) desc")
    Page<Post> getBestPosts(Pageable pageable);

    @Query(value = "select p from Post p where " +
            defaultCondition +
            " order by p.time asc")
    Page<Post> getEarlyPosts(Pageable pageable);

    @Query(value = "select p from Post p where " +
            defaultCondition +
            " and ((:query is null or p.title like %:query%) or " +
            "p.text like %:query%) " +
            "order by p.time desc")
    Page<Post> search(@Param("query") String query, Pageable pageable);

    @Query(value = "select p from Post p where " +
            defaultCondition +
            " and p.id = :id")
    Post getActiveById(@Param("id") int id);

    @Query(value = "select p from Post p where " +
            defaultCondition +
            " and (p.time between :dateFrom and :dateTo) " +
            "order by p.time desc")
    Page<Post> getByDate(@Param("dateFrom") Calendar dateFrom,
                         @Param("dateTo") Calendar dateTo,
                         Pageable pageable);

    @Query(value = "select count(p) from Post p join p.tags t where " +
            defaultCondition +
            " and t.name = :tag")
    Integer getCountByTag(@Param("tag") String tag);

    @Query(value = "select p from Post p join p.tags t where " +
            defaultCondition +
            " and t.name = :tag " +
            "group by p " +
            "order by p.time desc")
    Page<Post> getByTag(@Param("tag") String tag, Pageable pageable);

    @Query(value = "select count(p) from Post p where " +
            "p.isActive = 1 and " +
            "(:moderatorId is null or p.moderatorId = :moderatorId) and " +
            "p.moderationStatus = :moderationStatus " +
            "order by p.time desc")
    int getCountModeration(@Param("moderationStatus") ModerationStatus moderationStatus,
                           @Param("moderatorId") Integer moderatorId);

    @Query(value = "select p from Post p where " +
            "p.isActive = 1 and " +
            "(:moderatorId is null or p.moderatorId = :moderatorId) and " +
            "p.moderationStatus = :moderationStatus " +
            "order by p.time desc")
    Page<Post> getModerationPosts(@Param("moderationStatus") ModerationStatus moderationStatus,
                                  @Param("moderatorId") Integer moderatorId,
                                  Pageable pageable);

    @Query(value = "select p from Post p where " +
            "p.isActive = :isActive and " +
            "p.user.id = :userId and " +
            "(:moderationStatus is null or p.moderationStatus = :moderationStatus) " +
            "order by p.time desc")
    Page<Post> getMyPosts(@Param("moderationStatus") ModerationStatus moderationStatus,
                          @Param("isActive") int isActive,
                          @Param("userId") int userId,
                          Pageable pageable);
}
