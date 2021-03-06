package com.devolek.blogengine.main.repo;


import com.devolek.blogengine.main.model.GlobalSetting;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface GlobalSettingRepository extends CrudRepository<GlobalSetting, Integer> {
    Optional<GlobalSetting> findByCode(String code);
}
