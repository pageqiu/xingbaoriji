package cn.pageqiu.module.collect.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.pageqiu.module.collect.entity.Collect;
import cn.pageqiu.module.topic.entity.Topic;
import cn.pageqiu.module.user.entity.User;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Repository
@CacheConfig(cacheNames = "collects")
public interface CollectDao extends JpaRepository<Collect, Integer> {

    @Cacheable
    Page<Collect> findByUser(User user, Pageable pageable);

    @Cacheable
    long countByUser(User user);

    @Cacheable
    long countByTopic(Topic topic);

    @Cacheable
    Collect findByUserAndTopic(User user, Topic topic);

}
