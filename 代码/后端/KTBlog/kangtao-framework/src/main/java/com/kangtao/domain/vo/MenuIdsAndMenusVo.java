package com.kangtao.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuIdsAndMenusVo {
    List<MenuTreeVo> menus;
    List<Long> checkedKeys;
}
