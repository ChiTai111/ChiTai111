package com.kangtao.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.constants.SystemConstants;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.AddArticleDto;
import com.kangtao.domain.entity.Article;
import com.kangtao.domain.entity.ArticleTag;
import com.kangtao.domain.entity.Category;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.utils.RedisCache;
import com.kangtao.domain.vo.ArticleDetailVo;
import com.kangtao.domain.vo.ArticleListVo;
import com.kangtao.domain.vo.HotArticleVo;
import com.kangtao.domain.vo.PageVo;
import com.kangtao.mapper.ArticleMapper;
import com.kangtao.mapper.ArticleTagMapper;
import com.kangtao.service.ArticleService;
import com.kangtao.service.ArticleTagService;
import com.kangtao.service.CategoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;




@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article>
        implements ArticleService {
    @Override
    public ResponseResult hotArticleList() {
//查询热门文章 封装成ResponseResult返回
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
//必须是正式文章
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
//按照浏览量进行排序
        queryWrapper.orderByDesc(Article::getViewCount);
//最多只查询10条
        Page<Article> page = new Page(1,10);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();


        //bean拷贝
/*        List<HotArticleVo> articleVos = new ArrayList<>();
        for (Article article : articles) {
            HotArticleVo vo = new HotArticleVo();
            BeanUtils.copyProperties(article,vo);
            articleVos.add(vo);
        }*/
        List<HotArticleVo> articleList = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);
        return ResponseResult.okResult(articleList);
    }
    @Autowired
    private CategoryService categoryService;
    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {


        //查询条件
        LambdaQueryWrapper<Article> queryWrapper=new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        queryWrapper.eq(Objects.nonNull(categoryId)&&categoryId>1,Article::getCategoryId,categoryId);
        // 状态是正式发布的
        queryWrapper.eq(Article::getStatus,SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        queryWrapper.orderByDesc(Article::getIsTop);
        //分页查询
        Page<Article> page = new Page<>(pageNum,pageSize);
        page(page,queryWrapper);

        //查询categoryName
        List<Article> articles = page.getRecords();
        //articleId去查询articleName进行设置
        articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
                .collect(Collectors.toList());


        //封装查询结果
        List<ArticleListVo> articleList = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);
        PageVo pageVo = new PageVo(articleList,page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);
        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount",
                id.toString());
        article.setViewCount(viewCount.longValue());
        //转换成VO
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null){
            articleDetailVo.setCategoryName(category.getName());
        }
        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Autowired
     private RedisCache redisCache;
    @Override
    public ResponseResult updateViewCount(Long id) {
//更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }

    @Autowired
    private ArticleTagService articleTagService;
    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {

//添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);

        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());
//添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult selectByTitleAndSummary(Integer pageNum, Integer pageSize, String title, String summary) {
        LambdaQueryWrapper<Article> queryWrapper= new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.hasText(title),Article::getTitle,title);
        queryWrapper.like(StringUtils.hasText(summary),Article::getSummary,summary);
        Page<Article> page =new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<Article> articles = page.getRecords();
        PageVo pageVo = new PageVo(articles,page.getTotal());
        return ResponseResult.okResult(pageVo);
    }


    @Override
    @Transactional
    public ResponseResult selectArticleById(Long id) {
        Article article =  getById(id);

        AddArticleDto articleDto = BeanCopyUtils.copyBean(article, AddArticleDto.class);
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagService.getBaseMapper().selectList(queryWrapper);
        List<Long> collect = articleTags.stream()
                .map(articleTag -> articleTag.getTagId())
                .collect(Collectors.toList());
        articleDto.setTags(collect);
        return ResponseResult.okResult(articleDto);
    }

    @Autowired
    private   ArticleMapper articleMapper;

    @Override
    @Transactional
    public ResponseResult updateArticle(AddArticleDto articleDto) {
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        List<ArticleTag> collect = articleDto.getTags().stream()
                .map(tagid -> new ArticleTag(article.getId(), tagid))
                .collect(Collectors.toList());
        articleMapper.updateById(article);
        //先删后加
        LambdaQueryWrapper<ArticleTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ArticleTag::getArticleId,article.getId());

        articleTagService.remove(queryWrapper);
        articleTagService.saveBatch(collect);


        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteArticleById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }


}