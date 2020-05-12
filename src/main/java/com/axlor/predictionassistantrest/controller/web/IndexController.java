package com.axlor.predictionassistantrest.controller.web;

import com.axlor.predictionassistantrest.exception.NoSnapshotsInDatabaseException;
import com.axlor.predictionassistantrest.exception.SnapshotCountMismatchException;
import com.axlor.predictionassistantrest.service.SnapshotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    SnapshotService snapshotService;

    @RequestMapping("/")
    public String getHomepage(Model model){

        try {
            model.addAttribute("snapshots", snapshotService.getAllSnapshots_mini()) ;
        } catch (SnapshotCountMismatchException | NoSnapshotsInDatabaseException e) {
            return "errorPage";
        }
        return "index";
    }
}
