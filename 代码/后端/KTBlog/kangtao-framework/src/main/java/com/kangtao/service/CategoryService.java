package com.kangtao.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.CategoryDto;
import com.kangtao.domain.entity.Category;
import com.kangtao.domain.vo.CategoryVo;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-02-20 16:43:22
 */
public interface CategoryService extends IService<Category> {
    ResponseResult getCategoryList();

    ResponseResult listAllCategory();

    ResponseResult listCategoryWithPage(Integer pageNum, Integer pageSize, String name, String status);

    ResponseResult addCategory(CategoryDto category);

    ResponseResult getCategoryById(Long id);

    ResponseResult updateCategory(CategoryVo categoryVo);

    ResponseResult deleteCategoryById(Long id);
}
