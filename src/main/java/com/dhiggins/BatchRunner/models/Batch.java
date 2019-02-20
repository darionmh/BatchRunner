package com.dhiggins.BatchRunner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Batch {
    private String id;
    private LocalDateTime ts;
    private String job;
//    private Job job;
    private BatchStatus batchStatus;

    @JsonIgnore
    public String getCompositeId(){
        return batchStatus+"-"+id;
    }
}
