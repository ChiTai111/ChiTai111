package com.kangtao.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.constants.SystemConstants;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.CategoryDto;
import com.kangtao.domain.entity.Article;
import com.kangtao.domain.entity.Category;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.vo.AdminCategory;
import com.kangtao.domain.vo.CategoryVo;
import com.kangtao.domain.vo.PageVo;
import com.kangtao.mapper.CategoryMapper;
import com.kangtao.service.ArticleService;
import com.kangtao.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-02-20 17:13:03
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Override
    public ResponseResult getCategoryList() {
        //查询文章表 状态为已发布的文章
        LambdaQueryWrapper<Article> articleWrapper = new LambdaQueryWrapper<>();
        articleWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(articleWrapper);
        //获取文章的分类id，并且去重
        Set<Long> categoryIds= articleList.stream()
                .map(article -> article.getCategoryId())
                .collect(Collectors.toSet());
        //查询分类表
        List<Category> categories = listByIds(categoryIds);
        List<Category> categoryList = categories.stream()
                .filter(category -> category.getStatus().equals(SystemConstants.STATUS_NORMAL))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public ResponseResult listCategoryWithPage(Integer pageNum, Integer pageSize, String name, String status) {
/*        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        queryWrapper.select(Category::getName,Category::getDescription,Category::getStatus,Category::getId);
        Page<Category> page = new Page<>(pageSize,pageNum);
        page(page,queryWrapper);
        List<Category> records = page.getRecords();
        List<AdminCategory> adminCategories = BeanCopyUtils.copyBeanList(records, AdminCategory.class);*/


       // List<Category> list = list();
        //TODO 做不了,分页查询有bug
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name),Category::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Category::getStatus,status);
        List<Category> list = list(queryWrapper);
        List<AdminCategory> adminCategories = BeanCopyUtils.copyBeanList(list, AdminCategory.class);
        return ResponseResult.okResult(new PageVo(adminCategories, (long)adminCategories.size()));
    }

    @Override
    public ResponseResult addCategory(CategoryDto category) {
        Category category1 = BeanCopyUtils.copyBean(category, Category.class);
        save(category1);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getCategoryById(Long id) {
        Category category = getById(id);
        CategoryVo categoryVo = BeanCopyUtils.copyBean(category, CategoryVo.class);
        return ResponseResult.okResult(categoryVo);
    }

    @Override
    public ResponseResult updateCategory(CategoryVo categoryVo) {
        Category category = BeanCopyUtils.copyBean(categoryVo, Category.class);
        updateById(category);

        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategoryById(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
