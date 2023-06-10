package com.tanpanama.h2ohub.Data;

import com.tanpanama.h2ohub.Handler.dateHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
import java.util.Calendar;
import java.util.Date;

public class UserData {
    private String name;
    private int date_of_birth; //yyyyMMdd
    private String gender; // male or female
    private int height; // in cm
    private int weight; // in kg
    private int activity_level;

    private int limit;

    private String[] level = {"Sedentary", "Light Excercise", "Moderate Excercise", "Heavy Excercise", "Athelete"};
    private String[] level_description = {"Daily work or school", "Exercise once or twice in a while", "Exercise 3 to 5 a week", "Exercise everyday", "An athelete or have physical job"};
    private double[] level_weight = {1.200, 1.375, 1.550, 1.725, 1.9};

    public UserData(){

    }

    public void setName(String name){
        this.name = name;
    }

    public void setDate_of_birth(int dob){
        this.date_of_birth = dob;
    }

    public void setGender(String mf){
        this.gender = mf;
    }

    public void setHeight(int height){
        this.height = height;
    }

    public void setWeight(int weight){
        this.weight = weight;
    }

    public void setLimit(int limit){this.limit = limit;}

    public void setActivity_level(String activity_level){
        for(int i = 0; i < level.length; i++){
            if(activity_level.equalsIgnoreCase(level[i])){
                this.activity_level = i;
                break;
            }
        }
    }

    public String getName(){
        return this.name;
    }

    public String getGender(){
        return this.gender;
    }

    public int getWeight(){
        return this.weight;
    }

    public int getHeight(){
        return this.height;
    }

    public int getDate_of_birth(){
        return this.date_of_birth;
    }

    public String getActivityLevel(){
        return this.level[activity_level];
    }

    public String[] getLevel(){
        return this.level;
    }

    public String[] getLevel_description(){
        return this.level_description;
    }

    public int getMaxDrinkWater(){
        dateHandler dh = new dateHandler();
        dh.setDate(Integer.toString(this.date_of_birth));
        int age = dh.getYearFromNow();
        int result = (int) Math.round(((10 * this.weight) + (6.25 * this.height) - (5 * age) - 161) * this.level_weight[activity_level]);
        if(this.gender.equalsIgnoreCase("male")){
            result = (int) Math.round(((10 * this.weight) + (6.25 * this.height) - (5 * age) + 5) * this.level_weight[activity_level]);
        }
        return result;
    }

    public int getTargetDrinkWater(){
        double drink = getMaxDrinkWater() * 0.8;
        int result = (int) drink;
        return result;
    }

    public int getLimit(){
        return this.limit;
    }
}
