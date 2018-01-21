package cn.pageqiu.module.security.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.pageqiu.module.security.entity.Role;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Repository
@CacheConfig(cacheNames = "roles")
public interface RoleDao extends JpaRepository<Role, Integer> {

    @Cacheable
    Role findOne(int id);

    @Cacheable
    List<Role> findAll();

    void delete(int id);
}
