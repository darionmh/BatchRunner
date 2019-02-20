package com.dhiggins.BatchRunner.utils;

import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.BatchStatus;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import reactor.core.publisher.Mono;


public class BatchOpsUtil {

    public static Mono<Boolean> moveBatch(Batch batch, BatchStatus newStatus, ReactiveRedisOperations<String, Batch> batchOps){
        String oldKey = batch.getCompositeId();

        batch.setBatchStatus(newStatus);

        return batchOps.rename(oldKey, batch.getCompositeId()).then(
                batchOps.opsForValue().set(batch.getCompositeId(), batch));
    }
}
