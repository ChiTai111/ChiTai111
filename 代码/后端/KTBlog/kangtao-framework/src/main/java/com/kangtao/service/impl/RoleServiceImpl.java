package com.kangtao.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.constants.SystemConstants;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.RoleDto;
import com.kangtao.domain.dto.RoleMenuDto;
import com.kangtao.domain.dto.RoleUpdateDto;
import com.kangtao.domain.entity.Role;
import com.kangtao.domain.entity.RoleMenu;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.vo.PageVo;
import com.kangtao.domain.vo.RoleAdminVo;
import com.kangtao.domain.vo.RoleVo;
import com.kangtao.mapper.RoleMapper;
import com.kangtao.service.RoleMenuService;
import com.kangtao.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-03-02 20:41:43
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
//判断是否是管理员 如果是返回集合中只需要有admin
        if(id == 1L){
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
//否则查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult getRoleByNameAndStatus(Integer pageNum, Integer pageSize, String status, String roleName) {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(StringUtils.hasText(status),Role::getStatus,status);
        queryWrapper.like(StringUtils.hasText(roleName),Role::getRoleName,roleName);
        queryWrapper.orderByAsc(Role::getRoleSort);
        Page<Role> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Role> records = page.getRecords();
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(records, RoleVo.class);
        PageVo pageVo = new PageVo(roleVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult changeStatus(RoleDto roleDto) {

        Role role = getById(roleDto.getRoleId());
        role.setStatus(roleDto.getStatus());
        save(role);

        return ResponseResult.okResult();
    }


    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    @Transactional
    public ResponseResult addRole(RoleMenuDto roleMenuDto) {
        Role role = BeanCopyUtils.copyBean(roleMenuDto, Role.class);
        save(role);
        List<Long> menuIds = roleMenuDto.getMenuIds();
        List<RoleMenu> collect = menuIds.stream()
                .map(menuId -> new RoleMenu(role.getId(), menuId))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(collect);
        return ResponseResult.okResult();
    }


    @Override
    public ResponseResult updateRole(RoleUpdateDto roleUpdateDto) {
        Role role = BeanCopyUtils.copyBean(roleUpdateDto, Role.class);
        save(role);
        removeById(role.getId());
        List<Long> menuIds = roleUpdateDto.getMenuIds();
        List<RoleMenu> collect = menuIds.stream()
                .map(id -> new RoleMenu(role.getId(), id))
                .collect(Collectors.toList());
        roleMenuService.saveBatch(collect);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getRoleDto(Long id) {
        Role role = getById(id);
        LambdaQueryWrapper<RoleMenu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RoleMenu::getRoleId,id);
        List<Long> collect = roleMenuService.list(queryWrapper)
                .stream()
                .map(roleMenu -> roleMenu.getMenuId())
                .collect(Collectors.toList());
        RoleUpdateDto roleUpdateDto = BeanCopyUtils.copyBean(role, RoleUpdateDto.class);
        roleUpdateDto.setMenuIds(collect);
        return ResponseResult.okResult(roleUpdateDto);
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult listAllRole() {
        LambdaQueryWrapper<Role> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Role::getStatus, SystemConstants.STATUS_NORMAL);
        List<Role> list = list(queryWrapper);
        List<RoleAdminVo> roleAdminVos = BeanCopyUtils.copyBeanList(list, RoleAdminVo.class);
        return ResponseResult.okResult(roleAdminVos);
    }
}
