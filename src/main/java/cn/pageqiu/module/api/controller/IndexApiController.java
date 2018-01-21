package cn.pageqiu.module.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.pageqiu.common.BaseController;
import cn.pageqiu.common.config.SiteConfig;
import cn.pageqiu.exception.ApiException;
import cn.pageqiu.exception.Result;
import cn.pageqiu.module.topic.entity.Topic;
import cn.pageqiu.module.topic.service.TopicService;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@RestController
@RequestMapping("/api")
public class IndexApiController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private SiteConfig siteConfig;

    /**
     * 话题列表接口
     * @param tab
     * @param p
     * @return
     * @throws ApiException
     */
    @GetMapping("/index")
    public Result index(String tab, Integer p) throws ApiException {
        if(!StringUtils.isEmpty(tab) && !siteConfig.getSections().contains(tab)) throw new ApiException("版块不存在");
        if (StringUtils.isEmpty(tab)) tab = "全部";
        Page<Topic> page = topicService.page(p == null ? 1 : p, siteConfig.getPageSize(), tab);
        return Result.success(page);
    }
}
