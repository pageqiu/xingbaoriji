package cn.pageqiu.module.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pageqiu.common.BaseController;
import cn.pageqiu.exception.ApiException;
import cn.pageqiu.exception.Result;
import cn.pageqiu.module.collect.service.CollectService;
import cn.pageqiu.module.reply.entity.Reply;
import cn.pageqiu.module.reply.service.ReplyService;
import cn.pageqiu.module.topic.entity.Topic;
import cn.pageqiu.module.topic.service.TopicService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@RestController
@RequestMapping("/api/topic")
public class TopicApiController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private CollectService collectService;

    /**
     * 话题详情
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result detail(String token, @PathVariable Integer id) throws ApiException {
        Topic topic = topicService.findById(id);
        if(topic == null) throw new ApiException("话题不存在");
        Map<String, Object> map = new HashMap<>();
        //浏览量+1
        topic.setView(topic.getView() + 1);
        topicService.save(topic);//更新话题数据
        List<Reply> replies = replyService.findByTopic(topic);
        map.put("topic", topic);
        map.put("replies", replies);
        map.put("author", topic.getUser());
        map.put("collectCount", collectService.countByTopic(topic));
        return Result.success(map);
    }
}
