package cn.pageqiu.module.topic.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.pageqiu.module.topic.entity.Topic;
import cn.pageqiu.module.user.entity.User;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Repository
@CacheConfig(cacheNames = "topics")
public interface TopicDao extends JpaRepository<Topic, Integer> {

    @Cacheable
    Topic findOne(int id);

    void delete(int id);

    @Cacheable
    Page<Topic> findByTab(String tab, Pageable pageable);

    @Cacheable
    Page<Topic> findByUser(User user, Pageable pageable);

    void deleteByUser(User user);

    @Cacheable
    Page<Topic> findByGood(boolean b, Pageable pageable);

    @Cacheable
    Page<Topic> findByReplyCount(int i, Pageable pageable);
}
