package com.axlor.predictionassistantwebapp.repository;

import com.axlor.predictionassistantwebapp.model.Snapshot;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SnapshotRepository extends CrudRepository<Snapshot, Integer> {

    @Query("select hashId from Snapshot ORDER BY timestamp DESC")
    List<Integer> getAllIds();

    @Query("select timestamp from Snapshot ORDER BY timestamp DESC")
    List<Long> getTimestamps();

    Snapshot findSnapshotByTimestamp(long timestamp);
}
