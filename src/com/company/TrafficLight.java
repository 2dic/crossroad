package com.company;

public class TrafficLight {
    private int id;
    private String objectType;
    private char condition;
    private int queue;

    TrafficLight(int id, String objectType, char condition, int queue){
        this.id=id;
        this.objectType=objectType;
        this.condition=condition;
        this.queue=queue;
    }
    TrafficLight(){}
    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCondition(char condition) {
        this.condition = condition;
    }

    public char getCondition() {
        return this.condition;
    }

    public void setQueue(int queue) {
        this.queue = queue;
    }

    public int getQueue() {
        return this.queue;
    }
    public String getInfoOfTL(){
        return getId() + "\t|\t"+objectType+"\t\t|"+getCondition()+"\t\t\t|"+getQueue()+"\n";
    }
}
