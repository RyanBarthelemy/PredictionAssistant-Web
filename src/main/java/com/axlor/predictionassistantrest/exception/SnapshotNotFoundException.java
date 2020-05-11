package com.axlor.predictionassistantrest.exception;

public class SnapshotNotFoundException extends Exception {

    public SnapshotNotFoundException(Integer hashId){
        super("Snapshot with id:" + hashId + " not found in database.\n");
    }
}
