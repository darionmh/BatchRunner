package com.dhiggins.BatchRunner.controllers;

import com.dhiggins.BatchRunner.models.Job;
import com.dhiggins.BatchRunner.services.BatchService;
import com.dhiggins.BatchRunner.services.JobService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
public class JobController {

    private JobService jobService;
    private BatchService batchService;

    public JobController(JobService jobService, BatchService batchService) {
        this.jobService = jobService;
        this.batchService = batchService;
    }

    @GetMapping("/jobs")
    public Flux<Job> jobs(){
        System.out.println("JOBS");
        return jobService.getAllJobs();
    }

    @GetMapping("/jobs/{id}")
    public Mono<Job> jobWithId(@PathVariable String id){
        return jobService.getJobById(id);
    }

    @PostMapping("/jobs/submit")
    public Mono<Boolean> submitJobRequest(@RequestParam(required = false) String id, @RequestParam(required = false) String name){
        if(id != null && !id.isEmpty())
            return batchService.addBatchById(id);

        if(name != null && !name.isEmpty())
            return batchService.addBatchByName(name);

        return Mono.just(false);
    }
}
