package com.sakura.controller;

import com.sakura.dto.AddBlogDTO;
import com.sakura.farme.pojo.Results;
import com.sakura.service.BlogService;
import com.sakura.web.annotation.NoLoginRequired;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: bi
 * @date: 2021/11/23 10:26
 */
@NoLoginRequired
@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/addBlog")
    public Results<?> addBlog(@RequestBody AddBlogDTO addBlogDTO) {
        return blogService.addComment(addBlogDTO);
    }

    @RequestMapping("/listBlog")
    public Results<?> listBlog() {
        return blogService.listComment();
    }

    @PostMapping("/deleteBlog")
    public Results<?> deleteBlog(@RequestBody Long ids) {
        blogService.deleteComment(ids);
        return Results.buildSuccess("删除成功", true);
    }
}
