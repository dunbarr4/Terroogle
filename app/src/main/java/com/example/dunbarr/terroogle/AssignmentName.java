package com.example.dunbarr.terroogle;

public class AssignmentName {

    private int number;
    private String objectId;

    public AssignmentName() {
    }

    public AssignmentName(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String toString(){
        return String.format("%d", getNumber());
    }

}
