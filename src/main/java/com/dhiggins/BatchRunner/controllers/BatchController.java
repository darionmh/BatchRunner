package com.dhiggins.BatchRunner.controllers;


import com.dhiggins.BatchRunner.services.BatchService;
import com.dhiggins.BatchRunner.models.Batch;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class BatchController {

    private BatchService batchService;

    public BatchController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping(value = "/batches/{id}")
    public Mono<Batch> batchById(@PathVariable String id){
        return batchService.getById(id);
    }

    @GetMapping("/batches")
    public Flux<Batch> batches(){
        return batchService.getAllBatches();
    }

    @GetMapping("/batches/messages")
    public Flux<Batch> batchMessages(){
        return batchService.getAllBatches();
    }

    @PostMapping("/batches/add")
    public Mono<Boolean> add(@RequestParam("xml") String xml){
        System.out.println("HIT---"+xml);
        return batchService.addBatch(xml);
    }

    @GetMapping("/queue")
    public Flux<Batch> queue(){
        return batchService.queue();
    }
}
