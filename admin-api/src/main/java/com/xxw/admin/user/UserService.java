package com.xxw.admin.user;

import com.xxw.admin.user.pojo.UserDTO;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/27
 */
public interface UserService {

    Optional<UserDTO> findByUsername(String username);
}
