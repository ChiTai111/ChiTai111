package com.kangtao.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.builder.ExcelWriterBuilder;
import com.alibaba.fastjson.JSON;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.CategoryDto;
import com.kangtao.domain.entity.Category;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.utils.WebUtils;
import com.kangtao.domain.vo.CategoryVo;
import com.kangtao.domain.vo.ExcelCategoryVo;
import com.kangtao.enums.AppHttpCodeEnum;
import com.kangtao.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory(){
        return categoryService.listAllCategory();
    }

    @GetMapping("/list")
    public ResponseResult listCategoryWithPage(Integer pageNum,Integer pageSize,String name,String status){

        return categoryService.listCategoryWithPage(pageNum,pageSize,name,status);
    }

    @PostMapping
    public  ResponseResult addCategory(@RequestBody CategoryDto category){
        return categoryService.addCategory(category);
    }

    @GetMapping("/{id}")
    public  ResponseResult getCategoryById(@PathVariable Long id){
        return categoryService.getCategoryById(id);
    }

    @PutMapping
    public  ResponseResult updateCategory(@RequestBody CategoryVo categoryVo){
        return categoryService.updateCategory(categoryVo);
    }

    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable Long id){
        return categoryService.deleteCategoryById(id);
    }





    @PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("/export")
    public void export(HttpServletResponse response){
        try {
//设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
//获取需要导出的数据
            List<Category> categoryVos = categoryService.list();
            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
//把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(),
                            ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);



        } catch (Exception e) {
//如果出现异常也要响应json
            ResponseResult result =
                    ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }

    }

}