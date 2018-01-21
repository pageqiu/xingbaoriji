package cn.pageqiu.module.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import cn.pageqiu.common.BaseController;
import cn.pageqiu.common.config.SiteConfig;
import cn.pageqiu.module.collect.service.CollectService;
import cn.pageqiu.module.reply.service.ReplyService;
import cn.pageqiu.module.topic.service.TopicService;
import cn.pageqiu.module.user.entity.User;
import cn.pageqiu.module.user.service.UserService;
import cn.pageqiu.util.FileUploadEnum;
import cn.pageqiu.util.FileUtil;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Controller
@RequestMapping("/user")
public class UserController extends BaseController {

    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private TopicService topicService;
    @Autowired
    private ReplyService replyService;
    @Autowired
    private UserService userService;
    @Autowired
    private CollectService collectService;
    @Autowired
    private FileUtil fileUtil;

    /**
     * 个人资料
     *
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String profile(@PathVariable String username, Model model) {
        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            model.addAttribute("user", getUser());
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("collectCount", collectService.countByUser(currentUser));
            model.addAttribute("topicPage", topicService.findByUser(1, 7, currentUser));
            model.addAttribute("replyPage", replyService.findByUser(1, 7, currentUser));
            model.addAttribute("pageTitle", currentUser.getUsername() + " 个人主页");
        } else {
            model.addAttribute("pageTitle", "用户未找到");
        }
        return render("/user/info");
    }

    /**
     * 用户发布的所有话题
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}/topics")
    public String topics(@PathVariable String username, Integer p, HttpServletResponse response, Model model) {
        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("page", topicService.findByUser(p == null ? 1 : p, siteConfig.getPageSize(), currentUser));
            return render("/user/topics");
        } else {
            renderText(response, "用户不存在");
            return null;
        }
    }

    /**
     * 用户发布的所有回复
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}/replies")
    public String replies(@PathVariable String username, Integer p, HttpServletResponse response, Model model) {
        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("page", replyService.findByUser(p == null ? 1 : p, siteConfig.getPageSize(), currentUser));
            return render("/user/replies");
        } else {
            renderText(response, "用户不存在");
            return null;
        }
    }

    /**
     * 用户收藏的所有话题
     *
     * @param username
     * @return
     */
    @GetMapping("/{username}/collects")
    public String collects(@PathVariable String username, Integer p, HttpServletResponse response, Model model) {
        User currentUser = userService.findByUsername(username);
        if (currentUser != null) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("page", collectService.findByUser(p == null ? 1 : p, siteConfig.getPageSize(), currentUser));
            return render("/user/collects");
        } else {
            renderText(response, "用户不存在");
            return null;
        }
    }

    /**
     * 进入用户个人设置页面
     *
     * @param model
     * @return
     */
    @GetMapping("/setting")
    public String setting(Model model) {
        model.addAttribute("user", getUser());
        return render("/user/setting");
    }

    /**
     * 更新用户的个人设置
     *
     * @param email
     * @param url
     * @param signature
     * @param response
     * @return
     */
    @PostMapping("/setting")
    public String updateUserInfo(String email, String url, String signature, @RequestParam("avatar") MultipartFile avatar, HttpServletResponse response) throws IOException {
        User user = getUser();
        user.setEmail(email);
        user.setSignature(signature);
        user.setUrl(url);
        String requestUrl = fileUtil.uploadFile(avatar, FileUploadEnum.AVATAR);
        if (!StringUtils.isEmpty(requestUrl)) {
            user.setAvatar(requestUrl);
        }
        userService.updateUser(user);
        return redirect(response, "/user/" + user.getUsername());
    }

}
