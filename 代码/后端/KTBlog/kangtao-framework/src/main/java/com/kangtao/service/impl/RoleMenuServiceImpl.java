package com.kangtao.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.domain.entity.RoleMenu;
import com.kangtao.mapper.RoleMenuMapper;
import com.kangtao.service.RoleMenuService;
import org.springframework.stereotype.Service;
/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2024-03-04 22:26:43
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
