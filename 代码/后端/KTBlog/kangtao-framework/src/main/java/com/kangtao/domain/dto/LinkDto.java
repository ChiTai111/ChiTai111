package com.kangtao.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkDto {
    private String name;
    private String logo;
    private String description;
    //网站地址
    private String address;

    private String status;

}
