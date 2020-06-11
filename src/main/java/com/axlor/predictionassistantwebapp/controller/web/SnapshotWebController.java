package com.axlor.predictionassistantwebapp.controller.web;

import com.axlor.predictionassistantwebapp.exception.NoSnapshotsInDatabaseException;
import com.axlor.predictionassistantwebapp.exception.SnapshotCountMismatchException;
import com.axlor.predictionassistantwebapp.exception.SnapshotNotFoundException;
import com.axlor.predictionassistantwebapp.model.Snapshot;
import com.axlor.predictionassistantwebapp.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
            return "error/snapshotNotFound404Page";
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
            return "error/invalidSnapshotIdError";
        } catch (NoSnapshotsInDatabaseException | SnapshotNotFoundException e) {
            return "error/snapshotNotFound404Page";
        }
    }
}
