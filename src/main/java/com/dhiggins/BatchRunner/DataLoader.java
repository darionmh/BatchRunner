package com.dhiggins.BatchRunner;

import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.BatchStatus;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;
import java.util.UUID;

@Component
public class DataLoader {

    private ReactiveRedisConnectionFactory factory;
    private ReactiveRedisOperations<String, Batch> batchOps;
    private ChannelTopic topic;

    public DataLoader(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, Batch> batchOps, ChannelTopic topic) {
        this.factory = factory;
        this.batchOps = batchOps;
        this.topic = topic;
    }

    @PostConstruct
    void loadData(){
        factory.getReactiveConnection().serverCommands().flushAll().thenMany(
                Flux.just("A", "B", "C", "D")
                    .map(b->new Batch(UUID.randomUUID().toString(), b, b.getBytes(), BatchStatus.OPEN))
                    .flatMap(b->batchOps.opsForValue().set(b.getCompositeId(), b)))
                .thenMany(batchOps.keys("*")
                    .flatMap(key -> batchOps.opsForValue().get(key)))
                .subscribe(System.out::println);
    }

//    @PostConstruct
//    void loadData(){
//        factory.getReactiveConnection().serverCommands().flushAll().thenMany(
//                Flux.just("A", "B", "C", "D")
//                    .map(b->new Batch(UUID.randomUUID().toString(), b, b.getBytes(), BatchStatus.OPEN))
//                    .flatMap(b->batchOps.opsForSet().add(b.getBatchStatus()+"", b)))
//                .thenMany(batchOps.keys("*")
//                    .flatMap(key -> batchOps.opsForSet().members(key)))
//                .subscribe(System.out::println);
//    }
}
