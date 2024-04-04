package com.kangtao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.LinkDto;
import com.kangtao.domain.entity.Link;
import com.kangtao.domain.vo.LinkVo;

public interface LinkService extends IService<Link> {
    ResponseResult getAllLink();

    ResponseResult getLinkWithPage(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addLink(LinkDto linkDto);

    ResponseResult getLinkById(Integer id);

    ResponseResult updateLink(LinkVo linkvo);

    ResponseResult deleteLink(Long id);
}