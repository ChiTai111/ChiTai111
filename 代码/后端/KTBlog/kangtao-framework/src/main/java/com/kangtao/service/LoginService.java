package com.kangtao.service;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.User;

public interface LoginService {
ResponseResult login(User user);

    ResponseResult logout();

}