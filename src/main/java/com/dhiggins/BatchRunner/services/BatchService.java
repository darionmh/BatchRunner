package com.dhiggins.BatchRunner.services;


import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.BatchStatus;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class BatchService {

    private final ReactiveRedisOperations<String, Batch> batchOps;

    public BatchService(ReactiveRedisOperations<String, Batch> batchOps) {
        this.batchOps = batchOps;
    }

    public Mono<Batch> getById(String id){
        return batchOps.opsForValue().get(id);
    }

    public Flux<Batch> getAllBatches(){
        return batchOps.keys("*").flatMap(key -> batchOps.opsForValue().get(key));
    }

    public Mono<Boolean> addBatch(String xml){
        Batch batch = new Batch(UUID.randomUUID().toString(), xml, null, BatchStatus.OPEN);

        return batchOps.opsForValue().set(batch.getCompositeId(), batch);
    }

    public Flux<Batch> queue(){
        return batchOps.keys(BatchStatus.WAITING.name() + "*")
                .flatMap(key -> batchOps.opsForValue().get(key));
    }
}
