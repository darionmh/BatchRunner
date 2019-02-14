package com.dhiggins.BatchRunner.configs;

import com.dhiggins.BatchRunner.models.Batch;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.ReactiveRedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class RedisConfig {

    @Bean
    ChannelTopic channelTopic(){
        return new ChannelTopic("batches:queue");
    }

    @Bean
    ReactiveRedisOperations<String, Batch> redisOperations(ReactiveRedisConnectionFactory factory){

        Jackson2JsonRedisSerializer<Batch> serializer = new Jackson2JsonRedisSerializer<>(Batch.class);

        RedisSerializationContext.RedisSerializationContextBuilder<String, Batch> builder =
                RedisSerializationContext.newSerializationContext(new StringRedisSerializer());

        RedisSerializationContext<String, Batch> context = builder.value(serializer).build();

        return new ReactiveRedisTemplate<>(factory, context);
    }

    @Bean
    ReactiveRedisMessageListenerContainer redisMessageListenerContainer(ReactiveRedisConnectionFactory factory){
        ReactiveRedisMessageListenerContainer container = new ReactiveRedisMessageListenerContainer(factory);
        container.receive(channelTopic());

        return container;
    }

    @Bean("executor")
    public TaskExecutor threadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(1);
        executor.setMaxPoolSize(1);
        executor.initialize();
        return executor;
    }
}
