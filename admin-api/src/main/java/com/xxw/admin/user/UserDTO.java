package com.xxw.admin.user;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

/**
 * @author xxw
 * @date 2018/11/27
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString
public class UserDTO {

    /**
     * ID
     */
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 职位
     */
    private String position;

    /**
     * 状态，0-冻结，1-正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 创建者ID
     */
    private Integer creatorId;

    /**
     * 创建者名称
     */
    private String creatorName;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新者ID
     */
    private Integer updaterId;

    /**
     * 更新者名称
     */
    private String updaterName;
}
