package com.provider.demo.controller;

import com.api.dubbo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description
 * @Author fengwen
 * @Date 2020/1/22 17:34
 * @Version V1.0
 */
@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 减少数量，测试分布式锁
     * @param count
     * @return
     */
    @GetMapping("reduce")
    public String reduce(@RequestParam("count") int count){
        String result = userService.reduce(count);
        return result;
    }

    /**
     * 减少数量，测试分布式锁
     * @param count
     * @return
     */
    @GetMapping("reduce2")
    public String reduce2(@RequestParam("count") int count){
        String result = userService.reduce(count);
        return result;
    }
}
