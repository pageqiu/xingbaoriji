package cn.pageqiu.module.reply.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pageqiu.module.reply.dao.ReplyDao;
import cn.pageqiu.module.reply.entity.Reply;
import cn.pageqiu.module.topic.entity.Topic;
import cn.pageqiu.module.topic.service.TopicService;
import cn.pageqiu.module.user.entity.User;
import cn.pageqiu.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Service
@Transactional
public class ReplyService {

    @Autowired
    private ReplyDao replyDao;
    @Autowired
    private TopicService topicService;

    public Reply findById(int id) {
        return replyDao.findOne(id);
    }

    public void save(Reply reply) {
        replyDao.save(reply);
    }

    public Map delete(int id, User user) {
        Map map = new HashMap();
        Reply reply = findById(id);
        if (reply != null && user.getId() == reply.getUser().getId()) {
            map.put("topicId", reply.getTopic().getId());
            topicService.reduceOneReplyCount(reply.getTopic().getId());
            replyDao.delete(id);
        }
        return map;
    }

    /**
     * 删除用户发布的所有回复
     *
     * @param user
     */
    public void deleteByUser(User user) {
        replyDao.deleteByUser(user);
    }

    /**
     * 根据话题删除回复
     *
     * @param topicId
     */
    public void deleteByTopic(int topicId) {
        replyDao.deleteByTopicId(topicId);
    }

    /**
     * 对回复点赞
     *
     * @param userId
     * @param replyId
     */
    public void addOneUp(int userId, int replyId) {
        Reply reply = findById(replyId);
        if (reply != null) {
            reply.setUp(reply.getUp() + 1);
            String upIds = reply.getUpIds();
            reply.setUpIds((upIds == null ? Constants.COMMA : upIds) + userId + Constants.COMMA);
            save(reply);
        }
    }

    /**
     * 对回复取消点赞
     *
     * @param userId
     * @param replyId
     */
    public void reduceOneUp(int userId, int replyId) {
        Reply reply = findById(replyId);
        if (reply != null && reply.getUpIds().contains(Constants.COMMA + userId + Constants.COMMA)) {
            reply.setUp(reply.getUp() - 1);
            String upIds = reply.getUpIds();
            upIds = upIds.replace(Constants.COMMA + userId + Constants.COMMA, Constants.COMMA);
            reply.setUpIds(upIds);
            reply.setUpIds(reply.getUpIds() + userId + Constants.COMMA);
            save(reply);
        }
    }

    /**
     * 根据话题查询回复列表
     *
     * @param topic
     * @return
     */
    public List<Reply> findByTopic(Topic topic) {
        return replyDao.findByTopicOrderByInTime(topic);
    }

    /**
     * 分页查询回复列表
     *
     * @param p
     * @param size
     * @return
     */
    public Page<Reply> page(int p, int size) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "inTime"));
        Pageable pageable = new PageRequest(p - 1, size, sort);
        return replyDao.findAll(pageable);
    }

    /**
     * 查询用户的回复列表
     *
     * @param p
     * @param size
     * @param user
     * @return
     */
    public Page<Reply> findByUser(int p, int size, User user) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "inTime"));
        Pageable pageable = new PageRequest(p - 1, size, sort);
        return replyDao.findByUser(user, pageable);
    }
}
