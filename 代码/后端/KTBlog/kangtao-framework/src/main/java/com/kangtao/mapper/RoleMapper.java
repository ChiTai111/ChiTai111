package com.kangtao.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kangtao.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-03-02 20:41:43
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long id);
}
