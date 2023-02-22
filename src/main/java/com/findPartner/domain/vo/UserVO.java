package com.findPartner.domain.vo;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * @author eddy
 * @createTime 2023/2/14
 */
public class UserVO implements Serializable {
    @Serial
    private static final long serialVersionUID = -2515465819362798448L;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatar;
    /**
     * 用户简介
     */
    private String userIntro;

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
    //TODO 只有管理员才可以查看 （能不能创建两个dto 分别由于普通用户和管理员）
    private Integer userStatus;

    /**
     * 用户角色
     */
    private Integer userRole;

    /**
     * 创建时间
     */
    //TODO 只有管理员才可以查看
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date createTime;

    /**
     * 更新时间
     */
    //TODO 只有管理员才可以查看,为什么返回的是一个时间戳，怎么将其转换成正常的日期格式
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date updateTime;
}
