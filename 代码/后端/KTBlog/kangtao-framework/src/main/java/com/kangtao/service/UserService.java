package com.kangtao.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.UserAdminDto;
import com.kangtao.domain.dto.UserUpdateDto;
import com.kangtao.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-02-22 15:36:12
 */
public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult getUsersByUserNameAndPhonenum(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status);

    ResponseResult addUser(UserAdminDto userAdminDto);

    ResponseResult deleteUserById(Long id);


    ResponseResult getUserById(Long id);

    ResponseResult updateUserById(UserUpdateDto user);

}
