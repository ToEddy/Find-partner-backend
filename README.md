# 伙伴匹配系统

### 数据库设计

- 用户的标签是否需要一个关联表去关联用户的多个标签:
- 处理方式：将用户的标签使用 json 格式存储

## 代码优化设计

### 1.用户相关的实体类 (dto) 的设计怎么样才能符合数据的保护：

1. UserDto

- 尽量去掉用户的私密信息:
    - password

2. UserDtoWithPasswd

- 适用于注册和登录
- 校验密码（当用户是登录的时候，前端传递一个null给后端）

### 2.异常处理类 (Exception) 和数据返回类 (ResultData) 的设计：

1. resultdata


2. exception 
