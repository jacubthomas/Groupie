package com.csci310.ticket_master;

import com.csci310.db.entities.Event;
import org.springframework.http.ResponseEntity;
import org.json.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public abstract class TicketMaster {
    public ResponseEntity<String> searchEvent(SearchInformation info) throws IOException{
        URL url = this.prepareUrlForTicketMaster(info);
        return this.getEventFromTicketmaster(url);
    }

    public URL prepareUrlForTicketMaster(SearchInformation info) throws IOException {
        // prepare url to send to ticketmaster
        StringBuilder sb = new StringBuilder("https://app.ticketmaster.com/discovery/v2/events.json?");
        String city = info.getCity();
        boolean isFirstInput = true;
        if(city != null){
            sb.append("city=").append(city);
            isFirstInput = false;
        }
        String postCode = info.getPostCode();
        if(postCode != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("postalCode=").append(postCode);
            isFirstInput = false;
        }
        String distance = info.getDistance();
        if(distance != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("radius=").append(distance);
            isFirstInput = false;
        }
        String distanceUnit = info.getDistanceUnit();
        if(distanceUnit != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("unit=").append(distanceUnit);
            isFirstInput = false;
        }
        String startEndTime = info.getStartEndTime();
        if(startEndTime != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("startEndDateTime=").append(startEndTime);
            isFirstInput = false;
        }
        String keyword = info.getKeyword();
        if(keyword != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("keyword=").append(keyword);
            isFirstInput = false;
        }
        String genre = info.getGenre();
        if(genre != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("genre=").append(genre);
            isFirstInput = false;
        }
        String page_num = info.getPage_num();
        if(page_num != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("page=").append(page_num);
            isFirstInput = false;
        }
        String page_size = info.getPage_size();;
        if(page_size != null){
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("size=").append(page_size);
            isFirstInput = false;
        }
        else{
            // default number of events in one page
            if(!isFirstInput){
                sb.append("&");
            }
            sb.append("size=10");
            isFirstInput = false;
        }
        // sort by relevance and attach api key
        sb.append("&sort=relevance,asc&apikey=yw0YTQ03t4ZAmXkfEa5SW07IpYjmiiiv");
        String url = sb.toString();
        URL ticketMasterUrl = new URL(url);
        return ticketMasterUrl;
    }

    public abstract ResponseEntity<String> getEventFromTicketmaster(URL ticketMasterUrl) throws IOException;

    public ArrayList<Event> parseJsonToEvents(String ticketmasterResponse){
        // parse String json to Json Objects, and select Json arrays in the Json Object
        JSONObject response = new JSONObject(ticketmasterResponse);
        JSONObject embedded = response.getJSONObject("_embedded");
        JSONArray jsonEvents = embedded.getJSONArray("events");
        // create events
        ArrayList<Event> events = new ArrayList<>();
        for(int i = 0; i < jsonEvents.length(); i++){
            Event newEvent = new Event();
            events.add(newEvent);
        }
        return events;
    }


}
