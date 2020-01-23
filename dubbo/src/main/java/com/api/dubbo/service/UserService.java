package com.api.dubbo.service;

import com.feng.model.User;

/**
 * 用户service
 * @author fengwen
 */
public interface UserService {

    /**
     * 登录
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);

    /**
     * 减少用户拥有数量
     * @param count
     * @return
     */
    String reduce(int count);

}
