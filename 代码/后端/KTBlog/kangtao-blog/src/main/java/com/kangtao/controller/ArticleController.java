package com.kangtao.controller;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.Article;
import com.kangtao.service.ArticleService;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
/*    @GetMapping("/list")
    public List<Article> test(){
    return articleService.list();
    }*/
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList(){

        ResponseResult result= articleService.hotArticleList();
        return  result;
    }
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum,Integer pageSize, Long categoryId){
         return  articleService.articleList(pageNum,pageSize,categoryId);
    }

    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id){
        return articleService.getArticleDetail(id);
    }


    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id){
        return articleService.updateViewCount(id);
    }
}