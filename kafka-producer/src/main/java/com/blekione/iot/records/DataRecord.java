package com.blekione.iot.records;

public class DataRecord {

    private final String id;
    private final int value;
    private final int you = 125;

    public DataRecord(String id, int value) {
        this.id = id;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public int getValue() {
        return value;
    }

    public int getYou() {
        return you;
    }

}
