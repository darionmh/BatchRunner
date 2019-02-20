package com.dhiggins.BatchRunner.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Job {
    private String id;
    private String name;
    private String command;
    private String directory;
    private Integer capacity;
    private Boolean hasOutput;
    private String outputName;
    private String outputDir;
}
