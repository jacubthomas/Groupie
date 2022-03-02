package com.csci310.ticket_master;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.URL;
import static org.assertj.core.api.Assertions.assertThat;

public class TicketMasterServiceTest {
    TicketMasterService tmService;
    @Before
    public void setUp() throws Exception {
        this.tmService = new TicketMasterService();

    }

    @Test
    public void testGetEventFromTicketmaster() {
        try {
            SearchInformation badInfo = new SearchInformation("a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "0", "10");
            URL incorrectUrl = this.tmService.prepareUrlForTicketMaster(badInfo);
            // incorrect input case
            int response_code = this.tmService.getEventFromTicketmaster(incorrectUrl).getStatusCodeValue();
            boolean result = response_code == 400 | response_code == 429;
            assertThat(result).isEqualTo(true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        try {
            // correct input case
            SearchInformation info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");
            URL correctUrl = this.tmService.prepareUrlForTicketMaster(info);
            ResponseEntity<String> response = this.tmService.getEventFromTicketmaster(correctUrl);
            int response_code = response.getStatusCodeValue();
            boolean result = response_code == 200 | response_code == 429;
            assertThat(result).isEqualTo(true);
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}