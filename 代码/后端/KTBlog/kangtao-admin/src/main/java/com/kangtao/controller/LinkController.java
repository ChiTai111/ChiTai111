package com.kangtao.controller;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.LinkDto;
import com.kangtao.domain.vo.LinkVo;
import com.kangtao.service.LinkService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;
    @GetMapping("/list")
    public ResponseResult getLinkWithPage(Integer pageNum,Integer pageSize,String name,String status){
        return linkService.getLinkWithPage(pageNum,pageSize,name,status);
    }

    @PostMapping
    public  ResponseResult addLink(@RequestBody LinkDto linkDto){
        return linkService.addLink(linkDto);
    }

    @GetMapping("/{id}")
    public ResponseResult getLinkById(@PathVariable Integer id){
        return linkService.getLinkById(id);
    }

    @PutMapping
    public  ResponseResult updateLink(@RequestBody LinkVo linkvo){
        return linkService.updateLink(linkvo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteLink(@PathVariable Long id){
        return linkService.deleteLink(id);
    }
}
