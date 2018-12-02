package com.xxw.admin.user;

import com.github.dozermapper.core.Mapper;
import com.xxw.admin.user.pojo.UserDTO;
import com.xxw.admin.user.pojo.UserVO;
import com.xxw.common.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author xxw
 * @date 2018/11/27
 */
@RestController
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private Mapper mapper;

    @PostMapping("/user/save")
    public ResponseResult save(@Validated(UserDTO.Save.class) @RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseResult.success();
    }

    @GetMapping("/user/find")
    public ResponseResult findById(Integer id) {
        UserDTO userDTO = userService.findById(id);
        UserVO userVO = mapper.map(userDTO, UserVO.class);
        return ResponseResult.success(userVO);
    }

    @PostMapping("/user/update")
    public ResponseResult update(@Validated(UserDTO.Update.class) @RequestBody UserDTO userDTO) {
        userService.update(userDTO);
        return ResponseResult.success();
    }

    @GetMapping("/user/delete")
    public ResponseResult delete(Integer id) {
        userService.deleteById(id);
        return ResponseResult.success();
    }
}
