package com.kangtao.service.impl;

import com.kangtao.domain.utils.JwtUtil;
import io.jsonwebtoken.Claims;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceImplTest {


    public static void main(String[] args) throws Exception {
        String s = "$2a$10$Jnq31rRkNV3RNzXe0REsEOSKaYK8UgVZZqlNlNXqn.JeVcj2NdeZy";
        Claims claims = JwtUtil.parseJWT(s);
        System.out.println(claims);
    }
}