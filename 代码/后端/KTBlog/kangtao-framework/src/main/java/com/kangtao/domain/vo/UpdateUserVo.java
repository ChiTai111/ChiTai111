package com.kangtao.domain.vo;

import com.kangtao.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserVo {


    private List<Long> roleIds;
    private List<Role> roles;
    private UserUpdateVo user;
}
