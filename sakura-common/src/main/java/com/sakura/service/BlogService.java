package com.sakura.service;

import com.sakura.dto.AddBlogDTO;
import com.sakura.farme.pojo.Results;


/**
 * @author: bi
 * @date: 2021/11/23 10:15
 */
public interface BlogService {

    Results<?> addComment(AddBlogDTO addBlogDTO);

    Results<?> listComment();

    void deleteComment(Long ids);
}
