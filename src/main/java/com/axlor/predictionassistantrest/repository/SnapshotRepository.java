package com.axlor.predictionassistantrest.repository;

import com.axlor.predictionassistantrest.model.Snapshot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnapshotRepository extends CrudRepository<Snapshot, Integer> {

    @Query("select hashId from Snapshot ORDER BY timestamp DESC")
    public List<Integer> getAllIds();

    @Query("select timestamp from Snapshot ORDER BY timestamp DESC")
    public List<Long> getTimestamps();

    public Snapshot findSnapshotByTimestamp(long timestamp);
}
