package com.kangtao.controller;


import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.UserAdminDto;
import com.kangtao.domain.dto.UserUpdateDto;
import com.kangtao.domain.vo.UserAdminVo;
import com.kangtao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/user")
public class UserController {
    @Autowired
   private UserService userService;

    @GetMapping("/list")
    public ResponseResult getUsersByUserNameAndPhonenum(Integer pageNum,Integer pageSize,String userName,
                                                        String phonenumber, String status){
        return  userService.getUsersByUserNameAndPhonenum(pageNum,pageSize,userName,phonenumber,status);
    }

    @PostMapping
    public ResponseResult addUser(@RequestBody UserAdminDto userAdminDto){
        return userService.addUser(userAdminDto);

    }

    @GetMapping("/{id}")
    public  ResponseResult getUserById(@PathVariable Long id){
        return userService.getUserById(id);
    }


    @PutMapping
    public  ResponseResult updateUserById(@RequestBody UserUpdateDto user){
        return userService.updateUserById(user);
    }

}
