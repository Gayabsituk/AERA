package com.example.activitydasbord.models;

public class ActivityEntry {
    private int id;
    private String type;
    private String subType;
    private double value;
    private double co2e;
    private String timestamp;

    public ActivityEntry(int id, String type, String subType, double value, double co2e, String timestamp) {
        this.id = id;
        this.type = type;
        this.subType = subType;
        this.value = value;
        this.co2e = co2e;
        this.timestamp = timestamp;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public String getSubType() { return subType; }
    public double getValue() { return value; }
    public double getCo2e() { return co2e; }
    public String getTimestamp() { return timestamp; }
}