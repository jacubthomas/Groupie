package com.csci310.ticket_master;

import ch.qos.logback.core.CoreConstants;
import com.csci310.db.entities.Event;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.net.URL;
import java.io.IOException;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketMasterTest {
    TicketMaster tm;
    TicketMasterCacheProxy tmProxy;
    @Before
    public void setUp() {
        this.tmProxy = new TicketMasterCacheProxy();
        this.tm = this.tmProxy;

    }

    @Test
    public void testSearchEvent() throws IOException {
        try {
            // incorrect input case
            SearchInformation badInfo = new SearchInformation("a", "a", "a", "a", "a", "a","a", "a", "a", "a", "0", "10");
            int response_code = this.tm.searchEvent(badInfo).getStatusCodeValue();
            boolean result = response_code == 400 || response_code == 429;
            assertThat(result).isEqualTo(true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        try {
            // correct input case
            SearchInformation info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");
            int response_code = this.tm.searchEvent(info).getStatusCodeValue();
            boolean result = response_code == 200 || response_code == 429;
            assertThat(result).isEqualTo(true);
            // a case when it uses cache data
            response_code = this.tm.searchEvent(info).getStatusCodeValue();
            result = response_code == 200 || response_code == 429;
            assertThat(result).isEqualTo(true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void testPrepareUrlForTicketMaster() throws IOException{
        SearchInformation info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");
        URL url = this.tm.prepareUrlForTicketMaster(info);
        assertThat(url.toString()).isEqualTo("https://app.ticketmaster.com/discovery/v2/events.json?city=Los Angeles&postalCode=90015&radius=1000&unit=miles&startEndDateTime=2021-10-04T17:00:00Z,2021-12-03T02:30:00Z&keyword=rock&genre=music&page=0&size=10&sort=relevance,asc&apikey=yw0YTQ03t4ZAmXkfEa5SW07IpYjmiiiv");
        // single requirement inputted case
        SearchInformation single_info = new SearchInformation(null, "90015", null, null, null, null, null, null, null, null, null, null);
        url = this.tm.prepareUrlForTicketMaster(single_info);
        assertThat(url.toString()).isEqualTo("https://app.ticketmaster.com/discovery/v2/events.json?postalCode=90015&size=10&sort=relevance,asc&apikey=yw0YTQ03t4ZAmXkfEa5SW07IpYjmiiiv");

    }

    @After
    public void tearDown(){
        this.tmProxy.clearCache();
        assertThat(this.tmProxy.getCacheSize()).isEqualTo(0);
    }


}
