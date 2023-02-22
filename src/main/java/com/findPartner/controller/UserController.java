package com.findPartner.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.findPartner.common.exception.BusinessException;
import com.findPartner.common.exception.ErrorCode;
import com.findPartner.domain.R.ResultData;
import com.findPartner.domain.dto.UserDTO;
import com.findPartner.domain.dto.UserDTOWithPasswd;
import com.findPartner.domain.entity.User;
import com.findPartner.service.UserService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.findPartner.common.constant.UserConstant.SESSION_USER_STATE;

/**
 * @author eddy
 * @createTime 2023/2/13
 */
@Api(value = "用户模块")
@RestController
@Slf4j
@RequestMapping("/user")
@CrossOrigin(origins = {"http://127.0.0.1:5173"})
public class UserController {
    //todo 基于knife4j 将文档进行优化和完善
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;

    @PostMapping("/register")
    public ResultData<Long> UserRegister(@RequestBody UserDTOWithPasswd userDtoWithPasswd) {
        long ResultID = userService.userRegister(userDtoWithPasswd);
        return ResultData.success(ResultID, "用户注册成功");
    }

    @PostMapping("login")
    public ResultData<UserDTO> UserLogin(@RequestBody UserDTOWithPasswd userDtoWithPasswd, HttpServletRequest request) {
        UserDTO safetyUser = userService.userLogin(userDtoWithPasswd, request);
        return ResultData.success(safetyUser, "用户登陆成功");
    }

    @GetMapping("/current")
    public ResultData<UserDTO> getCurrent(HttpServletRequest request) {
        Object attribute = request.getSession().getAttribute(SESSION_USER_STATE);
        if (attribute == null) {
            return ResultData.fail("用户暂未登录");
        }
        UserDTO safetyUser = (UserDTO) attribute;
        return ResultData.success(safetyUser, "获取当前用户成功");
    }

    // todo 用户昵称是否唯一？
    @GetMapping("/searchByName")
    public ResultData<List<UserDTO>> UserSearchByName(@RequestParam("username") String username) {
        List<UserDTO> userDTOList = userService.userSearchByName(username);
        return ResultData.success(userDTOList, "查找用户成功");
    }

    @GetMapping("/searchByTags")
    public ResultData<List<UserDTO>> UserSearchByTags(long pageNum, long pageSize, @RequestParam(required = false) List<String> tagNameList) {
        List<UserDTO> userDTOList = userService.userSearchByTagsWithSql(pageNum, pageSize, tagNameList);
        return ResultData.success(userDTOList, "查找用户成功");
    }

    @GetMapping("/recommend")
    public ResultData<Page<User>> getRecommend(long pageNum, long pageSize, HttpServletRequest request) {
        //在前端做一个默认的page参数的设置
        Object attribute = request.getSession().getAttribute(SESSION_USER_STATE);
        if (attribute == null) {
            return ResultData.fail("用户暂未登录");
        }
        UserDTO safetyUser = (UserDTO) attribute;
        Page<User> userPage = userService.getRecommend(pageNum, pageSize, safetyUser);
        return ResultData.success(userPage, "查询用户页成功");
    }

    @PostMapping("/cancel")
    public ResultData<Boolean> UserCancel(@RequestParam("userPassword") String userPassword, HttpServletRequest request) {
        //鉴权
        Object attribute = request.getSession().getAttribute(SESSION_USER_STATE);
        UserDTO safetyUser = (UserDTO) attribute;
        if (attribute == null) {
            throw new BusinessException(ErrorCode.NO_AUTH, "您没有登录");
        }
        boolean isCancel = userService.userCancel(userPassword, safetyUser);
        if (!isCancel) {
            return ResultData.fail("用户注销失败");
        }
        return ResultData.success(true, "用户注销成功");
    }

    @PostMapping("/delete")
    public ResultData<Boolean> userDelete(@RequestParam("userAccount") String userAccount, HttpServletRequest request) {
        //鉴权
        Object attribute = request.getSession().getAttribute(SESSION_USER_STATE);
        UserDTO safetyUser = (UserDTO) attribute;
        int userRole = safetyUser.getUserRole();
        if (userRole == 0 || userRole == 1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "您没有权限");
        }
        boolean isDelete = userService.userDelete(userAccount);
        if (!isDelete) {
            return ResultData.fail("用户删除失败");
        }
        return ResultData.success(true, "用户删除成功");
    }

    @PostMapping("/update")
    public ResultData userUpdate(@RequestBody UserDTO userDto, HttpServletRequest request) {
        int isUpdate = userService.userUpdate(userDto, request);
        if (isUpdate == 0) {
            return ResultData.fail("用户更新失败");
        }
        return ResultData.success(true, "用户更新成功");
    }

    @PostMapping("/logout")
    public ResultData<Boolean> userLogout(HttpServletRequest request) {
        boolean isLogout = userService.userLogout(request);
        if (!isLogout) {
            return ResultData.fail("用户退出登录失败");
        }
        return ResultData.success(true, "用户成功退出登录");
    }

    @PostMapping("/upgrade")
    public ResultData<Boolean> userUpgrade(@RequestParam("userAccount") String userAccount, HttpServletRequest request) {
        //鉴权
        Object attribute = request.getSession().getAttribute(SESSION_USER_STATE);
        UserDTO safetyUser = (UserDTO) attribute;
        int userRole = safetyUser.getUserRole();
        if (userRole == 0 || userRole == 1) {
            throw new BusinessException(ErrorCode.NO_AUTH, "您没有权限");
        }
        boolean isUpgrade = userService.upToAdmin(userAccount);
        if (!isUpgrade) {
            return ResultData.fail("用户升级失败");
        }
        return ResultData.success(true, "用户升级成功");
    }
}

