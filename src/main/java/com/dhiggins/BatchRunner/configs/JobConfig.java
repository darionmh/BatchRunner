package com.dhiggins.BatchRunner.configs;

import com.dhiggins.BatchRunner.models.JobList;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class JobConfig {

    @ConfigurationProperties("config")
    @Bean
    public JobList jobList(){
        return new JobList();
    }

    @Bean
    Map<String, ThreadPoolTaskExecutor> jobThreadPool(JobList jobList){
        Map<String, ThreadPoolTaskExecutor> threadPoolMap = new HashMap<>();

        jobList.getJobs().forEach(job -> threadPoolMap.put(job.getId(), threadPoolTaskExecutor(job.getName(), job.getCapacity())));

        return threadPoolMap;
    }

    private ThreadPoolTaskExecutor threadPoolTaskExecutor(String name, int poolSize) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix(name);
        executor.setCorePoolSize(poolSize);
        executor.setMaxPoolSize(poolSize);
        executor.initialize();
        return executor;
    }
}
