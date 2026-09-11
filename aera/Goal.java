package com.example.activitydasbord.models;

public class Goal {
    private int id;
    private String description;
    private int targetPercentage;
    private int progressPercentage;
    private String period;
    private String category;
    private boolean isCompleted;

    public Goal(int id, String description, int targetPercentage, int progressPercentage, String period, String category, boolean isCompleted) {
        this.id = id;
        this.description = description;
        this.targetPercentage = targetPercentage;
        this.progressPercentage = progressPercentage;
        this.period = period;
        this.category = category;
        this.isCompleted = isCompleted;
    }

    public int getId() { return id; }
    public String getDescription() { return description; }
    public int getTargetPercentage() { return targetPercentage; }
    public int getProgressPercentage() { return progressPercentage; }
    public String getPeriod() { return period; }
    public String getCategory() { return category; }
    public boolean isCompleted() { return isCompleted; }
}