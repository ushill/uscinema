package com.ushill.configure;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ushill.utils.stat.CachedStatMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * @author ：五羊
 * @description：TODO
 * @date ：2020/4/2 下午10:38
 */
@Configuration
@EnableScheduling
public class StaticScheduleTask {

    private final CachedStatMap cachedStatMap;

    public StaticScheduleTask(CachedStatMap cachedStatMap) {
        this.cachedStatMap = cachedStatMap;
    }

    /**
     * @description: 将缓存命中/未命中的统计信息上传到Redis
     * @author: 五羊
     * @date: 2020/4/2 下午10:41
     * @params:
     * @return
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(fixedRate=60000)
    private void uploadCachedStats() throws JsonProcessingException {
        cachedStatMap.uploadCachedStats();
//        System.err.println("执行cacheCachedStats定时任务时间: " + LocalDateTime.now());
    }
}