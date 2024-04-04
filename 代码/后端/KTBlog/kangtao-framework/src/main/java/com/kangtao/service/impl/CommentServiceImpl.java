package com.kangtao.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kangtao.constants.SystemConstants;
import com.kangtao.domain.ResponseResult;
import com.kangtao.domain.entity.Comment;
import com.kangtao.domain.utils.BeanCopyUtils;
import com.kangtao.domain.vo.CommentVo;
import com.kangtao.domain.vo.PageVo;
import com.kangtao.enums.AppHttpCodeEnum;
import com.kangtao.exception.SystemException;
import com.kangtao.mapper.CommentMapper;
import com.kangtao.service.CommentService;
import com.kangtao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
/*
import static com.sun.javafx.robot.impl.FXRobotHelper.getChildren;*/

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-02-21 19:51:58
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {
    private CommentService commentService;

    @Override
    public ResponseResult commentList(String commentType,Long articleId, Integer pageNum, Integer pageSize) {

        LambdaQueryWrapper<Comment> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemConstants.ARTICLE_COMMENT.equals(commentType),Comment::getArticleId,articleId);
        queryWrapper.eq(Comment::getRootId, SystemConstants.COMMENT_ROOT_ID);
        queryWrapper.eq(Comment::getType,commentType);
        Page<Comment> page =new Page<>(pageNum,pageSize);
        page(page,queryWrapper);
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());
        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
//查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
//赋值
            commentVo.setChildren(children);
        }
        return ResponseResult.okResult(new PageVo(commentVoList,page.getTotal()));
    }

    @Override
    public ResponseResult addComment(Comment comment) {
        if(!StringUtils.hasText(comment.getContent())){
            throw new SystemException(AppHttpCodeEnum.COMMENT_NOT_NULL);
        }
        save(comment);
        return ResponseResult.okResult();
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * @param id 根评论的id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {
        LambdaQueryWrapper<Comment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Comment::getRootId,id);
        queryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = list(queryWrapper);
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }

    @Autowired
    private UserService userService;
    private List<CommentVo> toCommentVoList(List<Comment> list){
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list,
                CommentVo.class);
//遍历vo集合
        for (CommentVo commentVo : commentVos) {
//通过creatyBy查询用户的昵称并赋值
            String nickName =
                    userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
//通过toCommentUserId查询用户的昵称并赋值
//如果toCommentUserId不为-1才进行查询
            if(commentVo.getToCommentUserId()!=-1){
                String toCommentUserName =
                        userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }
}
