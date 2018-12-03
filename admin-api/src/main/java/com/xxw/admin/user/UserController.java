package com.xxw.admin.user;

import com.xxw.admin.user.pojo.UserDTO;
import com.xxw.common.web.PageModel;
import com.xxw.common.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @PostMapping("/user/save")
    public ResponseResult save(@Validated(UserDTO.Save.class) @RequestBody UserDTO userDTO) {
        userService.save(userDTO);
        return ResponseResult.success();
    }

    @GetMapping("/user/find")
    public ResponseResult find(Integer id) {
        return ResponseResult.success(userService.findById(id));
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

    @GetMapping("/user/page")
    public ResponseResult page(UserDTO userDTO, Pageable pageable) {
        Page<UserDTO> userDTOPage = userService.findAll(userDTO, pageable);
        return ResponseResult.success(new PageModel(userDTOPage.getContent(), userDTOPage.getTotalElements()));
    }
}
