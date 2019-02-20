package com.dhiggins.BatchRunner.services;

import com.dhiggins.BatchRunner.models.Job;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class JobService {

    private ReactiveRedisOperations<String, Job> jobOps;

    public JobService(@Qualifier("JobOps") ReactiveRedisOperations<String, Job> jobOps) {
        this.jobOps = jobOps;
    }

    public Mono<Job> getJobById(String id){
        return jobOps.opsForValue().get(id);
    }

    public Mono<Job> getJobByName(String name){
        return jobOps.keys("*").flatMap(key -> jobOps.opsForValue().get(key)).filter(job -> job.getName().equals(name)).single();
    }

    public Flux<Job> getAllJobs() {
        return jobOps.keys("*").flatMap(key -> jobOps.opsForValue().get(key));
    }
}
