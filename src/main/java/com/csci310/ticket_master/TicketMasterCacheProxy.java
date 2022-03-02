package com.csci310.ticket_master;

import org.springframework.http.ResponseEntity;
import java.util.HashMap;
import java.io.IOException;
import java.net.URL;

public class TicketMasterCacheProxy extends TicketMaster{
    private TicketMaster ticketMasterService;
    private HashMap<URL, ResponseEntity<String>> cacheSearches = new HashMap<URL, ResponseEntity<String>>();

    public TicketMasterCacheProxy(){
        this.ticketMasterService = new TicketMasterService();
    }

    public ResponseEntity<String> getEventFromTicketmaster(URL ticketMasterUrl) throws IOException {
        // check if current URL get request has been made before
        if(this.cacheSearches.containsKey(ticketMasterUrl)){
            return this.cacheSearches.get(ticketMasterUrl);
        }
        else{
            ResponseEntity<String> response = this.ticketMasterService.getEventFromTicketmaster(ticketMasterUrl);
            this.cacheSearches.put(ticketMasterUrl, response);
            return response;
        }
    }

    public void clearCache(){
        this.cacheSearches.clear();
    }

    public int getCacheSize(){
        return this.cacheSearches.size();
    }
}
