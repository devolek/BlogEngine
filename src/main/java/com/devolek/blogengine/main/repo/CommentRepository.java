package com.devolek.blogengine.main.repo;

import com.devolek.blogengine.main.model.PostComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository  extends JpaRepository<PostComment, Integer> {

}
