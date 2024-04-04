package com.kangtao.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.AddArticleDto;
import com.kangtao.domain.entity.Article;


public interface  ArticleService extends IService<Article> {
    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult selectByTitleAndSummary(Integer pageNum, Integer pageSize, String title, String summary);

    ResponseResult selectArticleById(Long id);

    ResponseResult updateArticle(AddArticleDto articleDto);

    ResponseResult deleteArticleById(Long id);
}
