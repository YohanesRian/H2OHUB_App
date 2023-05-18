package com.tanpanama.h2ohub.Data;

import java.time.Year;
import java.util.Date;

public class UserData {
    private String name;
    private Date date_of_birth; // dd/MM/yyyy
    private String gender; // M or F
    private int height; // in cm
    private int weight; // in kg
    private int activity_level;

    private String[] level = {};
    private String[] level_description = {};
    private int[] level_weight = {};

    public UserData(){

    }

    public void setName(String name){
        this.name = name;
    }

    public void setDate_of_birth(Date dob){
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

    public void setActivity_level(int activity_level){
        this.activity_level = activity_level;
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

    public Date getDate_of_birth(){
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
        int result = 0;
        int age = Year.now().getValue() - this.date_of_birth.getYear();
        if(this.gender.equalsIgnoreCase("male")){
            result = (int) ((10 * this.weight) + (6.25 * this.height) - (((5 * age) + 5) * this.level_weight[activity_level]));
        }
        else{
            result = (int) ((10 * this.weight) + (6.25 * this.height) - (((5 * age) - 161) * this.level_weight[activity_level]));
        }
        return result;
    }
}
