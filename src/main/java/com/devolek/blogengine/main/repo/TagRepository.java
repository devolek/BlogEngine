package com.devolek.blogengine.main.repo;


import com.devolek.blogengine.main.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    List<Tag> findAllByNameStartsWith(String name);

    Tag findFirstByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
