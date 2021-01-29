package com.dynamic.service;

import com.dynamic.mapper.dto.User;

import java.util.List;

/**
 * @program: dynamicDataSource
 * @description:
 * @author: zhangqi
 * @create: 2018-01-29 09:56
 **/
public interface UserService {
     List<User> listAll();

     int insert(User user);
}
