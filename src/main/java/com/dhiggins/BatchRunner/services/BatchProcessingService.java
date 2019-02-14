package com.dhiggins.BatchRunner.services;

import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.BatchRunner;
import com.dhiggins.BatchRunner.models.BatchStatus;
import com.dhiggins.BatchRunner.utils.BatchOpsUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.concurrent.Future;


@Component
public class BatchProcessingService {
    private final String WORKING_DIR = "/Users/darionhiggins/BatchRunner/src/main/resources";
    private ReactiveRedisOperations<String, Batch> batchOps;
    private BatchService batchService;
    private ThreadPoolTaskExecutor taskExecutor;

    public BatchProcessingService(ReactiveRedisOperations<String, Batch> batchOps, BatchService batchService, @Qualifier("executor") TaskExecutor taskExecutor) {
        this.batchOps = batchOps;
        this.batchService = batchService;
        this.taskExecutor = (ThreadPoolTaskExecutor) taskExecutor;
    }

    @Scheduled(fixedRate = 1000)
    public void checkOps(){
        batchOps.keys(BatchStatus.OPEN + "*")
                .flatMap(key -> batchOps.opsForValue().get(key))
                .subscribe(batch -> {
                    batch = BatchOpsUtil.moveBatch(batch, BatchStatus.QUEUED, batchOps);
                    System.out.println("Queuing "+batch);
                });
    }

    @Scheduled(fixedRate = 1000)
    public void checkQueue(){
        batchOps.keys(BatchStatus.QUEUED+"*")
                .flatMap(key -> batchOps.opsForValue().get(key))
                .subscribe(batch -> {
                    //Need another batch to run
                    batch = BatchOpsUtil.moveBatch(batch, BatchStatus.WAITING, batchOps);
                    Future f = taskExecutor.submit(new BatchRunner(batch, getProcess(), batchOps));
                });
    }

    private ProcessBuilder getProcess(){
        ProcessBuilder pb = new ProcessBuilder("./wait.sh");
        pb.directory(new File(WORKING_DIR));
        return pb;
    }

}
