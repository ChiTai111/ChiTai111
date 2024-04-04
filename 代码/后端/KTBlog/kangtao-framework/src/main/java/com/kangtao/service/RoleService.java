package com.kangtao.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.RoleDto;
import com.kangtao.domain.dto.RoleMenuDto;
import com.kangtao.domain.dto.RoleUpdateDto;
import com.kangtao.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-03-02 20:41:43
 */
public interface RoleService extends IService<Role> {
    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult getRoleByNameAndStatus(Integer pageNum, Integer pageSize, String status, String roleName);

    ResponseResult changeStatus(RoleDto roleDto);

    ResponseResult addRole(RoleMenuDto roleMenuDto);

    ResponseResult updateRole(RoleUpdateDto roleUpdateDto);

    ResponseResult getRoleDto(Long id);

    ResponseResult deleteRoleById(Long id);

    ResponseResult listAllRole();

}
