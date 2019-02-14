package com.dhiggins.BatchRunner.utils;

import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.BatchStatus;
import org.springframework.data.redis.core.ReactiveRedisOperations;


public class BatchOpsUtil {

    public static Batch moveBatch(Batch oldBatch, BatchStatus newStatus, ReactiveRedisOperations<String, Batch> batchOps){
        Batch newBatch = oldBatch.clone();
        newBatch.setBatchStatus(newStatus);
        batchOps.opsForValue().set(newBatch.getCompositeId(), newBatch).subscribe(success -> {
            if(success){
                batchOps.opsForValue().delete(oldBatch.getCompositeId()).subscribe();
            }
        });

        return newBatch;
    }
}
