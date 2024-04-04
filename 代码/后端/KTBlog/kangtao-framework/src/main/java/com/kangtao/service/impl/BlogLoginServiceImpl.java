package com.kangtao.service.impl;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.LoginUser;
import com.kangtao.domain.entity.User;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.utils.JwtUtil;
import com.kangtao.domain.utils.RedisCache;
import com.kangtao.domain.vo.BlogUserLoginVo;
import com.kangtao.domain.vo.UserInfoVo;
import com.kangtao.service.BlogLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class BlogLoginServiceImpl implements BlogLoginService {
    @Autowired
    private AuthenticationConfiguration auth;


  /*  @Autowired
    private AuthenticationManager authenticationManager;*/

    @Autowired
    private RedisCache redisCache;
    @Override
    public ResponseResult login(User user)  {
        UsernamePasswordAuthenticationToken authenticationToken = new
                UsernamePasswordAuthenticationToken(user.getUserName(),user.getPassword());
        AuthenticationManager authenticationManager;
        try {
            authenticationManager = auth.getAuthenticationManager(); //大坑
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
         Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //判断是否认证通过
        if(Objects.isNull(authenticate)){
            throw new RuntimeException("用户名或密码错误");
        }
         //获取userid 生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId().toString();
        String jwt = JwtUtil.createJWT(userId);
         //把用户信息存入redis
        redisCache.setCacheObject("bloglogin:"+userId,loginUser);
         //把token和userinfo封装 返回
          //把User转换成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(loginUser.getUser(), UserInfoVo.class);
        BlogUserLoginVo vo = new BlogUserLoginVo(jwt,userInfoVo);
        return ResponseResult.okResult(vo);

    }

    @Override
    public ResponseResult logout() {
        //获取token 解析获取userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        //获取userid
        Long userId = loginUser.getUser().getId();
        //删除redis中的用户信息
        redisCache.deleteObject("bloglogin:"+userId);
        return ResponseResult.okResult();
    }
}
