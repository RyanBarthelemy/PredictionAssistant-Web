package com.axlor.predictionassistantrest.controller.web;

import com.axlor.predictionassistantrest.exception.NoSnapshotsInDatabaseException;
import com.axlor.predictionassistantrest.exception.SnapshotCountMismatchException;
import com.axlor.predictionassistantrest.exception.SnapshotNotFoundException;
import com.axlor.predictionassistantrest.model.Snapshot;
import com.axlor.predictionassistantrest.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.websocket.server.PathParam;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class SnapshotWebController {

    @Autowired
    SnapshotService snapshotService;

    @RequestMapping("/snapshots")
    public String getSnapshots(Model model){
        try {
            model.addAttribute("snapshots", snapshotService.getAllSnapshots_mini()) ;
        } catch (SnapshotCountMismatchException | NoSnapshotsInDatabaseException e) {
            return "errorPage";
        }
        return "snapshots";
    }

    @RequestMapping("/snapshots/{hashId}")
    public String getSnapshotInfo(@PathVariable("hashId") String hashId, Model model){
        try{
            Integer hashIdParsed = Integer.valueOf(hashId);
            Snapshot snapshot = snapshotService.getSnapshot(hashIdParsed);
            String timestampToDisplay = new SimpleDateFormat("M/d/yyyy hh:mm aa").format(new Date(snapshot.getTimestamp()));

            model.addAttribute("hashId", snapshot.getHashId());
            model.addAttribute("timestampToDisplay", timestampToDisplay);
            model.addAttribute("marketsList", snapshot.getMarkets());

            return "snapshotInfo";

        }catch(NumberFormatException nfe){
            return "invalidSnapshotIdError";
        } catch (NoSnapshotsInDatabaseException | SnapshotNotFoundException e) {
            return "snapshotNotFound404Page";
        }


    }
}
