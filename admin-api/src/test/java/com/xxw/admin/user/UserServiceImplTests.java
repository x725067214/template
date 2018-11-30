package com.xxw.admin.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/27
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceImplTests {

    @Autowired
    private UserService userService;

    //@Test
    public void findByUsername() {
        Optional<UserDTO> userDTO = userService.findByUsername("system");
        System.out.println(userDTO.get().toString());
    }
}
