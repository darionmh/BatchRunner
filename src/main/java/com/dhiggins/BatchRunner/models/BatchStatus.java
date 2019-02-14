package com.dhiggins.BatchRunner.models;

public enum BatchStatus {
    OPEN,
    QUEUED,
    WAITING,
    RUNNING,
    FAILED,
    CANCELED,
    COMPLETE;
}
