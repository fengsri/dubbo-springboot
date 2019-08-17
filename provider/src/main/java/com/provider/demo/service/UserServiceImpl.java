package com.provider.demo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.dubbo.service.UserService;
import com.feng.model.User;


@Service
public class UserServiceImpl implements UserService {

    @Override
    public User login(String username, String password) {
        User user = new User();
        user.setPassword("12345");
        user.setUsername("fengwen");
        user.setId(10);
        return user;
    }
}
