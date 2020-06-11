package com.axlor.predictionassistantwebapp.controller.web;

import com.axlor.predictionassistantwebapp.exception.MarketNotFoundException;
import com.axlor.predictionassistantwebapp.exception.NoSnapshotsInDatabaseException;
import com.axlor.predictionassistantwebapp.model.Contract;
import com.axlor.predictionassistantwebapp.model.Market;
import com.axlor.predictionassistantwebapp.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class MarketWebController {

    @Autowired
    MarketService marketService;

    @RequestMapping("/markets/{id}")
    public String getSnapshotInfo(@PathVariable("id") String id, Model model){
        try{
            int nonUniqueId = Integer.parseInt(id);
            Market market = marketService.getLatestMarketInfo(nonUniqueId);
            //could just add the market object but I think this makes it cleaner in the html
            model.addAttribute("nonUniqueId", nonUniqueId);
            model.addAttribute("marketName", market.getName());
            model.addAttribute("contractsList", market.getContracts());
            model.addAttribute("marketUrl", market.getUrl());

            return "marketInfo";
        }catch(NumberFormatException nfe){
            return "error/invalidMarketIdError";
        } catch (NoSnapshotsInDatabaseException | MarketNotFoundException e) {
           return "error/marketNotFound404Page";
        }
    }

    @RequestMapping("/markets/{marketId}/contracts/{contractId}")
    public String getContractHistory(@PathVariable("marketId") String marketId, @PathVariable("contractId") String contractId, Model model){
        try{
            int nonUniqueMarketId = Integer.parseInt(marketId);
            int nonUniqueContractId = Integer.parseInt(contractId);

            List<Market> marketHistory = marketService.getMarketHistoryByNonUniqueId(nonUniqueMarketId);
            List<Contract> contractHistoryList = new ArrayList<Contract>();
            for (int i = 0; i < marketHistory.size(); i++) {
                for (int j = 0; j < marketHistory.get(i).getContracts().size(); j++) {
                    if(marketHistory.get(i).getContracts().get(j).getId() == nonUniqueContractId){
                        contractHistoryList.add(marketHistory.get(i).getContracts().get(j));
                    }
                }
            }
            if(contractHistoryList==null || contractHistoryList.isEmpty()){
                return "error/marketNotFound404Page";
            }

            model.addAttribute("contractHistoryList", contractHistoryList);
            model.addAttribute("contractId", nonUniqueContractId);
            model.addAttribute("marketName", marketHistory.get(0).getName());
            model.addAttribute("contractName", contractHistoryList.get(0).getName());
            model.addAttribute("marketHistoryList", marketHistory);

            return "contractHistory";

        }catch(NumberFormatException nfe){
            return "error/invalidMarketIdError";
        } catch (MarketNotFoundException e) {
            return "error/marketNotFound404Page";
        }
    }

}
