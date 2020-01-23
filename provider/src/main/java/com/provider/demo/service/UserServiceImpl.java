package com.provider.demo.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.api.dubbo.service.UserService;
import com.feng.model.User;
import com.provider.demo.comment.DistributedLock;
import com.provider.demo.comment.RedisLock;
import org.springframework.beans.factory.annotation.Autowired;


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
     * 基于redis做的分布式锁
     */
    @Autowired
    private RedisLock redisLock;

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
     * 减少用户拥有数量，这个地方采用的是curator做的分布式锁
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


    /**
     * 减少用户拥有数量，这个地方采用的是redis做的分布式锁
     * @param count
     * @param id
     * @return
     */
    @Override
    public String reduce(String id,int count) {
        //加锁
        long time = System.currentTimeMillis() + 3000 ;
        if(!redisLock.lock(id,String.valueOf(time))){ //如果返回
            return  "并发量太多了，换个姿势再试试！";
        }

        if (allCount<count){
            redisLock.unlock(id,String.valueOf(time));
            return "数量不足";
        }

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        allCount = allCount -count;

        redisLock.unlock(id,String.valueOf(time));
        return "减少成功";
    }
}
