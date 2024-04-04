package com.kangtao.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.constants.SystemConstants;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.Menu;
import com.kangtao.domain.entity.RoleMenu;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.utils.SecurityUtils;
import com.kangtao.domain.vo.MenuIdsAndMenusVo;
import com.kangtao.domain.vo.MenuTreeVo;
import com.kangtao.domain.vo.MenuVo;
import com.kangtao.mapper.MenuMapper;
import com.kangtao.service.MenuService;
import com.kangtao.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-03-02 14:56:09
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public List<String> selectPermsByUserId(Long id) {
//如果是管理员，返回所有的权限
        if(id == 1L){
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType,SystemConstants.MENU,SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> menus = list(wrapper);
            List<String> perms = menus.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
       //否则返回所具有的权限
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
//判断是否是管理员
        if(SecurityUtils.isAdmin()){
//如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
//否则 获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }
//构建tree
//先找出第一层的菜单 然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public ResponseResult getMenus(String status, String menuName) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
        queryWrapper.like(StringUtils.hasText(menuName),Menu::getMenuName,menuName);
        queryWrapper.eq(StringUtils.hasText(status),Menu::getStatus,status);
        List<Menu> menus = list(queryWrapper);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    @Override
    public ResponseResult addMenu(Menu menu) {
        save(menu);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getMenuById(Long id) {
        Menu menu = getById(id);

        return ResponseResult.okResult(menu);
    }

    @Override
    public ResponseResult updateMenuById(Menu menu) {
        if(menu.getParentId() == menu.getId()){
            return ResponseResult.errorResult(500,"修改菜单'写博文'失败，上级菜单不能选择自己");
        }

        updateById(menu);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteMenu(Long id) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Menu::getParentId,id);
        if(  count(queryWrapper)>0 ){
            return ResponseResult.errorResult(500,"存在子菜单不允许删除");
        }

        removeById(id);
        return ResponseResult.okResult();
    }

/*    @Override
    public ResponseResult getMenuTree() {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Menu::getParentId,SystemConstants.MENU_PARENT_ID);

        List<Menu> tmp = list(queryWrapper);



        List<Menu> menus = list();


        List<Menu> collect = tmp.
                stream()
                .map(menu -> menu.setChildren(builderMenuTree(menus, menu.getId())))
                .collect(Collectors.toList());
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(collect, MenuTreeVo.class);
        List<MenuTreeVo> collect1 = menuTreeVos.stream()
                // .filter(menuTreeVo -> menuTreeVo.setLabel(getById(menuTreeVo.getId()).getMenuName()))
                .filter(menuTreeVo -> {
                    String label = getById(menuTreeVo.getId()).getMenuName();
                    menuTreeVo.setLabel(label);
                    return true;
                })
                .collect(Collectors.toList());
        return ResponseResult.okResult(collect1);
    }*/
    @Override
    public ResponseResult getMenuTree() {
        List<MenuTreeVo> menusTree = getMenusTree();


        return ResponseResult.okResult(menusTree);

    }

    private List<MenuTreeVo> getMenusTree(){
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Menu::getParentId,SystemConstants.MENU_PARENT_ID);
        List<Menu> parent_tmp = list(queryWrapper);
        List<MenuTreeVo> parents = BeanCopyUtils.copyBeanList(parent_tmp, MenuTreeVo.class);
        List<MenuTreeVo> parentstree = parents.stream().map(menuTreeVo -> {
            String menuName = getById(menuTreeVo.getId()).getMenuName();
            return menuTreeVo.setLabel(menuName);
        }).collect(Collectors.toList());

        List<Menu> menus = list();
        List<MenuTreeVo> menuTreeVos = BeanCopyUtils.copyBeanList(menus, MenuTreeVo.class);

        List<MenuTreeVo> childrens = menuTreeVos.stream()
                .map(menuTreeVo -> menuTreeVo.setLabel(getById(menuTreeVo.getId()).getMenuName()))
                .collect(Collectors.toList());

        for (MenuTreeVo menuTree : parentstree ){
            List<MenuTreeVo> ls = new ArrayList<>();
            for(MenuTreeVo menu : childrens){
                System.out.println(menu.getParentId());
                if(menu.getParentId().equals(menuTree.getId()) ){
                    ls.add(menu);
                }
            }
            menuTree.setChildren(new ArrayList<>(ls));
            ls.clear();

        }

        for(MenuTreeVo menuTreeVo: parentstree){
            for(MenuTreeVo menuTreeVo1 : menuTreeVo.getChildren()){
                List<MenuTreeVo> ls = new ArrayList<>();
                for(MenuTreeVo menu : childrens){

                    if(menu.getParentId().equals(menuTreeVo1.getId()) ){
                        ls.add(menu);
                    }
                }
                menuTreeVo1.setChildren(new ArrayList<>(ls));
                ls.clear();
            }
        }

        return parentstree;
    }

    @Autowired
    private RoleMenuService roleMenuService;
    @Override
    public ResponseResult getRoleMenuTreeselect(Long id) {
        List<MenuTreeVo> menusTree = getMenusTree();
        List<Long> menuIds = new ArrayList<>();
        if( id.equals(1L)){
            menuIds = roleMenuService.list()
                    .stream()
                    .distinct()
                    .map(roleMenu -> roleMenu.getMenuId())
                    .collect(Collectors.toList());
        }else {
            LambdaQueryWrapper<RoleMenu>queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(RoleMenu::getRoleId,id);
            List<RoleMenu> list = roleMenuService.list(queryWrapper);
            menuIds = list.stream().map(roleMenu -> roleMenu.getMenuId()).collect(Collectors.toList());
        }

        return ResponseResult.okResult(new MenuIdsAndMenusVo(menusTree,menuIds));
    }


    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }


    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
