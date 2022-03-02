package com.csci310.ticket_master;

public class SearchInformation {
    private String city;
    private String postCode;
    private String distance;
    private String distanceUnit;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String keyword;
    private String genre;
    private String page_num;
    private String page_size;

    public SearchInformation(String city, String postCode, String distance, String distanceUnit,
                             String startDate, String endDate, String startTime, String endTime, String keyword, String genre, String page_num,
                             String page_size){
        this.city = city;
        this.postCode = postCode;
        this.distance = distance;
        this.distanceUnit = distanceUnit;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.keyword = keyword;
        this.genre = genre;
        this.page_num = page_num;
        this.page_size = page_size;

    }

    public String getCity(){
        return  this.city;
    }

    public String getPostCode() {
        return this.postCode;
    }

    public String getDistance() {
        return this.distance;
    }

    public String getDistanceUnit() {
        return this.distanceUnit;
    }

    public String getStartDateTime() {
        if(startDate != null && startTime != null) {
            StringBuilder sb = new StringBuilder(this.startDate);
            sb.append("T");
            sb.append(this.startTime);
            sb.append("Z");
            return sb.toString();
        }
        else return null;
    }

    public String getEndDateTime() {
        if(endDate != null && endTime != null) {
            StringBuilder sb = new StringBuilder(this.endDate);
            sb.append("T");
            sb.append(this.endTime);
            sb.append("Z");
            return sb.toString();
        }
        else return null;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getPage_num() {
        return this.page_num;
    }

    public String getPage_size() {
        return this.page_size;
    }

    public String getStartEndTime(){
        String startDateTime = this.getStartDateTime();
        String endDateTime = this.getEndDateTime();
        if(this.getStartDateTime() != null) {
            StringBuilder sb = new StringBuilder(startDateTime);
            if(endDateTime != null) {
                sb.append(",");
                sb.append(endDateTime);
            }
            return sb.toString();
        }
        else return null;

    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
