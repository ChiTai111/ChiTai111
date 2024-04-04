package com.kangtao.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TestJob {
    @Scheduled(cron = "0/55 * * * * ?")
    public void testJob(){
//要执行的代码
        System.out.println("定时任务执行了");
    }
}