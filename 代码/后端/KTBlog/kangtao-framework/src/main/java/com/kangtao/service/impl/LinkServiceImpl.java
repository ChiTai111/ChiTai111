package com.kangtao.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.constants.SystemConstants;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.dto.LinkDto;
import com.kangtao.domain.entity.Link;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.vo.LinkVo;
import com.kangtao.domain.vo.PageVo;
import com.kangtao.mapper.LinkMapper;
import com.kangtao.service.LinkService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 友链(Link)表服务实现类
 *
 * @author makejava
 * @since 2024-02-21 15:02:57
 */
@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> list = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(list, LinkVo.class);
        //封装返回

        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult getLinkWithPage(Integer pageNum, Integer pageSize, String name, String status) {
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.hasText(name),Link::getName,name);
        queryWrapper.eq(StringUtils.hasText(status),Link::getStatus,status);
      /*  Page<Link> page = new Page<>(pageSize,pageNum);
        page(page,queryWrapper);
        List<Link> records = page.getRecords();*/
        List<Link> records = list(queryWrapper);
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(records, LinkVo.class);
        return ResponseResult.okResult(new PageVo(linkVos,(long)records.size()));
    }

    @Override
    public ResponseResult addLink(LinkDto linkDto) {
        Link link = BeanCopyUtils.copyBean(linkDto, Link.class);
        save(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult getLinkById(Integer id) {
        Link byId = getById(id);
        LinkVo linkVo = BeanCopyUtils.copyBean(byId, LinkVo.class);
        return ResponseResult.okResult(linkVo);
    }

    @Override
    public ResponseResult updateLink(LinkVo linkvo) {
        Link link = BeanCopyUtils.copyBean(linkvo, Link.class);
        updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLink(Long id) {
        removeById(id);
        return ResponseResult.okResult();
    }
}
