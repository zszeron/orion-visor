package com.orion.ops.module.asset.task;

import com.orion.ops.module.asset.service.PathBookmarkGroupService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 定时清理未使用的路径标签分组
 *
 * @author Jiahang Li
 * @version 1.0.0
 * @since 2024/4/24 23:50
 */
@Slf4j
@Component
public class PathBookmarkGroupAutoClearTask {

    /**
     * 分布式锁名称
     */
    private static final String LOCK_KEY = "clear:pbg:lock";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private PathBookmarkGroupService pathBookmarkGroupService;

    /**
     * 清理
     */
    @Scheduled(cron = "0 20 2 * * ?")
    public void clear() {
        log.info("PathBookmarkGroupAutoClearTask.clear start");
        // 获取锁
        RLock lock = redissonClient.getLock(LOCK_KEY);
        // 未获取到直接返回
        if (!lock.tryLock()) {
            log.info("PathBookmarkGroupAutoClearTask.clear locked end");
            return;
        }
        try {
            // 清理
            pathBookmarkGroupService.clearUnusedGroup();
            log.info("PathBookmarkGroupAutoClearTask.clear finish");
        } catch (Exception e) {
            log.error("PathBookmarkGroupAutoClearTask.clear error", e);
        } finally {
            lock.unlock();
        }
    }

}
