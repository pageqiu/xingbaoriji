package cn.pageqiu.module.reply.dao;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.pageqiu.module.reply.entity.Reply;
import cn.pageqiu.module.topic.entity.Topic;
import cn.pageqiu.module.user.entity.User;

import java.util.List;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Repository
@CacheConfig(cacheNames = "replies")
public interface ReplyDao extends JpaRepository<Reply, Integer> {

    List<Reply> findByTopicOrderByInTimeDesc(Topic topic);

    void deleteByTopicId(int topicId);

    void deleteByUser(User user);

    @Cacheable
    Page<Reply> findByUser(User user, Pageable pageable);
}
