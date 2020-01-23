package com.consumer.demo.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.api.dubbo.service.UserService;
import com.feng.model.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 消费者
 * @author fengwen
 */
@RestController
@RequestMapping(value = "user")
public class UserController {

    @Reference
    private UserService userService;


    @RequestMapping("login")
    public User login(String username,String password) {
        User user = userService.login(username,password);
        return user;
    }
}
