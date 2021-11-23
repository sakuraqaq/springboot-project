package com.sakura.service;

import com.sakura.dto.AddBlogDTO;
import com.sakura.entity.Blog;
import com.sakura.farme.pojo.Results;
import com.sakura.farme.wapper.QueryWrapper;
import com.sakura.mapper.BlogMapper;
import com.sakura.uid.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;

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
        return Results.buildSuccess("查询成功：", blogMapper.selectList(new QueryWrapper<Blog>().eq(Blog::getType, 1)));
    }

    @Override
    public void deleteComment(Long ids) {
        blogMapper.delete(new QueryWrapper<Blog>().eq(Blog::getBlogId, ids));
    }
}
