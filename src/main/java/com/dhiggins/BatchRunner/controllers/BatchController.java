package com.dhiggins.BatchRunner.controllers;


import com.dhiggins.BatchRunner.models.Batch;
import com.dhiggins.BatchRunner.services.BatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
public class BatchController {

    private BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("/batches/{id}")
    public Mono<Batch> batchById(@PathVariable String id){
        return batchService.getById(id);
    }

    @GetMapping("/batches")
    public Flux<Batch> batches(){
        return batchService.getAllBatches();
    }
}
