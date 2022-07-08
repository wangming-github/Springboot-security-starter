package com.zimug.courses.security.basic.controller;

import com.zimug.courses.security.basic.config.service.MethodELTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BizpageController {


    @Autowired
    private MethodELTestService methodELDemo;

    // 登录
    @PostMapping("/login")
    public String index(String username, String password) {
        return "index";
    }

    // 日志管理
    @GetMapping("/syslog")
    public String showOrder() {
        return "syslog";
    }

    // 用户管理
    @GetMapping("/sysuser")
    public String addOrder() {
        return "sysuser";
    }

    // 具体业务一
    @GetMapping("/biz1")
    public String updateOrder() {
        //methodELDemo.findAll();
        //methodELDemo.findOne();
        //List<Integer> ids = new ArrayList<>();
        //ids.add(1);
        //ids.add(2);
        //methodELDemo.delete(ids, null);
        //List<User> pds = methodELDemo.findAllPD();
        return "biz1";
    }

    // 具体业务二
    @GetMapping("/biz2")
    public String deleteOrder() {
        return "biz2";
    }


}