package cn.pageqiu.module.index.controller;

import org.jsoup.helper.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import cn.pageqiu.common.BaseController;
import cn.pageqiu.common.config.SiteConfig;
import cn.pageqiu.exception.Result;
import cn.pageqiu.module.topic.entity.Topic;
import cn.pageqiu.module.topic.service.TopicService;
import cn.pageqiu.module.user.entity.User;
import cn.pageqiu.module.user.service.UserService;
import cn.pageqiu.util.FileUploadEnum;
import cn.pageqiu.util.FileUtil;
import cn.pageqiu.util.identicon.Identicon;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
@Controller
public class IndexController extends BaseController {

    @Autowired
    private TopicService topicService;
    @Autowired
    private UserService userService;
    @Autowired
    private SiteConfig siteConfig;
    @Autowired
    private Identicon identicon;
    @Autowired
    private FileUtil fileUtil;

    /**
     * 首页
     *
     * @return
     */
    @GetMapping("/")
    public String index(String tab, Integer p, Model model) {
        String sectionName = tab;
        if (StringUtils.isEmpty(tab)) tab = "全部";
        if (tab.equals("全部") || tab.equals("精华") || tab.equals("等待回复")) {
            sectionName = "版块";
        }
        Page<Topic> page = topicService.page(p == null ? 1 : p, siteConfig.getPageSize(), tab);
        model.addAttribute("page", page);
        model.addAttribute("tab", tab);
        model.addAttribute("sectionName", sectionName);
        model.addAttribute("user", getUser());
        return render("/index");
    }

    /**
     * 进入登录页
     *
     * @return
     */
    @GetMapping("/login")
    public String toLogin(String s, Model model, HttpServletResponse response) {
        if (getUser() != null) {
            return redirect(response, "/");
        }
        model.addAttribute("s", s);
        return render("/login");
    }

    /**
     * 进入注册页面
     *
     * @return
     */
    @GetMapping("/register")
    public String toRegister(HttpServletResponse response) {
        if (getUser() != null) {
            return redirect(response, "/");
        }
        return render("/register");
    }

    /**
     * 注册验证
     *
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/register")
    public String register(String username, String password,String password2,String checkCode,HttpServletRequest request, HttpServletResponse response, Model model) {
    	
    	String valiedCode = (String)request.getSession().getAttribute("checkCode");
    	
    	if(StringUtil.isBlank(valiedCode) || !valiedCode.equals(checkCode)){
    		model.addAttribute("username", username);
    		model.addAttribute("password", password);
    		model.addAttribute("password2", password2);
    		model.addAttribute("errors", "请输入正确的验证码");
    		return render("/register");
    	}
    	
    	
    	User user = userService.findByUsername(username);
        if (user != null) {
            model.addAttribute("errors", "用户名已经被注册");
            model.addAttribute("username", username);
    		model.addAttribute("password", password);
    		model.addAttribute("password2", password2);
        } else if (StringUtils.isEmpty(username)) {
            model.addAttribute("errors", "用户名不能为空");
            model.addAttribute("username", username);
    		model.addAttribute("password", password);
    		model.addAttribute("password2", password2);
        } else if (StringUtils.isEmpty(password)) {
            model.addAttribute("errors", "密码不能为空");
            model.addAttribute("username", username);
    		model.addAttribute("password", password);
    		model.addAttribute("password2", password2);
        } else {
            Date now = new Date();
            String avatarName = UUID.randomUUID().toString();
            // int avatarName=1+(int)(Math.random()*14);
            identicon.generator(String.valueOf(avatarName));
            user = new User();
            user.setUsername(username);
            user.setPassword(new BCryptPasswordEncoder().encode(password));
            user.setInTime(now);
            user.setAvatar(siteConfig.getBaseUrl() + "/static/images/avatar/" + String.valueOf(avatarName) + ".png");
            userService.save(user);
            return redirect(response, "/login?s=reg");
        }
        return render("/register");
    }

    /**
     * 上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload")
    @ResponseBody
    public Result upload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                String requestUrl = fileUtil.uploadFile(file, FileUploadEnum.FILE);
                return Result.success(requestUrl);
            } catch (IOException e) {
                e.printStackTrace();
                return Result.error("上传失败");
            }
        }
        return Result.error("文件不存在");
    }

    /**
     * wangEditor上传
     *
     * @param file
     * @return
     */
    @PostMapping("/wangEditorUpload")
    @ResponseBody
    public String wangEditorUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                return fileUtil.uploadFile(file, FileUploadEnum.FILE);
            } catch (IOException e) {
                e.printStackTrace();
                return "error|服务器端错误";
            }
        }
        return "error|文件不存在";
    }
    
    @GetMapping("/agree")
    public String agree(Model model, HttpServletResponse response) {
//        if (getUser() != null) {
//            return redirect(response, "/");
//        }
        // model.addAttribute("s", s);
        return render("/agree");
    }

}
