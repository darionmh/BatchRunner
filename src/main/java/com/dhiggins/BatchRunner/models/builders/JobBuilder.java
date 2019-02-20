package com.dhiggins.BatchRunner.models.builders;

import com.dhiggins.BatchRunner.models.Job;

public final class JobBuilder {
    private String id;
    private String name;
    private String command;
    private String directory;
    private Integer capacity;
    private Boolean hasOutput;
    private String outputName;
    private String outputDir;

    private JobBuilder() {
    }

    public static JobBuilder aJob() {
        return new JobBuilder();
    }

    public JobBuilder withId(String id) {
        this.id = id;
        return this;
    }

    public JobBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public JobBuilder withCommand(String command) {
        this.command = command;
        return this;
    }

    public JobBuilder withDirectory(String directory) {
        this.directory = directory;
        return this;
    }

    public JobBuilder withCapacity(Integer capacity) {
        this.capacity = capacity;
        return this;
    }

    public JobBuilder withHasOutput(Boolean hasOutput) {
        this.hasOutput = hasOutput;
        return this;
    }

    public JobBuilder withOutputName(String outputName) {
        this.outputName = outputName;
        return this;
    }

    public JobBuilder withOutputDir(String outputDir) {
        this.outputDir = outputDir;
        return this;
    }

    public Job build() {
        Job job = new Job();
        job.setId(id);
        job.setName(name);
        job.setCommand(command);
        job.setDirectory(directory);
        job.setCapacity(capacity);
        job.setHasOutput(hasOutput);
        job.setOutputName(outputName);
        job.setOutputDir(outputDir);
        return job;
    }
}
