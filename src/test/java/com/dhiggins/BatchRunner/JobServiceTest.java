package com.dhiggins.BatchRunner;

import com.dhiggins.BatchRunner.models.Job;
import com.dhiggins.BatchRunner.models.builders.JobBuilder;
import com.dhiggins.BatchRunner.services.JobService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;

@WebFluxTest
public class JobServiceTest {

    private JobService jobService;
    private Map<String, Job> jobMap;

    @Before
    public void setup(){
        jobMap = new HashMap<>();
        jobMap.put("1", job("Test1", "1"));
        jobMap.put("2", job("Test2", "2"));

        jobService = Mockito.mock(JobService.class);
        Mockito.when(jobService.getAllJobs()).thenReturn(Flux.fromIterable(jobMap.values()));
        Mockito.when(jobService.getJobById(anyString())).then(inv -> {
            String key = inv.getArgument(0);
            if(jobMap.containsKey(key))
                return Mono.just(jobMap.get(key));

            return Mono.empty();
        });
    }

    @Test
    public void testGetAll(){
        StepVerifier.create(jobService.getAllJobs())
                .expectNext(job("Test1", "1"))
                .expectNext(job("Test2", "2"))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetJobByIdReturnsJob(){
        StepVerifier.create(jobService.getJobById("1"))
                .expectNext(job("Test1", "1"))
                .expectComplete()
                .verify();
    }

    @Test
    public void testGetJobByIdReturnsNull(){
        StepVerifier.create(jobService.getJobById("3"))
                .expectComplete()
                .verify();
    }

    Job job(String name, String id){
        return JobBuilder.aJob()
                .withName(name)
                .withId(id)
                .build();
    }
}
