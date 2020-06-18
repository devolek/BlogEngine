package com.devolek.blogengine.main.repo;

import com.devolek.blogengine.main.model.Post;
import com.devolek.blogengine.main.model.PostVote;
import com.devolek.blogengine.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostVotesRepository extends JpaRepository<PostVote, Integer> {
    PostVote findByUserAndPost(User user, Post post);
}
