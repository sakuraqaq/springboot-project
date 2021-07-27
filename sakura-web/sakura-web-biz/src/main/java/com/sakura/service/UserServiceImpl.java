package com.sakura.service;

import com.sakura.entity.User;
import com.sakura.farme.wapper.QueryWrapper;
import com.sakura.mapper.UserMapper;
import com.sakura.oss.OssService;
import com.sakura.uid.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author : bi
 * @since : 2021年06月24日
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserServiceImpl implements UserService {

    private final IdGenerator idGenerator;
    private final UserMapper userMapper;
    private final OssService ossService;

    @Override
    public User getUser() {
        System.out.println(idGenerator.getUID()+"\r\n"+ossService);
        return  userMapper.selectOne(new QueryWrapper<User>()
                .eq(User::getPhoneNumber, "17610068303"));
    }
}
