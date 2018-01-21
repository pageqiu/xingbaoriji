package cn.pageqiu.common;

import org.springframework.beans.factory.annotation.Value;

/**
 * Created by tomoya.
 * Copyright (c) 2016, All Rights Reserved.
 * http://tomoya.cn
 */
//@Component
//@Setter @Getter
public class BaseProperties {

    @Value("${cn.pageqiu.bbs.siteName}")
    private String siteName;

    @Value("${cn.pageqiu.bbs.cookie.name}")
    private String cookieName;

    @Value("${cn.pageqiu.bbs.cookie.domain}")
    private String cookieDomain;

    @Value("${cn.pageqiu.bbs.session.name}")
    private String sessionName;

}
