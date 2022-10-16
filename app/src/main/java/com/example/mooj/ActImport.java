package com.example.mooj;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class ActImport extends AppCompatActivity implements FragFromDtata.intrFromData,FragFromTamm.intrFromTamm{

    RadioButton radFromSD,radFromDateFolder;
    TextView tvfromFile,tvFromData;
    Button backFromImport;
    int mainsons=0;
    File impFile,sd,data;
    Mysql mysql=new Mysql(this,"mydb");
    Mysql tempsql=new Mysql(this,"tempdb");
    ArrayList<Ashjar> orgAshjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> newAshjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> waitingAshjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> readyAshjar=new ArrayList<Ashjar>();
    FragFromDtata fromDtata;
    String dbNameString;
    FragmentTransaction ft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_import);

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.statuscolor));
        }

        fromDtata=new FragFromDtata();
        ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.mainFrag,fromDtata,"aaa");
        ft.commit();

        backFromImport=(Button)findViewById(R.id.backFromImport);
        backFromImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvFromData = (TextView)findViewById(R.id.tvFromData);
        tvfromFile  = (TextView)findViewById(R.id.tvfromFile);
        radFromDateFolder=(RadioButton)findViewById(R.id.radFromDateFolder);
        radFromSD =(RadioButton)findViewById(R.id.radFromSD);

        radFromSD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFromData.setTextColor(getResources().getColor(R.color.black));
                tvfromFile.setTextColor(getResources().getColor(R.color.purple_700));
                FragFromTamm fromTamm=new FragFromTamm(getApplicationContext());
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrag,fromTamm,"aaa");
                ft.commit();
            }
        });
        radFromDateFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvFromData.setTextColor(getResources().getColor(R.color.purple_700));
                tvfromFile.setTextColor(getResources().getColor(R.color.black));
                fromDtata=new FragFromDtata();
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.mainFrag,fromDtata,"aaa");
                ft.commit();
            }
        });

    }

    @Override
    public void getDbFromPath(String path,boolean ispath) {

        ArrayList<Integer> beginOfFileName=new ArrayList<Integer>();
        int tab=0,tprog=0,tlvl,maxId,total=0;
        String tname="";

        String firstLetter= path.substring(0,1);
        if (firstLetter.equals("/")){
            for (int ii=path.length(); ii>0; ii--){
                if (path.substring(ii-1,ii).equals("/")){
                    beginOfFileName.add(ii);
                }
            }
            dbNameString=path.substring(beginOfFileName.get(0),path.length()-4);
        }
        else {
            dbNameString = path.substring(0,11);
        }

        DiaImport diaImport= new DiaImport();
        diaImport.setCancelable(false);

        sd= Environment.getExternalStorageDirectory();
        data = Environment.getDataDirectory();
        diaImport.show(getSupportFragmentManager(),"wait to import");
        if (!ispath){
            path=sd.getAbsolutePath()+"/Android/data/mooj/"+path;
        }
        impFile= new File(path);

        SQLiteDatabase tempdbread=tempsql.getReadableDatabase();

        File tempdp= new File("/data/"+data.getAbsolutePath()+"/com.example.mooj/databases/tempdb");

        try {
            FileChannel myscr=new FileInputStream(impFile).getChannel();
            FileChannel mydst=new FileOutputStream(tempdp).getChannel();
            mydst.transferFrom(myscr,0,myscr.size());
            mydst.close();
            myscr.close();

            //Toast.makeText(getApplicationContext(),"تم النقل للاختبار",Toast.LENGTH_SHORT).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Cursor cursor = tempdbread.rawQuery("select * from trees", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tname=cursor.getString(cursor.getColumnIndex("name"));
                total++;
                cursor.moveToNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this,"النسخة تالفة ولا يمكن القراءة منها",Toast.LENGTH_SHORT).show();
        }

        SQLiteDatabase orgdb = mysql.getReadableDatabase();
        orgAshjar=readibn(0,orgdb);
        //Toast.makeText(this,"org = "+orgAshjar.size(),Toast.LENGTH_SHORT).show();
        newAshjar=readibn(0,tempdbread);
        mainsons=newAshjar.size();
        //Toast.makeText(this,"new = "+newAshjar.size(),Toast.LENGTH_SHORT).show();

        int matches=0;
        boolean isinner=false;
        boolean isOrgBigger=false;
        int j=0;
        while (j<orgAshjar.size()){
            if (newAshjar.size()>0) {
                for (int i = 0; i < newAshjar.size(); i++) {
                    if (orgAshjar.get(j).name.equals(newAshjar.get(i).name)) {
                        matches++;
                        newAshjar.remove(i);
                    }

                }
            }
            j++;

            if (newAshjar.size()==0){
                isOrgBigger=true;
            }else if (orgAshjar.size()==0){
                isOrgBigger=false;
            }
        }

        diaImport.dismiss();

        DiaPreImport preImport = new DiaPreImport(total,matches,tempdp,this);
        preImport.setCancelable(true);
        preImport.show(getSupportFragmentManager(),"pre import");

    }

    public ArrayList<Ashjar> readibn(int ab,SQLiteDatabase db) {

        ArrayList<Ashjar> allashjar=new ArrayList<Ashjar>();
        int tsons=0,idtemp=0,tab=0,tprog=0;
        String tname="";

        Cursor cursor = db.rawQuery("select * from (trees) where ab="+ab, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tname=cursor.getString(cursor.getColumnIndex("name"));
            idtemp=cursor.getInt(cursor.getColumnIndex("id"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tprog=cursor.getInt(cursor.getColumnIndex("prog"));
            allashjar.add(new Ashjar(tname,idtemp,0,tab,tsons,tprog));
            cursor.moveToNext();
        }
        return allashjar;
    }

    void addtoDB(File file) {
        DiaImportProps importProps = new DiaImportProps();
        importProps.setCancelable(false);
        importProps.show(getSupportFragmentManager(),"get from db");

        int countAdding=0;
        int tsons=0,idtemp=0,tab=0,tprog=0;
        String tname="";

        addibn(new Ashjar("0",0,0,0,mainsons,0));

        SQLiteDatabase orgdb = mysql.getReadableDatabase();
        SQLiteDatabase tempdbread=tempsql.getReadableDatabase();

        Mysql thismydb=new Mysql(this,"mydb");
        SQLiteDatabase orgdbw = thismydb.getWritableDatabase();
        orgdb = thismydb.getReadableDatabase();
        Cursor cursor2 = orgdb.rawQuery("SELECT max(_rowid_) FROM trees", null);
        cursor2.moveToFirst();
        idtemp=cursor2.getInt(cursor2.getColumnIndex("max(_rowid_)"));;

        orgdbw.execSQL("UPDATE trees SET name='"+dbNameString+"' WHERE _rowid_='"+idtemp+"'");

        int newdbid=0;

        waitingAshjar.clear();
        readyAshjar.clear();
        waitingAshjar=readibn(newdbid,tempdbread);

        for (int i=0; i<waitingAshjar.size(); i++){

            addibn(new Ashjar(
                    waitingAshjar.get(i).name,waitingAshjar.get(i).id,
                    waitingAshjar.get(i).lvl,idtemp,
                    waitingAshjar.get(i).sons,waitingAshjar.get(i).prog
            ));
            countAdding++;

            if (waitingAshjar.get(i).sons>0){
                newAshjar.clear();
                newAshjar=readibn(waitingAshjar.get(i).id,tempdbread);
                for (int j=0; j<newAshjar.size(); j++){
                    readyAshjar.add(new Ashjar(
                            newAshjar.get(j).name,newAshjar.get(j).id,
                            newAshjar.get(j).lvl,idtemp+i+1,
                            newAshjar.get(j).sons,newAshjar.get(j).prog
                    ));
                }
            }



        }

        //second generation

        while (readyAshjar.size()>0) {
            waitingAshjar.clear();

            //Toast.makeText(this,"ready ="+readyAshjar.size(),Toast.LENGTH_SHORT).show();

            for (int k = 0; k < readyAshjar.size(); k++) {
                addibn(readyAshjar.get(k));
                countAdding++;
                if (readyAshjar.get(k).sons > 0) {
                    newAshjar.clear();
                    newAshjar = readibn(readyAshjar.get(k).id, tempdbread);
                    for (int j = 0; j < newAshjar.size(); j++) {
                        readyAshjar.add(new Ashjar(
                                newAshjar.get(j).name, newAshjar.get(j).id,
                                newAshjar.get(j).lvl, idtemp + countAdding,
                                newAshjar.get(j).sons, newAshjar.get(j).prog
                        ));
                    }
                }
                k--;
                readyAshjar.remove(0);

            }

        }

        importProps.dismiss();
        Toast.makeText(this,"تمت الإضافة الى "+dbNameString,Toast.LENGTH_SHORT).show();
        finish();
    }


    void addibn(Ashjar sha){
        SQLiteDatabase db=mysql.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("ab",sha.ab);
        cv.put("lvl",sha.lvl);
        cv.put("name",sha.name);
        cv.put("sons",sha.sons);
        cv.put("prog",sha.prog);
        db.insert("trees",null,cv);
    }

}