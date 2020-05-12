package com.axlor.predictionassistantrest.service;

import com.axlor.predictionassistantrest.exception.NoSnapshotsInDatabaseException;
import com.axlor.predictionassistantrest.exception.SnapshotCountMismatchException;
import com.axlor.predictionassistantrest.exception.SnapshotNotFoundException;
import com.axlor.predictionassistantrest.model.Snapshot;
import com.axlor.predictionassistantrest.model.mini.SnapshotMini;
import com.axlor.predictionassistantrest.repository.SnapshotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Handles requesting data from various repositories and organizing that data into a form the controllers require.
 */
@Service
public class SnapshotService {

    @Autowired
    SnapshotRepository snapshotRepository;

    /**
     * Gets relevant info for user to understand what Markets are currently available.
     *
     * @return Returns a List of SnapshotMini objects that contain info the user needs to understand what Snapshots there are and how to find them.
     * @throws SnapshotCountMismatchException A precaution that should never be thrown. Could theoretically be caused by retrieving data during a database update, but should never happen.
     * @throws NoSnapshotsInDatabaseException Thrown when no data is in database.
     */
    public List<SnapshotMini> getAllSnapshots_mini() throws SnapshotCountMismatchException, NoSnapshotsInDatabaseException {

        List<Integer> hashes = snapshotRepository.getAllIds();
        List<Long> timestamps = snapshotRepository.getTimestamps();

        if(hashes.size() != timestamps.size()){
            throw new SnapshotCountMismatchException();
        }
        if(hashes.isEmpty()){
            throw new NoSnapshotsInDatabaseException();
        }

        List<SnapshotMini> snapshotMinisList = new ArrayList<>();
        for (int i = 0; i < hashes.size(); i++) {
            snapshotMinisList.add( createSnapshotMini(hashes.get(i), timestamps.get(i)) );
        }
        return snapshotMinisList;

    }

    private SnapshotMini createSnapshotMini(Integer hashId, Long timestamp) {
        String href = "/api/snapshots/" + hashId;

        String timestampToDisplay = new SimpleDateFormat("M/d/yyyy hh:mm aa").format(new Date(timestamp));
        return new SnapshotMini(hashId, timestampToDisplay, href);

    }

    /**
     * Gets snapshot.
     *
     * @param hashId The primary key of a Snapshot provided by ../snapshots uri
     * @return A unique Snapshot object with hashId of the input parameter.
     * @throws SnapshotNotFoundException      Thrown when Snapshot with parameter hashId cannot be found.
     * @throws NoSnapshotsInDatabaseException Thrown when no data is in database.
     */
    public Snapshot getSnapshot(Integer hashId) throws SnapshotNotFoundException, NoSnapshotsInDatabaseException {
        if(snapshotRepository.count()==0){
            throw new NoSnapshotsInDatabaseException();
        }

        /*
        For whatever reason snapshotRepository.findById(hashId) is getting wayyy too many Markets in its List<Market>
        Many many duplicate markets. This code culls the repeats at some cost.
        TODO: Write a custom @Query in the repository and see if it works
        Note: ./snapshots/latest works correctly, but /snapshots/{hashId} that retrieves the exact same market will have tons of duplicate Markets in list...
        I think a custom @Query should solve this issue more elegantly.
        I don't think it is an ORM issue since other things are working correctly, I think the superclass findById just sucks for this somehow.
         */
        if(snapshotRepository.findById(hashId).isPresent()){
            Snapshot snapshot = snapshotRepository.findById(hashId).get();
            System.out.println("Number of markets = " + snapshot.getMarkets().size());
            //for some reason this isn't working correctly.
            for (int i=0; i< snapshot.getMarkets().size()-1; i++){
                if(snapshot.getMarkets().get(i).getMarketUniqueID().equals(snapshot.getMarkets().get(i + 1).getMarketUniqueID())){
                    snapshot.getMarkets().remove(i);
                    i--;
                }
            }
            System.out.println("number of markets after cull = " + snapshot.getMarkets().size());
            return snapshot;
        }
        throw new SnapshotNotFoundException(hashId);

    }

    /**
     * Gets latest Snapshot.
     *
     * @return the latest Snapshot
     * @throws NoSnapshotsInDatabaseException Thrown when no data is in database.
     */
    public Snapshot getLatestSnapshot() throws NoSnapshotsInDatabaseException {
        if(snapshotRepository.count()==0){
            throw new NoSnapshotsInDatabaseException();
        }
        long timestamp = snapshotRepository.getTimestamps().get(0);
        return snapshotRepository.findSnapshotByTimestamp(timestamp);
    }
}
