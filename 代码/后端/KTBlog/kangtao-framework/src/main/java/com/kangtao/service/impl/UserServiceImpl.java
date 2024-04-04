package com.kangtao.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.UserAdminDto;
import com.kangtao.domain.dto.UserUpdateDto;
import com.kangtao.domain.entity.Role;
import com.kangtao.domain.entity.User;
import com.kangtao.domain.entity.UserRole;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.utils.SecurityUtils;
import com.kangtao.domain.vo.*;
import com.kangtao.enums.AppHttpCodeEnum;
import com.kangtao.exception.SystemException;
import com.kangtao.mapper.UserMapper;
import com.kangtao.service.RoleService;
import com.kangtao.service.UserRoleService;
import com.kangtao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-02-22 15:36:13
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public ResponseResult userInfo() {
//获取当前用户id
        Long userId = SecurityUtils.getUserId();
//根据用户id查询用户信息
        User user = getById(userId);
//封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user,UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }
    /*@Autowired*/
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if(!StringUtils.hasText(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getPassword())){
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getEmail())){
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if(!StringUtils.hasText(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
//对数据进行是否存在的判断
        if(userNameExist(user.getUserName())){
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if(nickNameExist(user.getNickName())){
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
//...
//对密码进行加密

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
//存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getUsersByUserNameAndPhonenum(Integer pageNum, Integer pageSize, String userName, String phonenumber, String status) {
        LambdaQueryWrapper<User>queryWrapper =new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(userName),User::getUserName,userName);
        queryWrapper.eq(StringUtils.hasText(phonenumber),User::getPhonenumber,phonenumber);
        queryWrapper.eq(StringUtils.hasText(status),User::getStatus,status);
        Page<User> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<User> records = page.getRecords();
        List<UserAdminVo> userAdminVos = BeanCopyUtils.copyBeanList(records, UserAdminVo.class);

        return ResponseResult.okResult(new PageVo(userAdminVos,page.getTotal()));
    }

    @Override
    public ResponseResult addUser(UserAdminDto userAdminDto) {

        User user = BeanCopyUtils.copyBean(userAdminDto, User.class);
        if(!StringUtils.hasText(user.getUserName())){
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(User::getUserName,user.getUserName());
        if(count(queryWrapper)>0){
            queryWrapper.clear();
            return ResponseResult.errorResult(AppHttpCodeEnum.USERNAME_EXIST);
        }

        queryWrapper.eq(User::getPhonenumber,user.getPhonenumber());
        if(count(queryWrapper)>0){
            queryWrapper.clear();
            return ResponseResult.errorResult(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        queryWrapper.eq(User::getEmail,user.getEmail());
        if(count(queryWrapper)>0){
            return ResponseResult.errorResult(AppHttpCodeEnum.EMAIL_EXIST);
        }

        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
//存入数据库
        save(user);
        return ResponseResult.okResult();


    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
    @Autowired
    private UserRoleService userRoleService;


    @Autowired
    private RoleService roleService;

    @Override
    public ResponseResult getUserById(Long id) {

        User user = getById(id);
        UserUpdateVo userUpdateVo = BeanCopyUtils.copyBean(user, UserUpdateVo.class);
        LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
        List<UserRole> list = userRoleService.list(queryWrapper.eq(UserRole::getUserId, id));
        List<Long> roleIds = list.stream().map(userRole -> userRole.getRoleId())
                .collect(Collectors.toList());
        List<Role> roles = roleService.list();

        return ResponseResult.okResult(new UpdateUserVo(roleIds, roles, userUpdateVo));
    }

    @Override
    @Transactional
    public ResponseResult updateUserById(UserUpdateDto user) {
        User user1 = BeanCopyUtils.copyBean(user, User.class);
        save(user1);
        List<Long> roleIds = user.getRoleIds();
      LambdaQueryWrapper<UserRole> queryWrapper = new LambdaQueryWrapper<>();
      queryWrapper.eq(UserRole::getUserId,user.getId());
      userRoleService.remove(queryWrapper);
      roleIds.stream()
              .forEach(id -> userRoleService.save(new UserRole(user.getId(),id)));
        return ResponseResult.okResult();
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName,nickName);


        return  count(queryWrapper)>0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName,userName);


        return  count(queryWrapper)>0;
    }


}
