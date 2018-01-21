package cn.pageqiu.module.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.pageqiu.module.reply.service.ReplyService;
import cn.pageqiu.module.topic.service.TopicService;
import cn.pageqiu.module.user.dao.UserDao;
import cn.pageqiu.module.user.entity.User;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Service
@Transactional
public class UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private TopicService topicService;
    @Autowired
    private ReplyService replyService;

    public User findById(int id) {
        return userDao.findOne(id);
    }

    /**
     * 根据用户名判断是否存在
     *
     * @param username
     * @return
     */
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    public void save(User user) {
        userDao.save(user);
    }

    public void updateUser(User user) {
        userDao.save(user);
    }

    /**
     * 分页查询用户列表
     *
     * @param p
     * @param size
     * @return
     */
    public Page<User> pageUser(int p, int size) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "inTime"));
        Pageable pageable = new PageRequest(p - 1, size, sort);
        return userDao.findAll(pageable);
    }

    public void deleteById(int id) {
        User user = findById(id);
        //删除该用户的所有话题
        topicService.deleteByUser(user);
        //删除用户发的所有回复
        replyService.deleteByUser(user);
        //删除用户
        userDao.delete(user);
    }
}
