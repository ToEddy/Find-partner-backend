package com.findPartner.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.findPartner.common.constant.UserConstant;
import com.findPartner.common.exception.BusinessException;
import com.findPartner.common.exception.ErrorCode;
import com.findPartner.common.exception.SystemException;
import com.findPartner.domain.dto.UserDTO;
import com.findPartner.domain.dto.UserDTOWithPasswd;
import com.findPartner.domain.entity.User;
import com.findPartner.mapper.UserMapper;
import com.findPartner.service.UserService;
import com.findPartner.tools.UserUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.findPartner.common.constant.RedisAndRedisson.USER_RECOMMEND_PAGE;
import static com.findPartner.common.constant.UserConstant.REDIS_LOGIN_KEY;
import static com.findPartner.common.constant.UserConstant.SESSION_USER_STATE;

/**
 * @author 47607
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2023-02-22 11:35:55
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public long userRegister(UserDTOWithPasswd userDtoWithPasswd) {
        if (userDtoWithPasswd == null) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "");
        }
        String username = userDtoWithPasswd.getUsername();
        String account = userDtoWithPasswd.getUserAccount();
        String password = userDtoWithPasswd.getUserPassword();
        String checkPassword = userDtoWithPasswd.getCheckPassword();
        //1.账号，密码和校验密码都不能为空
        if (StringUtils.isAnyBlank(account, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "昵称，账号，密码和校验密码不能为空");
        }
        if (username == null || username.length() == 0) {
            username = UUID.randomUUID().toString();
            System.out.println(username);
        }
        //2.账号长度不能小于4位
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "账号不符合规范");
        }
        //3.账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "账号不符合规范");
        }
        //4.密码长度不能小于8位
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "密码不符合规范");
        }
        //5. 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "密码和校验密码不同");
        }
        //6.账号不能重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", account);
        boolean exists = userMapper.exists(queryWrapper);
        if (exists) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERR, "账号已经注册");
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());
        //创建用户
        User user = new User();
        user.setUsername(username);
        user.setUserAccount(account);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        if (!save) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERR, "账号注册失败");
        }
        return user.getId();
    }

    @Override
    public UserDTO userLogin(UserDTOWithPasswd userDtoWithPasswd, HttpServletRequest request) {
        if (userDtoWithPasswd == null) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "");
        }
        String account = userDtoWithPasswd.getUserAccount();
        String password = userDtoWithPasswd.getUserPassword();
        //1.账号，密码都不能为空
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "账号，密码不能为空");
        }
        //2.账号长度不能小于4位
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "账号长度不符合规范");
        }
        //3.账号不能包含特殊字符
        String validPattern = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(validPattern).matcher(account);
        if (matcher.find()) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "账号包含特殊字符");
        }
        //4.密码长度不能小于8位
        if (password.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERR_CODE, "密码长度不符合规范");
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + password).getBytes());
        //5.1 先检查账号是不是存在的 = > 也就是用户输入的账号有没有错误？
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", account);
        boolean exists = userMapper.exists(queryWrapper);
        if (!exists) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERR, "账号不存在");
        }
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null) {
            throw new BusinessException(ErrorCode.ACCOUNT_ERR, "账号和密码不匹配");
        }
        //用户脱敏
        UserDTO safetyUser = UserUtils.getSafetyUser(user);
        //将用户的登录态存储到服务器
        request.getSession().setAttribute(SESSION_USER_STATE, safetyUser);
        //将用户的登录信息存储到redis中
        stringRedisTemplate.opsForValue().set(REDIS_LOGIN_KEY + safetyUser.getId(), JSONUtil.toJsonStr(safetyUser));
        stringRedisTemplate.expire(REDIS_LOGIN_KEY + safetyUser.getId(), 1, TimeUnit.DAYS);
        return safetyUser;
    }


    @Override
    public List<UserDTO> userSearchByName(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("username", username);
        List<User> userList = userMapper.selectList(queryWrapper);
        if (userList == null || userList.size() == 0) {
            throw new BusinessException(ErrorCode.NONE_USER_ERR, "通过用户名暂未查询到用户");
        }
        //将User类型的集合转换成UserDto类型的集合
        return userList.stream().map(UserUtils::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> userSearchByTagsWithSql(long pageNum, long pageSize, List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        //通过forEach循环给queryWrapper添加很多的条件 ---> .like().like().like().like().like().like().....
        for (String tags : tagNameList) {
            queryWrapper.like("tags", tags);
        }
        Page<User> userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        if (userPage == null) {
            throw new BusinessException(ErrorCode.NONE_USER_ERR, "通过标签暂未查询到用户");
        }
        List<User> userList = userPage.getRecords();
        //将集合的每个元素脱敏再放入一个新地集合中
        return userList.stream().map(UserUtils::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public List<UserDTO> userSearchByTagsWithMemory(long pageNum, long pageSize, List<String> tagNameList) {
        if (CollectionUtils.isEmpty(tagNameList)) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "");
        }
        if (pageNum == 0) {
            pageNum = 1L;
        }
        if (pageSize == 0) {
            pageSize = 10L;
        }
        //1. 先查询所有的用户，将其加载到内存中，再提取符合条件的用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        List<User> userList = userMapper.selectList(queryWrapper);
        if (userList == null || userList.size() == 0) {
            throw new BusinessException(ErrorCode.NONE_USER_ERR, "通过标签暂未查询到用户");
        }
        Gson gson = new Gson();
        /**
         * 执行流程：先将useList 变成一个流，通过一定的条件过滤之后，在将其收集到一个新的map中
         */
        return userList.stream()
                .filter(//这个是过滤函数 filter 里面的代码块，他的需求值是一个布尔值
                        user -> {
                            String tagStr = user.getTags();
                            //从JSON字符串中解析出集合tagSet
                            Set<String> tagSet = gson.fromJson(tagStr, new TypeToken<Set<String>>() {
                            }.getType());
                            for (String tags : tagNameList) {
                                if (!tagSet.contains(tags)) {
                                    return false;
                                }
                            }
                            return true;
                        }
                ).map(UserUtils::getSafetyUser).collect(Collectors.toList());
    }

    @Override
    public boolean userDelete(String userAccount) {
        if (userAccount == null) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "参数不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        //权限足够
        int delete = userMapper.delete(queryWrapper);
        //更新用户的信息到缓存中
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean userCancel(String userPassword, UserDTO userDTO) {
        if (StringUtils.isEmpty(userPassword)) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "请输入密码");
        }
        Long userId = userDTO.getId();
        //在数据库中删除用户的信息
        String userAccount = userDTO.getUserAccount();
        String encryptPassword = DigestUtils.md5DigestAsHex((UserConstant.SALT + userPassword).getBytes());
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        int delete = userMapper.delete(queryWrapper);
        //在redis中删除用户信息
        stringRedisTemplate.delete(REDIS_LOGIN_KEY + userId);
        return delete > 0;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        try {
            request.getSession().removeAttribute(SESSION_USER_STATE);
        } catch (Exception e) {
            throw new SystemException(ErrorCode.SYSTEM_ERR_CODE, "系统异常，退出账号失败");
        }
        return true;
    }

    @Override
    @Transactional
    public boolean upToAdmin(String userAccount) {
        if (userAccount == null) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "参数不能为空");
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        //权限足够
        User user = userMapper.selectOne(queryWrapper);
        user.setUserRole(1);
        //更新用户的信息到缓存中
        boolean update = updateById(user);
        return update;
    }

    @Override
    public int userUpdate(UserDTO userDTOUpdate, HttpServletRequest request) {
        if (userDTOUpdate == null) {
            throw new BusinessException(ErrorCode.NULL_ERR_CODE, "请输入修改信息");
        }
        Object attribute = request.getSession().getAttribute(SESSION_USER_STATE);
        if (attribute == null) {
            throw new BusinessException(ErrorCode.NO_LOGIN, "用户暂未登录");
        }
        UserDTO safetyUser = (UserDTO) attribute;
        Long userId = safetyUser.getId();
        userDTOUpdate.setId(userId);
        //将修改的用户信息同步到redis中
        stringRedisTemplate.opsForValue().set(REDIS_LOGIN_KEY + safetyUser.getId(), JSONUtil.toJsonStr(userDTOUpdate));
        stringRedisTemplate.expire(REDIS_LOGIN_KEY + safetyUser.getId(), 1, TimeUnit.DAYS);
        //将修改的用户信息同步到Session中
        request.getSession().setAttribute(SESSION_USER_STATE, userDTOUpdate);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", userId);
        User user = BeanUtil.copyProperties(userDTOUpdate, User.class);
        return userMapper.update(user, queryWrapper);
    }

    @Override
    public Page<User> getRecommend(long pageNum, long pageSize, UserDTO safetyUser) {
        String redisKey = USER_RECOMMEND_PAGE + safetyUser.getId();
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        // 如果有缓存，直接读缓存
        Page<User> userPage = (Page<User>) valueOperations.get(redisKey);
        if (userPage != null) {
            return userPage;
        }
        // 无缓存，查数据库
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        userPage = userMapper.selectPage(new Page<>(pageNum, pageSize), queryWrapper);
        // 写缓存
        try {
            valueOperations.set(redisKey, userPage, 12, TimeUnit.HOURS);

        } catch (Exception e) {
            log.error("redis set key error", e);
        }
        return userPage;
    }
}




