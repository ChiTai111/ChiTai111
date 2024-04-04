package com.kangtao.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.domain.entity.UserRole;
import com.kangtao.mapper.UserRoleMapper;
import com.kangtao.service.UserRoleService;
import org.springframework.stereotype.Service;
/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2024-03-06 21:13:58
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
