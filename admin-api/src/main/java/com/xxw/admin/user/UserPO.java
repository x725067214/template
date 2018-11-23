package com.xxw.admin.user;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 用户
 * Created by xuxiaowei on 2018/11/23.
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name="user")
@EntityListeners(AuditingEntityListener.class)
public class UserPO {

	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	/**
	 * 用户名
	 */
	@NotBlank(message = "用户名不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "用户名长度为1-32个字符",groups = {Save.class, Update.class})
	private String username;

	/**
	 * 密码
	 */
	@NotBlank(message = "密码不能为空", groups = Save.class)
	@Length(min = 1, max = 256, message = "密码长度为1-256个字符",groups = {Save.class, Update.class})
	private String password;

	/**
	 * 真实姓名
	 */
	@NotBlank(message = "真实姓名不能为空", groups = Save.class)
	@Length(min = 1, max = 32, message = "真实姓名长度为1-32个字符",groups = {Save.class, Update.class})
	private String realName;

	/**
	 * 手机号
	 */
	@NotBlank(message = "手机号不能为空", groups = Save.class)
	@Length(min = 1, max = 16, message = "手机号长度为1-16个字符",groups = {Save.class, Update.class})
	private String mobile;

	/**
	 * 邮箱
	 */
	@Length(max = 64, message = "邮箱长度最多为64个字符",groups = {Save.class, Update.class})
	private String email;

	/**
	 * 职位
	 */
	@Length(max = 16, message = "职位长度最多为16个字符",groups = {Save.class, Update.class})
	private String position;

	/**
	 * 状态，0-冻结，1-正常
	 */
	@NotNull(message = "状态不能为空", groups = Save.class)
	@Range(min = 0, message = "状态至少为0", groups = {Save.class, Update.class})
	private Integer status;

	/**
	 * 创建时间
	 */
	@CreatedDate
	private LocalDateTime createTime;

	/**
	 * 创建者ID
	 */
	@Range(min = 0, message = "创建者ID至少为0", groups = {Save.class, Update.class})
	private Integer creatorId;

	/**
	 * 创建者名称
	 */
	@Length(max = 32, message = "创建者名称长度最多为32个字符",groups = {Save.class, Update.class})
	private String creatorName;

	/**
	 * 更新时间
	 */
	@LastModifiedDate
	private LocalDateTime updateTime;

	/**
	 * 更新者ID
	 */
	@Range(min = 0, message = "更新者ID至少为0", groups = {Save.class, Update.class})
	private Integer updatorId;

	/**
	 * 更新者名称
	 */
	@Length(max = 32, message = "更新者名称长度最多为32个字符",groups = {Save.class, Update.class})
	private String updatorName;

	public interface Save {}

	public interface Update {}
}
