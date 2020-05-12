package com.axlor.predictionassistantrest.controller.rest;

import com.axlor.predictionassistantrest.exception.MarketNotFoundException;
import com.axlor.predictionassistantrest.exception.NoSnapshotsInDatabaseException;
import com.axlor.predictionassistantrest.model.Market;
import com.axlor.predictionassistantrest.model.mini.MarketMini;
import com.axlor.predictionassistantrest.service.MarketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * A SpringMVC RestController that handles HTTP GET requests made to ../markets/..
 */
@RequestMapping("/api")
@RestController
public class MarketController {

    /**
     * The MarketService object that is handled and injected by Spring.
     */
    @Autowired
    MarketService marketService;

    /**
     * Handles the GET request made to /markets/
     *
     * @return A status 200 Response with a body that is the JSON representation of a list of Markets. Not ALL Market data is shown.
     * @throws NoSnapshotsInDatabaseException Thrown when no Snapshot exists in the database. With no Snapshots there are no Markets to get.
     */
    @GetMapping("/markets")
    public ResponseEntity<List<MarketMini>> getMarkets() throws NoSnapshotsInDatabaseException {
        List<MarketMini> marketMinis = marketService.getMarketsList();

        return new ResponseEntity<>(marketMinis, HttpStatus.OK);
    }

    /**
     * Gets market history by non unique id.
     *
     * @param marketId the non-unique market id given to the Market by PredictIt
     * @return A status 200 Response with a body of a JSON formatted list representing a List<Market> containing all historical snapshots/instances of that specific Market
     * @throws MarketNotFoundException Thrown when a Market with marketId is not found in the database.
     * @throws NumberFormatException Thrown when the marketId path parameter is not a valid number.
     */
    @GetMapping("/markets/{marketId}")
    public ResponseEntity<List<Market>> getMarketHistoryByNonUniqueId(@PathVariable String marketId) throws MarketNotFoundException {
        try {
            int nonUniqueId = Integer.parseInt(marketId);
            List<Market> marketHistory = marketService.getMarketHistoryByNonUniqueId(nonUniqueId);
            Collections.reverse(marketHistory); //I have the service reverse the list so that it is timestamp descending, most current market snapshot is first, oldest is last in list.

            return new ResponseEntity<>(marketHistory, HttpStatus.OK);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException("Market Id: '" + marketId + "' is not a number. Expected a valid id number for marketId at '/markets/{marketId}");
        }
    }

    /**
     * Gets latest market info for Market with non-unique marketId given by PredictIt.
     *
     * @param marketId the non-unique market id given to the Market by PredictIt
     * @return A status 200 Response with a body of the JSON representation of the most recent Market instance with this id.
     * @throws NoSnapshotsInDatabaseException If there are no Snapshots in the database, then there can be no latest of the requested Market.
     * @throws MarketNotFoundException        Thrown when a Market with marketId is not found in the database.
     */
    @GetMapping("/markets/{marketId}/latest")
    public ResponseEntity<Market> getLatestMarketInfo(@PathVariable String marketId) throws NoSnapshotsInDatabaseException, MarketNotFoundException {

        try{
            int nonUniqueId = Integer.parseInt(marketId);
            Market market = marketService.getLatestMarketInfo(nonUniqueId);
            return new ResponseEntity<>(market, HttpStatus.OK);
        }catch(NumberFormatException nfe){
            throw new NumberFormatException("Market Id: '" + marketId + "' is not a number. Expected a valid id number for marketId at '/markets/{marketId}");
        }
    }

}
