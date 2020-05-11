package com.axlor.predictionassistantrest.controller;

import com.axlor.predictionassistantrest.exception.NoSnapshotsInDatabaseException;
import com.axlor.predictionassistantrest.exception.SnapshotCountMismatchException;
import com.axlor.predictionassistantrest.exception.SnapshotNotFoundException;
import com.axlor.predictionassistantrest.model.Snapshot;
import com.axlor.predictionassistantrest.model.mini.SnapshotMini;
import com.axlor.predictionassistantrest.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

//exception handing help here: https://dzone.com/articles/spring-rest-service-exception-handling-1

/**
 * A SpringMVC RestController that handles HTTP GET requests made to ../snapshots/..
 */
@RequestMapping("/api")
@RestController
public class SnapshotController {

    /**
     * The SnapshotService object handled and injected by Spring.
     */
    @Autowired
    SnapshotService snapshotService;

    /**
     * Gets basic info on ALL Snapshots in the database.
     *
     * @return HTTP Response entity of status 200 that contains the JSON representation of a List<SnapshotMini>, relevant Snapshot data without overly high cost.
     * @throws SnapshotCountMismatchException Thrown when number of Snapshot id's in database does not match number of Snapshot timestamps. Should never be thrown but is here just in case of synchronicity issues.
     * @throws NoSnapshotsInDatabaseException Thrown when there are no Snapshots in the database, so there is no data to display. Rather than an empty page, we see an error page explaining this.
     */
    @GetMapping("/snapshots")
    public ResponseEntity<List<SnapshotMini>> getAllSnapshots_mini() throws SnapshotCountMismatchException, NoSnapshotsInDatabaseException {
        return new ResponseEntity<>(snapshotService.getAllSnapshots_mini(), HttpStatus.OK);
    }

    /**
     * Gets the Snapshot from the database with primary key hashId.
     *
     * @param hashId The unique id of the requested Snapshot
     * @return An HTTP Response entity with status 200 and a body
     * @throws SnapshotNotFoundException      Thrown when hashId from uri does not match any Snapshot primary key in the database.
     * @throws NoSnapshotsInDatabaseException Thrown when there are no Snapshots in the database, so there is no data to display. Rather than an empty page, we see an error page explaining this.
     */
    @GetMapping("/snapshots/{hashId}")
    public ResponseEntity<Snapshot> getSnapshot(@PathVariable("hashId") String hashId) throws SnapshotNotFoundException, NoSnapshotsInDatabaseException {
            try {
                Integer id = Integer.parseInt(hashId);
                return new ResponseEntity<>(snapshotService.getSnapshot(id), HttpStatus.OK);
            }catch(NumberFormatException nfe){
                throw new NumberFormatException("hashId: '" + hashId + "' is not a number, cannot be used as an ID for a Snapshot.");
            }
    }

    /**
     * Gets the JSON representation of the most recent Snapshot in the database.
     *
     * @return HTTP Response entity of status 200 that contains the JSON representation of the Snapshot with the most recent timestamp
     * @throws NoSnapshotsInDatabaseException Thrown when there are no Snapshots in the database, so there is no data to display. Rather than an empty page, we see an error page explaining this.
     */
    @GetMapping("/snapshots/latest")
    public ResponseEntity<Snapshot> getLatestSnapshot() throws NoSnapshotsInDatabaseException {
        return new ResponseEntity<>(snapshotService.getLatestSnapshot(), HttpStatus.OK);
    }
}
