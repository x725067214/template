package com.xxw.admin.user;

import com.xxw.admin.user.pojo.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * @author xxw
 * @date 2018/11/27
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class UserServiceImplTests {

    @Autowired
    private UserService userService;

    //@Test
    public void findByUsername() {
        Optional<UserDTO> userDTO = userService.findByUsername("system");
        System.out.println(userDTO.get().toString());
    }
}
