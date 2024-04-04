package com.kangtao.service;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}