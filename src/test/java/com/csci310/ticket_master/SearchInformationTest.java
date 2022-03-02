package com.csci310.ticket_master;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SearchInformationTest {
    SearchInformation info;
    @Before
    public void setUp() throws Exception {
        this.info = new SearchInformation("Los Angeles", "90015", "1000", "miles", "2021-10-04", "2021-12-03", "17:00:00", "02:30:00","rock", "music", "0", "10");


    }

    @Test
    public void testGetCity() {
        Assert.assertEquals("Los Angeles", info.getCity());
    }

    @Test
    public void testGetPostCode() {
        Assert.assertEquals("90015", info.getPostCode());
    }

    @Test
    public void testGetDistance() {
        Assert.assertEquals("1000", info.getDistance());
    }

    @Test
    public void testGetDistanceUnit() {
        Assert.assertEquals("miles", info.getDistanceUnit());
    }

    @Test
    public void testGetStartDate(){
        Assert.assertEquals("2021-10-04", info.getStartDate());
    }

    @Test
    public void testGetEndDate(){
        Assert.assertEquals("2021-12-03", info.getEndDate());

    }

    @Test
    public void testGetStartTime(){
        Assert.assertEquals("17:00:00", info.getStartTime());
    }

    @Test
    public void testGetEndTime(){
        Assert.assertEquals("02:30:00", info.getEndTime());

    }

    @Test
    public void testGetStartDateTime() {
        Assert.assertEquals("2021-10-04T17:00:00Z", info.getStartDateTime());
    }

    @Test
    public void testGetEndDateTime() {
        Assert.assertEquals("2021-12-03T02:30:00Z", info.getEndDateTime());
    }

    @Test
    public void testGetStartEndTime() {
        Assert.assertEquals("2021-10-04T17:00:00Z,2021-12-03T02:30:00Z", info.getStartEndTime());
    }



    @Test
    public void testGetKeyword() {
        Assert.assertEquals("rock", info.getKeyword());
    }

    @Test
    public void testGetGenre() {
        Assert.assertEquals("music", info.getGenre());
    }

    @Test
    public void testGetPage_num() {
        Assert.assertEquals("0", info.getPage_num());
    }

    @Test
    public void testGetPage_size() {
        Assert.assertEquals("10", info.getPage_size());
    }
}