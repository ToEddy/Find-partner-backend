package com.findPartner.domain.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户表
 *
 * @TableName user
 */
@TableName(value = "user")
@Data
public class User implements Serializable {
    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;
    /**
     * 用户昵称
     */
    private String username;
    /**
     * 账号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String userPassword;
    /**
     * 用户简介
     */
    private String userIntro;
    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 性别
     */
    private Integer gender;
    /**
     * 手机号
     */
    private String phone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 用户标签
     */
    private String tags;
    /**
     * 用户状态
     */
    private Integer userStatus;
    /**
     * 用户角色 （0-普通用户）（1-管理员）（2-最高管理员）
     */
    private Integer userRole;
    /**
     * 是否删除(逻辑删除)
     */
    @TableLogic
    private Integer isDelete;
    /**
     * 创建时间
     */
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date createTime;
    /**
     * 更新时间
     */
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date updateTime;
}