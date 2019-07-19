package com.chryl.controller;

import com.alibaba.fastjson.JSONObject;
import com.chryl.annotation.PassToken;
import com.chryl.annotation.UserLoginToken;
import com.chryl.bean.User;
import com.chryl.mapper.UserMapper;
import com.chryl.token.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created By Chr on 2019/7/19.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserMapper userMapper;

    //登陆不需要 jwt验证
    @PassToken
    @PostMapping(value = "/login")
    public Object login(@RequestParam("username") String username,
                        @RequestParam("password") String password) {

        JSONObject jsonObject = new JSONObject();
        //模拟db查询
        User user = userMapper.selectByUserName(username);
        if (user == null) {
            jsonObject.put("msg", "login fail");
            return jsonObject;
        } else {
            if (!password.equals(user.getUserPassword())) {
                jsonObject.put("msg", "login fail");
                return jsonObject;
            } else {
                String token = tokenService.getToken(user);
                jsonObject.put("token", token);
                jsonObject.put("user", user);
                return jsonObject;
            }
        }
    }

    //查看信息,需要验证jwt
    @UserLoginToken
    @GetMapping(value = "/get/{id}")
    public Object getMsg(@PathVariable("id") String id) {
        return userMapper.selectByUserId(id);
    }


    //#####################
//    @PostMapping("/login")
//    public Object loginC(@RequestBody @Valid User user) {
//
//
//        return null;
//    }
}
