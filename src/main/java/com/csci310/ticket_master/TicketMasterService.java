package com.csci310.ticket_master;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.ResponseEntity;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TicketMasterService extends TicketMaster{

    public ResponseEntity<String> getEventFromTicketmaster(URL ticketMasterUrl) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) ticketMasterUrl.openConnection();
        //get data from ticketmaster as json file format
        connection.setRequestMethod("GET");
        //request header
        connection.setRequestProperty("User-Agent", "Mozilla/5.0");
        int responseCode  = connection.getResponseCode();
        // 400 BAD REQUEST, incorrect inputs
        // PARSE ERROR MESSAGE
        StringBuilder sb = new StringBuilder();
        String line;
        if(responseCode == 400){
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(sb);
            return ResponseEntity.badRequest().body(json);
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        return ResponseEntity.ok(sb.toString());

    }
}