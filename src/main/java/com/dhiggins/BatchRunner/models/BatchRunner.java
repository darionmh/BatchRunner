package com.dhiggins.BatchRunner.models;

import com.dhiggins.BatchRunner.utils.BatchOpsUtil;
import org.springframework.data.redis.core.ReactiveRedisOperations;

public class BatchRunner implements Runnable{
    private Batch batch;
    private ProcessBuilder pb;
    private Process p;
    private final ReactiveRedisOperations<String, Batch> batchOps;

    public BatchRunner(Batch batch, ProcessBuilder pb, ReactiveRedisOperations<String, Batch> batchOps) {
        this.batch = batch;
        this.pb = pb;
        this.batchOps = batchOps;
    }

    public Batch getBatch() {
        return batch;
    }

    public void setBatch(Batch batch) {
        this.batch = batch;
    }

    public ProcessBuilder getPb() {
        return pb;
    }

    public void setPb(ProcessBuilder pb) {
        this.pb = pb;
    }

    public Process getP() {
        return p;
    }

    public void setP(Process p) {
        this.p = p;
    }

    @Override
    public void run() {
        try {
            BatchOpsUtil.moveBatch(batch, BatchStatus.RUNNING, batchOps);
            System.out.println("Running batch "+batch);
            p = pb.start();
            while (p.isAlive()) ;
            BatchOpsUtil.moveBatch(batch, BatchStatus.COMPLETE, batchOps);
            System.out.println("Batch complete "+batch);
        }catch (Exception e){
            System.err.println("Error running batch");
        }
    }
}
