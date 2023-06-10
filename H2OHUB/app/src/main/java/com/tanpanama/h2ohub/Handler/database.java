package com.tanpanama.h2ohub.Handler;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.tanpanama.h2ohub.Data.CupsData;
import com.tanpanama.h2ohub.Data.UserData;
import com.tanpanama.h2ohub.Data.DrinkData;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;

public class database extends SQLiteOpenHelper {
    private Context context;

    private static final int DATABASE_VERSION = 5;
    static final String DATABASE_NAME = "H2OHUB";

    final String[] table_name = {"user_data", "container_data", "drink_data"};
    final String[][] column_name ={{"name", "dob", "gender", "height", "weight", "activity_level", "drink_limit"},
                                    {"name", "empty_weight", "full_weight", "picture"},
                                    {"date", "total_drink", "maximum_drink", "target_drink", "last_drink"}};
    final String[][] data_type ={{" TEXT, ", " INTEGER, ", " TEXT, ", " INTEGER, ", " INTEGER, ", " TEXT, ", " INTEGER"},
                                    {" TEXT PRIMARY KEY, ", " INTEGER, ", " INTEGER, ", " BLOB"},
                                    {" INTEGER PRIMARY KEY, ", " INTEGER, ", " INTEGER, ", " INTEGER, ", " TEXT"}};

    public database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i = 0; i < table_name.length; i++){
            String sql = "CREATE TABLE " + table_name[i] + " (";
            String temp = new String();
            for(int j = 0; j < column_name[i].length; j++){
                temp += column_name[i][j] + data_type[i][j];
            }
            sql += temp;
            sql += ");";
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_name[0]);
        db.execSQL("DROP TABLE IF EXISTS " + table_name[1]);
        db.execSQL("DROP TABLE IF EXISTS " + table_name[2]);
        onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS " + table_name[0]);
        db.execSQL("DROP TABLE IF EXISTS " + table_name[1]);
        db.execSQL("DROP TABLE IF EXISTS " + table_name[2]);
        onCreate(db);
    }

    public boolean isNotRegistered() {
        String query = "SELECT * FROM " + table_name[0];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return (count == 0);
    }
    public boolean addUserData(UserData ud){
        SQLiteDatabase db = this.getWritableDatabase();
        int limit  = 0;

        ContentValues cv = new ContentValues();

        cv.put(column_name[0][0], ud.getName());
        cv.put(column_name[0][1], ud.getDate_of_birth());
        cv.put(column_name[0][2], ud.getGender());
        cv.put(column_name[0][3], ud.getHeight());
        cv.put(column_name[0][4], ud.getWeight());
        cv.put(column_name[0][5], ud.getActivityLevel());
        cv.put(column_name[0][6], limit);

        long result = db.insert(table_name[0], null, cv);
        db.close();

        return (result != -1);
    }

    public boolean updateUserData(String name, UserData ud){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(column_name[0][0], ud.getName());
        cv.put(column_name[0][1], ud.getDate_of_birth());
        cv.put(column_name[0][2], ud.getGender());
        cv.put(column_name[0][3], ud.getHeight());
        cv.put(column_name[0][4], ud.getWeight());
        cv.put(column_name[0][5], ud.getActivityLevel());
        cv.put(column_name[0][6], ud.getLimit());

        long result = db.update(table_name[0], cv, (column_name[0][0] + " = ?"), new String[]{name});
        db.close();

        return (result != -1);
    }

    public boolean updateDrinkDataTarget(int target){
        DrinkData dd = getUserDrink(getUserData());
        dd.setTarget_drink(target);
        return (updateUserDrink(dd));
    }
    public boolean updateDrinkDataLimit(int limit){
        DrinkData dd = getUserDrink(getUserData());
        dd.setMax_drink(limit);
        return (updateUserDrink(dd));
    }

    public UserData getUserData(){
        UserData ud = new UserData();
        String query = "SELECT * FROM " + table_name[0];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst() && cursor.getCount() > 0){
            ud.setName(cursor.getString(cursor.getColumnIndex(column_name[0][0])));
            dateHandler dh = new dateHandler();
            dh.setDate(cursor.getString(cursor.getColumnIndex(column_name[0][1])));
            ud.setDate_of_birth(dh.getIntDate());
            ud.setGender(cursor.getString(cursor.getColumnIndex(column_name[0][2])));
            ud.setHeight(cursor.getInt(cursor.getColumnIndex(column_name[0][3])));
            ud.setWeight(cursor.getInt(cursor.getColumnIndex(column_name[0][4])));
            ud.setActivity_level(cursor.getString(cursor.getColumnIndex(column_name[0][5])));
            ud.setLimit(cursor.getInt(cursor.getColumnIndex(column_name[0][6])));
        }
        db.close();
        cursor.close();
        return ud;
    }

    public boolean addUserDrink(DrinkData dd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_name[2][0], dd.getDate());
        cv.put(column_name[2][1], dd.getTotal_drink());
        cv.put(column_name[2][2], dd.getMax_drink());
        cv.put(column_name[2][3], dd.getTarget_drink());
        cv.put(column_name[2][4], dd.getLast_drink());

        long result = db.insert(table_name[2], null, cv);
        db.close();
        return (result != -1);
    }

    public DrinkData getUserDrink(UserData ud){
        DrinkData dd = new DrinkData();
        dateHandler dh = new dateHandler();

        dh.setDate(Calendar.getInstance().getTime());

        String query = "SELECT * FROM " + table_name[2] + " WHERE " + column_name[2][0] + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{dh.getYMD().toString()});
        if(cursor.getCount() == 0) {
            dd.setDate(dh.getIntDate());
            dd.setTotal_drink(0);
            if(ud.getLimit() == 0){
                dd.setTarget_drink(ud.getTargetDrinkWater());
                dd.setMax_drink(ud.getMaxDrinkWater());
            }
            else {
                dd.setTarget_drink(ud.getLimit());
                dd.setMax_drink(ud.getLimit());
            }
            dd.setLast_drink("00:00");
            addUserDrink(dd);
        }
        else{
            if(cursor.moveToFirst()) {
                dd.setDate(cursor.getInt(cursor.getColumnIndex(column_name[2][0])));
                dd.setTotal_drink(cursor.getInt(cursor.getColumnIndex(column_name[2][1])));
                dd.setMax_drink(cursor.getInt(cursor.getColumnIndex(column_name[2][2])));
                dd.setTarget_drink(cursor.getInt(cursor.getColumnIndex(column_name[2][3])));
                dd.setLast_drink(cursor.getString(cursor.getColumnIndex(column_name[2][4])));
            }
        }
        cursor.close();
        db.close();
        return dd;
    }

    public ArrayList<DrinkData> getReportUserDrink(int month, int year){
        String date_start = year + "-" + month +"-" + "01";
        String date_end = year + "-" + month +"-" + YearMonth.of(year, month).lengthOfMonth();
        ArrayList<DrinkData> aldd = new ArrayList<>();
        dateHandler dh = new dateHandler();
        dh.setDateY_M_D(date_start);
        int start = dh.getIntDate();
        dh.setDateY_M_D(date_end);
        int end = dh.getIntDate();

        String query = "SELECT * FROM " + table_name[2] + " WHERE " + column_name[2][0] + " >= ? AND " + column_name[2][0] + " <= ? ORDER BY "+ column_name[2][0] + " DESC";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(start), String.valueOf(end)});

        if(cursor.moveToFirst()){
            do{
                DrinkData dd = new DrinkData();
                dd.setDate(cursor.getInt(cursor.getColumnIndex(column_name[2][0])));
                dd.setTotal_drink(cursor.getInt(cursor.getColumnIndex(column_name[2][1])));
                dd.setMax_drink(cursor.getInt(cursor.getColumnIndex(column_name[2][2])));
                dd.setTarget_drink(cursor.getInt(cursor.getColumnIndex(column_name[2][3])));
                dd.setLast_drink(cursor.getString(cursor.getColumnIndex(column_name[2][4])));
                aldd.add(dd);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return aldd;
    }

    public boolean addTodayUserDrink(int drank){
        UserData ud = getUserData();
        DrinkData dd = getUserDrink(ud);
        dateHandler dh = new dateHandler();

        int total = dd.getTotal_drink();
        dd.setTotal_drink(total + drank);
        dd.setLast_drink(dh.getTimeNow());

        return updateUserDrink(dd);
    }

    public boolean updateUserDrink(DrinkData dd){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(column_name[2][0], dd.getDate());
        cv.put(column_name[2][1], dd.getTotal_drink());
        cv.put(column_name[2][2], dd.getMax_drink());
        cv.put(column_name[2][3], dd.getTarget_drink());
        cv.put(column_name[2][4], dd.getLast_drink());

        dateHandler dh = new dateHandler();
        dh.setDate(dd.getDate());

        long result = db.update(table_name[2], cv, (column_name[2][0] + " = ?"), new String[]{dh.getYMD()});
        db.close();

        return (result != -1);
    }

    public boolean isSameNameCups(String name){
        String query = "SELECT * FROM " + table_name[1] + " WHERE " + column_name[1][0] + " = ? ";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, new String[]{name.toString()});
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return (count == 0);
    }
    public boolean addCups(CupsData cd){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_name[1][0], cd.getName());
        cv.put(column_name[1][1], cd.getEmpty_weight());
        cv.put(column_name[1][2], cd.getFull_weight());
        cv.put(column_name[1][3], cd.getByteArrPicture());

        long result = db.insert(table_name[1], null, cv);
        db.close();
        return (result != -1);
    }

    public boolean deleteCups(CupsData cd){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(table_name[1], column_name[1][0]+"=?", new String[]{cd.getName()});
        return (result != -1);
    }

    public ArrayList<CupsData> getCups(){
        ArrayList<CupsData>  result = new ArrayList<>();
        String query = "SELECT * FROM " + table_name[1];
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                CupsData cd = new CupsData();
                cd.setName(cursor.getString(cursor.getColumnIndex(column_name[1][0])));
                cd.setEmpty_weight(cursor.getInt(cursor.getColumnIndex(column_name[1][1])));
                cd.setFull_weight(cursor.getInt(cursor.getColumnIndex(column_name[1][2])));
                cd.setPicture(cursor.getBlob(cursor.getColumnIndex(column_name[1][3])));
                result.add(cd);
            }while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

}
