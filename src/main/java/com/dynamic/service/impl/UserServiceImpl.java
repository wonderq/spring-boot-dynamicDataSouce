package com.dynamic.service.impl;

import com.dynamic.mapper.UserMapper;
import com.dynamic.mapper.dto.User;
import com.dynamic.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @version 1.0
 */
@Service("userService")
public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);
    @Resource
    private UserMapper userMapper;

    @Override
    public List<User> listAll() {
        return userMapper.listAll();
    }

    @Override
    public int insert(User user) {
        return userMapper.insert(user);
    }
}