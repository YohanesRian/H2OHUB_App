package com.tanpanama.h2ohub.Handler;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;

public class dateHandler {
    private String[] list_month = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private String date; //yyyymmdd

    public dateHandler(){}

    public void setDate(String date) {
        this.date = date;
    }

    public void setDate(int date) {
        this.date = Integer.toString(date);
    }

    public void setDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        this.date = dateFormat.format(date);
    }

    public void setDateY_M_D(String date){
        try{
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dt = dateFormat.parse(date);
            dateFormat = new SimpleDateFormat("yyyyMMdd");
            setDate(dateFormat.format(dt));
        }
        catch (Exception E){
            setDate("00000000");
        }

    }

    public int getIntDate(){
        return Integer.parseInt(date);
    }

    public String getYMD(){
        return this.date;
    }

    public String getY_M_D(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(this.date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

    public int getDay(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(this.date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateFormat = new SimpleDateFormat("dd");
        int day = Integer.parseInt(dateFormat.format(date));
        return day;
    }

    public String getDayName(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(this.date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("EEE");
        return (sdf.format(date));
    }

    public String getD_M_Y(){
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = null;
        try {
            date = dateFormat.parse(this.date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(date);
    }

    public String getFancyDate(){
        String date = getY_M_D();
        String t[] = date.split("-");
        int month = Integer.parseInt(t[1]);
        String result = t[2] + " " + list_month[month - 1] + " " + t[0];
        return result;
    }

    public int getYearFromNow(){
        String date = getY_M_D();
        String t[] = date.split("-");
        int year = Integer.parseInt(t[0]);
        int month = Integer.parseInt(t[1]);
        int day = Integer.parseInt(t[2]);
        int gap = Period.between( LocalDate.of(year, month, day), LocalDate.now()).getYears();
        return gap;
    }


    public String getTimeNow(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        return dateFormat.format(date);
    }

}
