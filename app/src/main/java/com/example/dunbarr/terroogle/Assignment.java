package com.example.dunbarr.terroogle;

public class Assignment {
    private int pointsE, pointsP;
    private String name, objectId;

    public Assignment() {
    }

    public Assignment(String name, int pointsE) {
        this.pointsE = pointsE;
        this.name = name;
        this.pointsP = 5;
    }

    public void setPointsP(int pointsP) {
        this.pointsP = pointsP;
    }

    public int getPointsP() {
        return pointsP;
    }

    public void setPointsE(int pointsE) {
        this.pointsE = pointsE;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getPointsE() {
        return this.pointsE;
    }

    public double getPercent(){
        return (getPointsE() / 5.0) * 100;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    @Override
    public String toString() {
        return String.format("%s \n %d/5 \n %.2f%%", name, getPointsE(), getPercent());
    }
}


