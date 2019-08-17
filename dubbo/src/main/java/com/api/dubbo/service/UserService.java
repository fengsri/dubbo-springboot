package com.api.dubbo.service;

import com.feng.model.User;

public interface UserService {

    User login(String username, String password);
}
