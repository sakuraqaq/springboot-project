package com.sakura.service;

import com.sakura.component.SakuraSessionUser;
import com.sakura.dto.UserDTO;
import com.sakura.entity.User;
import com.sakura.farme.pojo.Results;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
public interface UserService {

    User getUser(SakuraSessionUser sessionUser);

    int setUser(UserDTO userDTO);

    Results<?> approve(Long workFlowId);

    Results<?> approve1(Long workFlowId);
}
