package com.findPartner.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.findPartner.domain.dto.UserDTO;
import com.findPartner.domain.dto.UserDTOWithPasswd;
import com.findPartner.domain.entity.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author 47607
 * @description 针对表【user(用户表)】的数据库操作Service
 * @createDate 2023-02-22 11:35:55
 */
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param userDtoWithPasswd
     * @return
     */
    long userRegister(UserDTOWithPasswd userDtoWithPasswd);

    /**
     * 用户登录
     *
     * @param userDtoWithPasswd
     * @param request
     * @return
     */
    UserDTO userLogin(UserDTOWithPasswd userDtoWithPasswd, HttpServletRequest request);

    /**
     * 修改用户
     *
     * @param userDTOUpdate
     * @param request
     * @return
     */
    int userUpdate(UserDTO userDTOUpdate, HttpServletRequest request);

    /**
     * 通过姓名查找用户
     *
     * @param username
     * @return
     */
    List<UserDTO> userSearchByName(String username);


    /**
     * 通过标签查找用户(SQL语句查询)
     *
     * @param tagList
     * @return
     */
    List<UserDTO> userSearchByTagsWithSql(long pageNum, long pageSize, List<String> tagList);

    /**
     * 通过标签查找用户 (内存查询)
     *
     * @param tagList
     * @return
     */
    List<UserDTO> userSearchByTagsWithMemory(long pageNum, long pageSize, List<String> tagList);

    boolean userDelete(String userAccount);

    /**
     * 用户注销
     *
     * @param userPassword
     * @param userDTO
     * @return
     */
    boolean userCancel(String userPassword, UserDTO userDTO);

    /**
     * 用户退出登录
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户升级权限
     *
     * @param userAccount
     * @return
     */
    boolean upToAdmin(String userAccount);

    /**
     * 首页用户推荐页
     *
     * @return
     */
    Page<User> getRecommend(long pageNum, long pageSize, UserDTO safetyUser);

}
