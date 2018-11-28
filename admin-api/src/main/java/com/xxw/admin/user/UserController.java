package com.xxw.admin.user;

import com.xxw.common.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author xxw
 * @date 2018/11/27
 */
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/user")
    @ResponseBody
    public ResponseResult findById(Integer id) {
        return ResponseResult.success(userService.findById(id));
    }
}
