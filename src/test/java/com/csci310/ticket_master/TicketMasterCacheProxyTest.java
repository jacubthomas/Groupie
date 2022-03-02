package com.csci310.ticket_master;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class TicketMasterCacheProxyTest {
    TicketMasterCacheProxy tmService;
    @Before
    public void setUp() {
        this.tmService = new TicketMasterCacheProxy();

    }
    @Test
    public void testGetEventFromTicketmaster() throws IOException {
        try {
            SearchInformation badInfo = new SearchInformation("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "0", "10");
            URL incorrectUrl = this.tmService.prepareUrlForTicketMaster(badInfo);
            // incorrect input case
            assertThat(this.tmService.getEventFromTicketmaster(incorrectUrl).getStatusCodeValue()).isEqualTo(400);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        try {
            // correct input case
            SearchInformation info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");
            URL correctUrl = this.tmService.prepareUrlForTicketMaster(info);
            int response_code = this.tmService.getEventFromTicketmaster(correctUrl).getStatusCodeValue();
            boolean result = response_code == 200 | response_code == 429;
            assertThat(result).isEqualTo(true);
            // case when request has been saved to cache
            int prev_size = this.tmService.getCacheSize();
            assertThat(this.tmService.getEventFromTicketmaster(correctUrl).getStatusCodeValue()).isEqualTo(200);
            assertThat(this.tmService.getCacheSize()).isEqualTo(prev_size);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    @Test
    public void testClearCache() {
        try {
            SearchInformation info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");
            this.tmService.searchEvent(info);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        this.tmService.clearCache();
        assertThat(this.tmService.getCacheSize()).isEqualTo(0);

    }

    @Test
    public void testGetCacheSize() throws IOException {
        this.tmService.clearCache();
        try {
            SearchInformation info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");
            this.tmService.searchEvent(info);
            assertThat(this.tmService.getCacheSize()).isEqualTo(1);
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }
}