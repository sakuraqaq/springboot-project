package com.sakura.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sakura.dto.AddBlogDTO;
import com.sakura.entity.Blog;
import com.sakura.farme.pojo.Results;
import com.sakura.mapper.BlogMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;

/**
 * @author: bi
 * @date: 2021/11/23 10:15
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogServiceImpl implements BlogService {

    private final BlogMapper blogMapper;

    @Override
    public Results<?> addComment(AddBlogDTO addBlogDTO) {
        Blog blog = new Blog();
        blog.setUsername(addBlogDTO.getUserName());
        blog.setContent(addBlogDTO.getContent());
        blog.setCreateDate(Calendar.getInstance().getTime());
        blog.setType((short) 1);
        blogMapper.insert(blog);
        return Results.buildSuccess("评论成功", blog);
    }

    @Override
    public Results<?> listComment() {
        return Results.buildSuccess("查询成功：", blogMapper.selectList(new LambdaQueryWrapper<Blog>().eq(Blog::getType, 1)));
    }

    @Override
    public void deleteComment(Long ids) {
        blogMapper.delete(new LambdaQueryWrapper<Blog>().eq(Blog::getBlogId, ids));
    }
}
