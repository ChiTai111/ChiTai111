package com.kangtao.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.TagListDto;
import com.kangtao.domain.entity.Tag;
import com.kangtao.domain.vo.PageVo;
import com.kangtao.domain.vo.TagVo;

import java.util.List;

/**
 * 标签(Tag)表服务接口
 *
 * @author makejava
 * @since 2024-03-01 14:54:21
 */
public interface TagService extends IService<Tag> {
    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult insert(Tag tag);

    ResponseResult deleteById(Long id);

    ResponseResult getTagById(Long id);

    ResponseResult updateTagById(Tag tag);

    List<TagVo> listAllTag();

}
