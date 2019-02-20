package com.dhiggins.BatchRunner.services;


import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.BatchStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class BatchService {

    private final ReactiveRedisOperations<String, Batch> batchOps;
    private final JobService jobService;

    public BatchService(@Qualifier("BatchOps") ReactiveRedisOperations<String, Batch> batchOps, JobService jobService) {
        this.batchOps = batchOps;
        this.jobService = jobService;
    }

    public Mono<Batch> getById(String id){
        return batchOps.opsForValue().get(id);
    }

    public Flux<Batch> getAllBatches(){
        return batchOps.keys("*").flatMap(key -> batchOps.opsForValue().get(key));
    }

    public Mono<Boolean> addBatchById(String jobId){
        return jobService.getJobById(jobId)
                .map(job -> new Batch(UUID.randomUUID().toString(), LocalDateTime.now(), job.getName(), BatchStatus.OPEN))
                .flatMap(batch -> batchOps.opsForValue().set(batch.getCompositeId(), batch));
    }

    public Mono<Boolean> addBatchByName(String name){
        return jobService.getJobByName(name)
                .map(job -> new Batch(UUID.randomUUID().toString(), LocalDateTime.now(), job.getName(), BatchStatus.OPEN))
                .flatMap(batch -> batchOps.opsForValue().set(batch.getCompositeId(), batch));
    }

    public Flux<Batch> queue(){
        return batchOps.keys(BatchStatus.WAITING.name() + "*")
                .flatMap(key -> batchOps.opsForValue().get(key));
    }
}
