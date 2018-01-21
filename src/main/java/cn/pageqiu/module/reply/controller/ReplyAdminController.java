package cn.pageqiu.module.reply.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.pageqiu.common.BaseController;
import cn.pageqiu.common.config.SiteConfig;
import cn.pageqiu.module.reply.entity.Reply;
import cn.pageqiu.module.reply.service.ReplyService;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Controller
@RequestMapping("/admin/reply")
public class ReplyAdminController extends BaseController {

    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private ReplyService replyService;

    /**
     * 回复列表
     *
     * @param p
     * @param model
     * @return
     */
    @GetMapping("/list")
    public String list(Integer p, Model model) {
        Page<Reply> page = replyService.page(p == null ? 1 : p, siteConfig.getPageSize());
        model.addAttribute("page", page);
        return render("/admin/reply/list");
    }

}
