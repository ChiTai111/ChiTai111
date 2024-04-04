package com.kangtao.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminCategory {
    private Long id;
    private String name;
    private String status;
    //描述
    private String description;
}
