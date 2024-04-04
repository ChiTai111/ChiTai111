package com.kangtao.controller;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.RoleDto;
import com.kangtao.domain.dto.RoleMenuDto;
import com.kangtao.domain.dto.RoleUpdateDto;
import com.kangtao.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/role")
public class RoleController {

     @Autowired
     private RoleService roleService;


     @GetMapping("/list")
    public ResponseResult getRoleByNameAndStatus(Integer pageNum, Integer pageSize, String status,String roleName){
        return  roleService.getRoleByNameAndStatus(pageNum,pageSize,status,roleName);

    }

    @PutMapping("/changeStatus")
    public ResponseResult changeStatus(@RequestBody RoleDto roleDto){
         return roleService.changeStatus(roleDto);
    }


    @PostMapping
    public  ResponseResult addRole(@RequestBody RoleMenuDto roleMenuDto){
         return  roleService.addRole(roleMenuDto);
    }

    @PutMapping
    public ResponseResult updateRole(@RequestBody RoleUpdateDto roleUpdateDto){
         return  roleService.updateRole(roleUpdateDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getRoleDto(@PathVariable Long id){
        return  roleService.getRoleDto(id);

    }

    @DeleteMapping("/{id}")

    public ResponseResult deleteRoleById(@PathVariable Long id){
         return roleService.deleteRoleById(id);
    }


    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        return  roleService.listAllRole();

    }






}
