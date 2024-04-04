package com.kangtao.controller;

import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.AddArticleDto;
import com.kangtao.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/content/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;
    @PostMapping
    public ResponseResult add(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }


    @GetMapping("/list")
    public ResponseResult selectByTitleAndSummary(Integer pageNum, Integer pageSize,
                                                 String title,String summary){
        return articleService.selectByTitleAndSummary(pageNum,pageSize,title,summary);
    }

    @GetMapping("/{id}")
    public  ResponseResult selectArticleById(@PathVariable Long id){
        return articleService.selectArticleById(id);
    }

    @PutMapping
    public ResponseResult updateArticle(@RequestBody AddArticleDto articleDto){
        return articleService.updateArticle(articleDto);
    }


    @DeleteMapping("/{id}")
    public  ResponseResult deleteArticleById(@PathVariable Long id){
        return articleService.deleteArticleById(id);
    }


}