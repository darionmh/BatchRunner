package com.dhiggins.BatchRunner.configs;

import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.models.Job;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class OpsConfig {

    ObjectMapper objectMapper(){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        return objectMapper;
    }

    @Bean("BatchOps")
    ReactiveRedisOperations<String, Batch> batchOps(ReactiveRedisConnectionFactory factory){

        Jackson2JsonRedisSerializer<Batch> serializer = new Jackson2JsonRedisSerializer<>(Batch.class);
        serializer.setObjectMapper(objectMapper());

        RedisSerializationContext.RedisSerializationContextBuilder<String, Batch> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Batch> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean("JobOps")
    ReactiveRedisOperations<String, Job> jobOps(ReactiveRedisConnectionFactory factory){

        Jackson2JsonRedisSerializer<Job> serializer = new Jackson2JsonRedisSerializer<>(Job.class);
        serializer.setObjectMapper(objectMapper());

        RedisSerializationContext.RedisSerializationContextBuilder<String, Job> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Job> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }


    //Todo make an executor for each job
    @Bean("executor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();
        return executor;
    }
}
