package com.kangtao.service.impl;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.LoginUser;
import com.kangtao.domain.entity.User;
import com.kangtao.domain.utils.JwtUtil;
import com.kangtao.domain.utils.RedisCache;
import com.kangtao.domain.utils.SecurityUtils;
import com.kangtao.service.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {
    @Autowired
    private AuthenticationConfiguration auth;

   /* @Autowired*/
    private AuthenticationManager authenticationManager ;




    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user) {
        try {
            authenticationManager = auth.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        Authentication authenticate =
                authenticationManager.authenticate(authenticationToken);
//判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
//获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
//把用户信息存入redis
        redisCache.setCacheObject("login:"+userId,loginUser);
//把token封装 返回
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        return ResponseResult.okResult(map);
    }

    @Override
    public ResponseResult logout() {
//获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
//删除redis中对应的值
        redisCache.deleteObject("login:"+userId);
        return ResponseResult.okResult();
    }
}