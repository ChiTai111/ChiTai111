package com.kangtao.controller;


import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.TagListDto;
import com.kangtao.domain.entity.Tag;
import com.kangtao.domain.vo.PageVo;
import com.kangtao.domain.vo.TagVo;
import com.kangtao.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content")
public class TagController {
    @Autowired
    private TagService tagService;
/*
    @GetMapping("/list")
    public ResponseResult list(){
        return ResponseResult.okResult(tagService.list());
    }
*/

    @GetMapping("/tag/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }

    @PostMapping("/tag")
    public ResponseResult insertTag(@RequestBody Tag tag){
        return tagService.insert(tag);
    }

    @DeleteMapping("/tag/{id}")
    public ResponseResult deleteById(@PathVariable Long id){
        return tagService.deleteById(id);
    }

    @GetMapping("/tag/{id}")
    public ResponseResult getTagById(@PathVariable Long id){
        return tagService.getTagById(id);
    }

    @PutMapping("/tag")
    public ResponseResult updateTagById(@RequestBody Tag tag){
        return tagService.updateTagById(tag);
    }

    @GetMapping("/tag/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
}