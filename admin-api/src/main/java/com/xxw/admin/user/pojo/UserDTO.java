package com.xxw.admin.user.pojo;

import com.xxw.common.json.serializer.LocalDateTimeFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    @NotNull(message = "ID不能为空", groups = Update.class)
    private Integer id;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = Save.class)
    @Length(min = 8, max = 32, message = "用户名长度为8-32个字符", groups = {Save.class, Update.class})
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = Save.class)
    @Length(min = 8, max = 16, message = "密码长度为8-16个字符", groups = {Save.class, Update.class})
    private String password;

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空", groups = Save.class)
    @Length(min = 2, max = 32, message = "昵称长度为2-32个字符", groups = {Save.class, Update.class})
    private String nickname;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = Save.class)
    @Pattern(regexp = "^(13[0-9]|14[01456789]|15[0-9]|16[56]|17[01235678]|18[0-9]|19[89])\\d{8}$",
            message = "手机号格式错误", groups = {Save.class, Update.class})
    private String mobile;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式错误", groups = {Save.class, Update.class})
    @Length(max = 64, message = "邮箱长度最多为64个字符", groups = {Save.class, Update.class})
    private String email;

    /**
     * 是否可用
     */
    @NotNull(message = "状态不能为空", groups = Save.class)
    private Boolean enabled;

    /**
     * 是否固定
     */
    private Boolean fixed;

    /**
     * 创建时间
     */
    @LocalDateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
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
    @LocalDateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 更新者ID
     */
    private Integer updaterId;

    /**
     * 更新者名称
     */
    private String updaterName;

    public interface Save {}

    public interface Update {}
}
