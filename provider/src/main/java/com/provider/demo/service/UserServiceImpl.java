package com.provider.demo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.dubbo.service.UserService;
import com.feng.model.User;
import com.provider.demo.comment.DistributedLock;


/**
 * 用户service实现类
 * @author fengwen
 */
@org.springframework.stereotype.Service
@Service
public class UserServiceImpl implements UserService {

    /**
     * 用于测试分布式锁
     */
    public static int allCount=10;

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    @Override
    public User login(String username, String password) {
        User user = new User();
        user.setPassword("12345");
        user.setUsername("fengwen");
        user.setId(10);
        return user;
    }

    /**
     * 减少用户拥有数量
     * @param count
     * @return
     */
    @Override
    public String reduce(int count) {
        DistributedLock lock = new DistributedLock("reduce");
        lock.acquireLock();
        if (allCount<count){
            lock.releaseLock();
            return "数量不足";
        }
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allCount = allCount -count;

        lock.releaseLock();
        return "减少成功";
    }


}
