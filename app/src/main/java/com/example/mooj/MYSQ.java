package com.example.mooj;


import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

class Mysql extends SQLiteOpenHelper {
    String dbname="mydb";
    Context context;
    public Mysql(Context context,String dbname) {
        super(context, dbname, null, 1);
        this.dbname=dbname;
        this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE 'trees' ('id' INTEGER,'name' TEXT,'ab' INTEGER,'lvl' INTEGER,'sons' INTEGER, 'prog' INTEGER,PRIMARY KEY('id'))";
        try {
            db.execSQL(query);
            //Toast.makeText(context,"تم انشاء قاعدة البيانات بنجاج",Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e){
            //Toast.makeText(context,"فشل انشاء قاعدة البيانات",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        String query="DROP TABLE 'trees'";
        try {
            db.execSQL(query);
            Toast.makeText(context,"تم الاسقاط",Toast.LENGTH_SHORT).show();
        }
        catch (SQLException e){
            Toast.makeText(context,"فشل الاسقاط",Toast.LENGTH_SHORT).show();
        }

    }


}