package com.dhiggins.BatchRunner;

import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.Job;
import com.dhiggins.BatchRunner.models.JobList;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class DataLoader {

    private ReactiveRedisConnectionFactory factory;
    private ReactiveRedisOperations<String, Batch> batchOps;
    private ReactiveRedisOperations<String, Job> jobOps;
    private JobList jobList;

    public DataLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Batch> batchOps, ReactiveRedisOperations<String, Job> jobOps, JobList jobList) {
        this.factory = factory;
        this.batchOps = batchOps;
        this.jobOps = jobOps;
        this.jobList = jobList;
    }

//    @PostConstruct
//    void loadData(){
//        factory.getReactiveConnection().serverCommands().flushAll().thenMany(
//                Flux.just("A", "B", "C", "D")
//                    .map(b->new Batch(UUID.randomUUID().toString(), b, b.getBytes(), BatchStatus.OPEN))
//                    .flatMap(b->batchOps.opsForValue().set(b.getCompositeId(), b)))
//                .thenMany(batchOps.keys("*")
//                    .flatMap(key -> batchOps.opsForValue().get(key)))
//                .subscribe(System.out::println);
//    }


    @PostConstruct
    void loadJobs(){
        System.out.println(jobList.getJobs());
        factory.getReactiveConnection().serverCommands().flushAll().thenMany(
                Flux.fromIterable(jobList.getJobs())
                .flatMap(job -> {
                    job.setId(UUID.randomUUID().toString());
                    System.out.println(job);
                    return jobOps.opsForValue().set("JOB-" + job.getId(), job);
                })
        ).subscribe();
    }
}
