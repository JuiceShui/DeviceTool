package com.jojo.devicetool.ui.network;

import java.io.Serializable;

/**
 * Description: Jojo on 2019/3/28
 */
public class NetworkItem implements Serializable {
    private String url;
    private String lat;
    private String lon;
    private String location;
    private String country;
    private String shortName;
    private String sponsor;
    private String id;
    private String host;
    private double distance = 0;

    public NetworkItem(String url, String lat, String lon,
                       String location, String country,
                       String shortName, String sponsor, String id, String host) {
        this.url = url;
        this.lat = lat;
        this.lon = lon;
        this.location = location;
        this.country = country;
        this.shortName = shortName;
        this.sponsor = sponsor;
        this.id = id;
        this.host = host;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "NetworkItem{" +
                "url='" + url + '\'' +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                ", location='" + location + '\'' +
                ", country='" + country + '\'' +
                ", shortName='" + shortName + '\'' +
                ", sponsor='" + sponsor + '\'' +
                ", id='" + id + '\'' +
                ", host='" + host + '\'' +
                ", distance=" + distance +
                '}';
    }
}
