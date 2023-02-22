package com.findPartner.tools.ScheduledTask;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.findPartner.domain.entity.User;
import com.findPartner.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.findPartner.common.constant.RedisAndRedisson.CACHE_RLOCK_NAME;
import static com.findPartner.common.constant.RedisAndRedisson.USER_RECOMMEND_PAGE;

/**
 * 定时任务 缓存预热，当同时有多台服务器的时候，我们就需要加锁
 *
 * @author eddy
 * @createTime 2023/2/21
 */
@Component
@Slf4j
public class PreCacheJob {
    @Resource
    private UserService userService;
    @Resource
    private RedisTemplate redisTemplate;
    @Resource
    private RedissonClient redissonClient;
    private List<Long> mainUserList = List.of(1L);

    //每年每月每天的零时零分零秒开始缓存预热
    @Scheduled(cron = "0 0 0 * * *")
    public void doCacheRecommend() {
        RLock rLock = redissonClient.getLock(CACHE_RLOCK_NAME);
        try {
            if (rLock.tryLock(0, -1, TimeUnit.MICROSECONDS)) {
                for (Long aLong : mainUserList) {
                    //从数据库查信息
                    QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                    Page<User> userPage = userService.page(new Page<>(1, 10), queryWrapper);
                    //写缓存
                    try {
                        redisTemplate.opsForValue().set(USER_RECOMMEND_PAGE + aLong, userPage, 100, TimeUnit.SECONDS);
                    } catch (Exception e) {
                        log.error("写入缓存失败", e);
                    }
                }
            }
        } catch (InterruptedException e) {
            log.error("doCacheRecommend error" + e);
        } finally {
            if (rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }
}
