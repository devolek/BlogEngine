package com.devolek.blogengine.main.repo;


import com.devolek.blogengine.main.model.Tag;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    Optional<Tag> findByName(String name);

    List<Tag> findAllByNameStartsWith (String name);
}
