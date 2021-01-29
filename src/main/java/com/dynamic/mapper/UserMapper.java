package com.dynamic.mapper;


import com.dynamic.mapper.dto.User;

import java.util.List;

/**
 * @author zhangqi
 * @version 1.0
 */
public interface UserMapper {
    List<User> listAll();

    int insert(User user);
}
