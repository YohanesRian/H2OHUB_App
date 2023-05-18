package com.tanpanama.h2ohub.LocalHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.tanpanama.h2ohub.Data.UserData;

public class database extends SQLiteOpenHelper {
    private Context context;

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "H2OHUB";

    final String[] table_name = {"user_data", "container_data", "drink_data"};
    final String[][] column_name ={{"name", "dob", "gender", "height", "weight", "activity_level"},
                                    {"name", "empty_weight", "full_weight", "picture"},
                                    {"date", "total_drink", "maximum_drink", "targer_drink", "last_drink"}};
    final String[][] data_type ={{" TEXT, ", " DATE, ", " TEXT, ", " INTEGER, ", " INTEGER, ", " TEXT"},
                                    {" TEXT, ", " INTEGER, ", " INTEGER, ", " BLOB"},
                                    {" DATE PRIMARY KEY, ", " INTEGER, ", " INTEGER, ", " INTEGER, ", " TIME"}};

    public database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for(int i = 0; i < table_name.length; i++){
            String sql = "CREATE TABLE " + table_name[i] + " (";
            for(int j = 0; j < column_name[i].length; j++){
                sql += column_name[i][j] + data_type[i][j];
            }
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

    Cursor getUserData(){
        String query = "SELECT * FROM " + table_name[0];
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    public boolean isNotRegistered() {
        return (getUserData().getCount() == 0);
    }
}
