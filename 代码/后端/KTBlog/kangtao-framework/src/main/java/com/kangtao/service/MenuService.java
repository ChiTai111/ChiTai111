package com.kangtao.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.Menu;

import java.util.List;

/**
 * 菜单权限表(Menu)表服务接口
 *
 * @author makejava
 * @since 2024-03-02 14:56:09
 */
public interface MenuService extends IService<Menu> {
    List<String> selectPermsByUserId(Long id);



    List<Menu> selectRouterMenuTreeByUserId(Long userId);



    ResponseResult getMenus(String status, String menuName);

    ResponseResult addMenu(Menu menu);


    ResponseResult getMenuById(Long id);

    ResponseResult updateMenuById(Menu menu);

    ResponseResult deleteMenu(Long id);

    ResponseResult getMenuTree();

    ResponseResult getRoleMenuTreeselect(Long id);
}
