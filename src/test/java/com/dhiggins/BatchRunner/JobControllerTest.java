package com.dhiggins.BatchRunner;

import com.dhiggins.BatchRunner.models.Job;
import com.dhiggins.BatchRunner.models.builders.JobBuilder;
import com.dhiggins.BatchRunner.services.JobService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@WebFluxTest
@RunWith(SpringRunner.class)
public class JobControllerTest {

    JobService jobService;
    WebTestClient client;

    @Before
    public void setup(){
        jobService = Mockito.mock(JobService.class);
        Mockito.when(jobService.getAllJobs()).thenReturn(Flux.just(job("Test1"), job("Test2")));

        client = WebTestClient.bindToServer().baseUrl("http://localhost:8080").build();
    }

    @Test
    public void testGetAll(){
        client.get().uri("/jobs")
                .accept(MediaType.ALL)
                .exchange()
                .expectBody(new ParameterizedTypeReference<Flux<Job>>() {})
                .isEqualTo(Flux.just(job("Test1"), job("Test2")));

//        StepVerifier.create(jobService.getAllJobs())
//                .expectNext(job("Test1"))
//                .expectNext(job("Test2"))
//                .expectComplete()
//                .verify();
    }

    Job job(String name){
        return JobBuilder.aJob()
                .withName(name)
                .build();
    }
}
