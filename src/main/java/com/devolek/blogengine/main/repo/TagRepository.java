package com.devolek.blogengine.main.repo;


import com.devolek.blogengine.main.model.Tag;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    @Query(value = "select t from Tag t where " +
            "(:query is null or t.name like %:query%)")
    List<Tag> getAllTagsWithQuery(@Param("query") String query);

    Tag findFirstByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
