package com.xxw.admin.user.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * @author xxw
 * @date 2018/11/24
 */
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "user")
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
	private String username;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 真实姓名
	 */
	@Column(name = "real_name")
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
	 * 是否可用
	 */
	private Boolean enabled;

	/**
	 * 是否固定
	 */
	private Boolean fixed;

	/**
	 * 创建时间
	 */
	@CreatedDate
	@Column(name = "create_time")
	private LocalDateTime createTime;

	/**
	 * 创建者ID
	 */
	@Column(name = "creator_id")
	private Integer creatorId;

	/**
	 * 创建者名称
	 */
	@Column(name = "creator_name")
	private String creatorName;

	/**
	 * 更新时间
	 */
	@LastModifiedDate
	@Column(name = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 更新者ID
	 */
	@Column(name = "updater_id")
	private Integer updaterId;

	/**
	 * 更新者名称
	 */
	@Column(name = "updater_name")
	private String updaterName;
}
