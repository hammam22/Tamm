package com.example.mooj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class SortingItems extends AppCompatActivity {

    int id;
    Mysql mysql=new Mysql(this,"mydb");
    ArrayList<Ashjar> allashjar=new ArrayList<Ashjar>();
    AdaptSort adaptSort;
    DragListView lvactsort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sorting_items);
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        readmine();
        adaptSort = new AdaptSort(allashjar,this);
        lvactsort=(DragListView) findViewById(R.id.lvactsort);
        lvactsort.setCheeseList(allashjar);
        lvactsort.setAdapter(adaptSort);

    }


    void readmine(){
        String tname="";
        int tsons=0,tid=0,tprog=0,tab=0,tlvl;
        SQLiteDatabase db = mysql.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from (trees) where ab="+id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tname=cursor.getString(cursor.getColumnIndex("name"));
            tid=cursor.getInt(cursor.getColumnIndex("id"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tprog=cursor.getInt(cursor.getColumnIndex("prog"));
            tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));

            allashjar.add(new Ashjar(tname,tid,tlvl,tab,tsons,tprog));
            cursor.moveToNext();
        }
    }
}