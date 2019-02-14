package com.dhiggins.BatchRunner.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Arrays;
import java.util.Objects;

public class Batch {
    private String id;
    private String inputXml;
    private byte[] outputPdf;
    private BatchStatus batchStatus;

    public Batch(String id, String inputXml, byte[] outputPdf, BatchStatus batchStatus) {
        this.id = id;
        this.inputXml = inputXml;
        this.outputPdf = outputPdf;
        this.batchStatus = batchStatus;
    }

    public Batch() {
    }

    public Batch(Batch other) {
        this.id = other.id;
        this.inputXml = other.inputXml;
        if(other.outputPdf != null)
            this.outputPdf = other.outputPdf.clone();
        this.batchStatus = other.batchStatus;
    }

    @JsonIgnore
    public String getCompositeId(){
        return batchStatus.name() + "-" + id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInputXml() {
        return inputXml;
    }

    public void setInputXml(String inputXml) {
        this.inputXml = inputXml;
    }

    public byte[] getOutputPdf() {
        return outputPdf;
    }

    public void setOutputPdf(byte[] outputPdf) {
        this.outputPdf = outputPdf;
    }

    @Override
    public String toString() {
        return "Batch{" +
                "id='" + id + '\'' +
                ", inputXml='" + inputXml + '\'' +
                ", outputPdf=" + Arrays.toString(outputPdf) +
                ", batchStatus=" + batchStatus +
                '}';
    }

    public BatchStatus getBatchStatus() {
        return batchStatus;
    }

    public void setBatchStatus(BatchStatus batchStatus) {
        this.batchStatus = batchStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Batch batch = (Batch) o;
        return batchStatus == batch.batchStatus &&
                Objects.equals(id, batch.id) &&
                Objects.equals(inputXml, batch.inputXml) &&
                Arrays.equals(outputPdf, batch.outputPdf);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, inputXml, batchStatus);
        result = 31 * result + Arrays.hashCode(outputPdf);
        return result;
    }

    public Batch clone(){
        return new Batch(this);
    }
}
