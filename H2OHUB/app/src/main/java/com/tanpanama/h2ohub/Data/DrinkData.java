package com.tanpanama.h2ohub.Data;

public class DrinkData {
    private int date;
    private int total_drink;
    private int max_drink;
    private int target_drink;
    private String last_drink;

    public DrinkData(){}

    public void setDate(int date) {
        this.date = date;
    }

    public void setTotal_drink(int total_drink) {
        this.total_drink = total_drink;
    }

    public void setMax_drink(int max_drink) {
        this.max_drink = max_drink;
    }

    public void setTarget_drink(int target_drink) {
        this.target_drink = target_drink;
    }

    public void setLast_drink(String last_drink) {
        this.last_drink = last_drink;
    }

    public int getDate() {
        return date;
    }

    public int getTotal_drink() {
        return total_drink;
    }

    public int getMax_drink() {
        return max_drink;
    }

    public int getTarget_drink() {
        return target_drink;
    }

    public String getLast_drink() {
        return last_drink;
    }
}
