package com.example.mooj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

     
//755BFF
    int id=0;
    int lvl=0;
    int sons=0;
    int steps;
    int noOfListItems;
    int autobackingcont;
    ListView lvmain;
    ImageButton btadd;
    EditText edmain;
    FloatingActionButton fab,floatMovemain,floatPastemain,floatCancelPastemain,floatShareactmain,floatminimain;
    TextView tvmainempty;
    ArrayList<Ashjar> checklistToBeDeleted=new ArrayList<Ashjar>();
    ArrayList<Ashjar> tocalculateprog=new ArrayList<Ashjar>();
    ArrayList<Ashjar> ahfad=new ArrayList<Ashjar>();
    Mysql mysql=new Mysql(this,"mydb");
    ArrayList<Ashjar> ashjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> oldashjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> abOnly=new ArrayList<Ashjar>();
    ArrayList<Ashjar> tempAhfad=new ArrayList<Ashjar>();
    ArrayList<Ashjar> ExpandedAshjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> arrayList;
    boolean deletemode=false,triggerExpand,isInputTime=false,toBackup=false,moveMode=false,ismooj=false,floatsExpanded=false;
    Adaptmain adaptmain;
    FirebaseAuth auth;
    SharedPreferences spmoveref;
    DrawerLayout drawer;
    NavigationView nav;
    Activity thiss;
    DiaLoading loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spmoveref =getSharedPreferences("ismove",MODE_PRIVATE);
        moveMode=spmoveref.getBoolean("ismove",false);
        edmain=(EditText)findViewById(R.id.edmain);


        /*SharedPreferences sp=getSharedPreferences("dl",MODE_PRIVATE);
        if (sp.getInt("no",0)>0){
        loadAshjar(sp.getInt("no",0));}*/

        auth= FirebaseAuth.getInstance();

        Toolbar toolbar= (Toolbar) findViewById(R.id.toolbar);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        mystatusbarcolor();

        drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,drawer,toolbar,R.string.open_drawer,R.string.close_drawer);
        drawer.addDrawerListener(toggle);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        toggle.syncState();

        nav=(NavigationView)findViewById(R.id.nav);
        tvmainempty = (TextView) findViewById(R.id.tvmainempty);
        lvmain = (ListView) findViewById(R.id.lvmain);
        btadd = (ImageButton) findViewById(R.id.btadd);
        fab = (FloatingActionButton) findViewById(R.id.floatdeletemain);
        floatMovemain = (FloatingActionButton) findViewById(R.id.floatMovemain);
        floatPastemain = (FloatingActionButton) findViewById(R.id.floatPastemain);
        floatCancelPastemain = (FloatingActionButton) findViewById(R.id.floatCancelPastemain);
        floatShareactmain = (FloatingActionButton) findViewById(R.id.floatShareactmain);
        floatminimain = (FloatingActionButton) findViewById(R.id.floatminimain);

        thiss=this;

        loading = new DiaLoading();
        loading.setCancelable(false);
        LoadProgBar();

        LoadingThread thread = new LoadingThread();
        thread.start();


    }

    protected void onActivityResult(int req, int result, Intent nn) {

        if (req==2){
            //back from delete or just back button will make error
            moveMode=nn.getBooleanExtra("movemode",false);
        if (moveMode){
            moveAshjar();
            }
        }

        if (req==3){

        }

    }

    private void moveAshjar() {
        floatPastemain.setVisibility(View.VISIBLE);
        floatCancelPastemain.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (deletemode){deletemode=false;readibn(id);
        fab.setVisibility(View.INVISIBLE);
        floatMovemain.setVisibility(View.INVISIBLE);
        floatShareactmain.setVisibility(View.INVISIBLE);
        floatminimain.setVisibility(View.INVISIBLE);
        floatsExpanded=false;
        }
        else if(isInputTime){
        isInputTime=false;
        }
        else if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            autobackup();
            finish();
        }
    }

    private void autobackup() {
        SharedPreferences qw =getSharedPreferences("autobackup",MODE_PRIVATE);
        SharedPreferences.Editor ed=qw.edit();
        autobackingcont=qw.getInt(getDate(),0);
        if (autobackingcont<7){
            autobackingcont++;
            ed.putInt(getDate(),autobackingcont);
            ed.commit();
        }
        ismooj=true;
        saveDatatoSD();
    }


    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.mainmenu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId()==R.id.mnuAccount){
            if (auth.getCurrentUser()==null){
                startActivity(new Intent(MainActivity.this,ActAccount.class));
            }
            else {
                toBackup=true;
                saveDatatoSD();
            }
        }

        if (item.getItemId()==R.id.MyAccountitem){
            if (auth.getCurrentUser()==null){
                startActivity(new Intent(MainActivity.this,ActAccount.class));
            }
            else {
                startActivity(new Intent(MainActivity.this,MyAcoount.class));
            }
        }

        if (item.getItemId()==R.id.addFromDB) {
            if (!isGranted()) {
                Toast.makeText(this, "try to get", Toast.LENGTH_SHORT).show();
                requestMyPer();
            }else {
                startActivity(new Intent(MainActivity.this, ActImport.class));
            /*restoreDatatoSD();
            addFromNewDB();
            ExpandedAshjar.clear();
            calculateProgress();
            readibn(id);
            abOnly=ashjar;
            readHafid();*/
            }
        }

        if (item.getItemId()==R.id.saveBackupToSD){
            if (!isGranted()) {
                Toast.makeText(this, "اسمح بالوصول للذاكرة", Toast.LENGTH_SHORT).show();
                requestMyPer();
            }else {
                DiaExport export = new DiaExport(this, true);
                export.setCancelable(true);
                export.show(getSupportFragmentManager(), "export");
            }
        }

        return super.onOptionsItemSelected(item);
    }





    private boolean isGranted() {
        int howGranted = ContextCompat.checkSelfPermission(
                MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        );
        if (howGranted == PackageManager.PERMISSION_GRANTED){ return true;}
        else { return false; }

    }

    private void requestMyPer() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        )){
            ActivityCompat.requestPermissions(
                    MainActivity.this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    111
            );
        }
        else {
            Toast.makeText(this,"الرجاء اعطاء الصلاحية للذاكرة",Toast.LENGTH_SHORT).show();
        }
    }

    private void saveDatatoSD() {

        if (!isGranted()){
            requestMyPer();
        }

        File sd= Environment.getExternalStorageDirectory();
        File data=Environment.getDataDirectory();


        File moojDir=new File(sd.getAbsolutePath()+"/Android/data/mooj");
        if (!moojDir.exists()){moojDir.mkdir();}
        if (sd.canWrite()){
            String pathdst="";
            String pathscr="/data/"+data.getAbsolutePath()+"/com.example.mooj/databases/mydb";
            if (ismooj){
                pathdst=moojDir.getAbsolutePath()+"/"+getDate()+"-"+autobackingcont+".tmm";
            ismooj=false;}else {
                pathdst=moojDir.getAbsolutePath()+"/"+getDate()+".tmm";
            }

            //Toast.makeText(this,getDate(),Toast.LENGTH_SHORT).show();

            File scr=new File(pathscr);
            File dst=new File(pathdst);
            if (scr.exists()){
                try {

                    FileChannel myscr=new FileInputStream(scr).getChannel();
                    FileChannel mydst=new FileOutputStream(dst).getChannel();
                    mydst.transferFrom(myscr,0,myscr.size());
                    mydst.close();
                    myscr.close();

                    Toast.makeText(getApplicationContext(),"تم التخزين",Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    Toast.makeText(this,"خطأ في المزامنة",Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {Toast.makeText(this,"لا توجد بيانات لمزامنتها",Toast.LENGTH_SHORT).show(); }
        }
        else {
            Toast.makeText(this,"لا يمكن الوصول للذاكرة",Toast.LENGTH_SHORT).show();
        }
        toBackup=false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        onResumeThread resumeThread = new onResumeThread();
        resumeThread.start();
        //calculateProgress();
    }

    class updatetreesThread extends Thread{

        ArrayList<Ashjar> ashjar;
        updatetreesThread(ArrayList<Ashjar> ashjar){
            this.ashjar=ashjar;
        }
        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {




                    if (ashjar.size()==0)
                    {
                        lvmain.setVisibility(View.INVISIBLE);

                        tvmainempty.setVisibility(View.VISIBLE);
                    }
                    else {
                        if (lvmain.getVisibility()==View.INVISIBLE)
                        {
                            tvmainempty.setVisibility(View.INVISIBLE);
                            lvmain.setVisibility(View.VISIBLE);
                        }
                        adaptmain = new Adaptmain(thiss, ashjar);
                        lvmain.setAdapter(adaptmain);

                        oldashjar.clear();
                        SharedPreferences sp =getSharedPreferences("ashjar",MODE_PRIVATE);
                        SharedPreferences.Editor ed=sp.edit();
                        ed.clear();
                        ed.commit();
                        for (int ii=0;ii<ashjar.size();ii++)
                        {
                            ed.putString("name"+String.valueOf(ii),ashjar.get(ii).name);
                            ed.putInt("id"+ii,ashjar.get(ii).id);
                            ed.putInt("ab"+ii,ashjar.get(ii).ab);
                            ed.putInt("sons"+ii,ashjar.get(ii).sons);
                            ed.putInt("prog"+ii,ashjar.get(ii).prog);
                            ed.putInt("lvl"+ii,ashjar.get(ii).lvl);
                        }

                        ed.commit();
                    }




                }
            });
        }
    }

    void updatetrees(ArrayList<Ashjar> ashjar){

        updatetreesThread thread=new updatetreesThread(ashjar);
        thread.start();
    }

    /*
    void saveAshjar(){
        SharedPreferences sp =getSharedPreferences("dl",MODE_PRIVATE);
        SharedPreferences.Editor ed=sp.edit();
        ed.remove("no");
        ed.putInt("no",ashjar.size());

        for (int ii=0;ii<ashjar.size();ii++)
        {
            ed.remove("s"+String.valueOf(ii));
            ed.putString("s"+String.valueOf(ii),ashjar.get(ii));
        }

        ed.commit();

    }

    void loadAshjar(int ii){
        SharedPreferences sp =getSharedPreferences("dl",MODE_PRIVATE);


        for (int i=0;i<ii;i++)
        {
            ashjar.add(sp.getString("s"+String.valueOf(i),"j"));

        }
        //Toast.makeText(this,ashjar.get(0),Toast.LENGTH_SHORT).show();
    }
    */


    public void readibn(int ab) {

        ArrayList<Ashjar> allashjar=new ArrayList<Ashjar>();
        int tsons=0,idtemp=0,tab=0,tprog=0;
        String tname="";
        SQLiteDatabase db = mysql.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from (trees) where ab="+id, null);
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
        ashjar=allashjar;
        updatetrees(ashjar);
        //Toast.makeText(this,String.valueOf(ashjar.get(0).sons),Toast.LENGTH_SHORT).show();

    }



    void addibn(Ashjar sha){
        SQLiteDatabase db=mysql.getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put("ab",sha.ab);
        cv.put("lvl",sha.lvl);
        cv.put("name",sha.name);
        cv.put("sons",0);
        cv.put("prog",0);
        db.insert("trees",null,cv);
    }


    void deletetree(Ashjar root){

        ArrayList<Ashjar> readyList=new ArrayList<Ashjar>();
        ArrayList<Ashjar> searchingList=new ArrayList<Ashjar>();
        searchingList.add(root);
        int lastItem;

        while (searchingList.size()>0) {
            lastItem = searchingList.size() - 1;
            searchForSons(searchingList.get(lastItem));
           // if (arrayList.size()>0){  decreaseSons(searchingList.get(lastItem));  }

            readyList.add(searchingList.get(lastItem));
            searchingList.remove(lastItem);
            if (arrayList.size()>0){
            for (int i = 0; i < arrayList.size(); i++) {
                searchingList.add(arrayList.get(i));
                }
            }
            arrayList.clear();
        }

        fulldelete(readyList);
        readibn(id);
    }

    void searchForSons(Ashjar shajara){
        int tsons=0,tlvl=0,tempid=0,tab=0,tprog=0;
        String tname="";

        arrayList=new ArrayList<Ashjar>();
        SQLiteDatabase dbread = mysql.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("select * from trees where ab='"+shajara.id+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tempid=cursor.getInt(cursor.getColumnIndex("id"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tname=cursor.getString(cursor.getColumnIndex("name"));
            tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
            tprog=cursor.getInt(cursor.getColumnIndex("prog"));
            arrayList.add(new Ashjar(tname,tempid,tlvl,tab,tsons,tprog));
            cursor.moveToNext();
        }
    }

    void fulldelete(ArrayList<Ashjar> readylist){
        SharedPreferences sp=getSharedPreferences("fav", MODE_PRIVATE);

        for (int i=0;i<readylist.size();i++){
            if (sp.getBoolean(String.valueOf(readylist.get(i).id),false)){
                removeFromFav(readylist.get(i).id);
            }
            SQLiteDatabase dbwrite=mysql.getWritableDatabase();
            dbwrite.execSQL("DELETE FROM trees WHERE _rowid_ IN ('"+readylist.get(i).id+"');");
        }

    }

    void toastall(){
        arrayList=new ArrayList<Ashjar>();
        int tsons=0,tlvl=0,tempid=0,tab=0,tprog;
        String tname="";

        SQLiteDatabase dbread = mysql.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("select * from trees", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tempid=cursor.getInt(cursor.getColumnIndex("id"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tname=cursor.getString(cursor.getColumnIndex("name"));
            tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
            tprog=cursor.getInt(cursor.getColumnIndex("prog"));
            arrayList.add(new Ashjar(tname,tempid,tlvl,tab,tsons,tprog));
            cursor.moveToNext();
        }
        /*String alldata="";
        for (int i=0;i<arrayList.size();i++){
            alldata=alldata+"\n"+arrayList.get(i).name;
        }
        Toast.makeText(this,alldata,Toast.LENGTH_LONG).show();*/
        arrayList.clear();
    }

    void ChecklistofDelete(Ashjar shajara,boolean decission){
        boolean readytoAdd=true;
        if (decission){
            //add to List
            if (checklistToBeDeleted.size()>0){
                //List have elements
                //check if duplicated
                for (int i=0;i<checklistToBeDeleted.size();i++){
                    if (checklistToBeDeleted.get(i).id==shajara.id){readytoAdd=false;}
                }
                //add if not duplicated
                if (readytoAdd){checklistToBeDeleted.add(shajara);}
            }
            else {
                //List is empty
                //add first one
                checklistToBeDeleted.add(shajara);
            }


        }

        else {
            //unchecked
            if (checklistToBeDeleted.size()>0){
                for (int i=0;i<checklistToBeDeleted.size();i++){
                    if (checklistToBeDeleted.get(i)==shajara){checklistToBeDeleted.remove(i);}
                }
            }
        }
    }

    /*void deletetree(Ashjar root){

        arrayList=new ArrayList<Ashjar>();
        arrayList.add(root);

        int lastitem=0,noOfSons=0;
        boolean loop=true;
        while (loop){

            lastitem=arrayList.size()-1;
            noOfSons=arrayList.get(lastitem).sons;

            if (noOfSons==0){
                deleteOne(arrayList.get(lastitem));
            }
            else {
                searchSons(arrayList.get(lastitem));
            }

        }
        readibn(id);
    }

    void deleteOne(Ashjar shajara){
        int oldAb=shajara.ab;
        if (oldAb==0){
            SQLiteDatabase dbwrite=mysql.getWritableDatabase();
            dbwrite.execSQL("DELETE FROM trees WHERE _rowid_ IN ('"+shajara.id+"');");
            loop=false;
            finish();
             }
        else {
            subDelete(shajara);
        }
    }

    void finalDelete(Ashjar shajara){
        SQLiteDatabase dbwrite=mysql.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM trees WHERE _rowid_ IN ('"+shajara.id+"');");
        if (shajara.ab==0){
            //arrayList=new ArrayList<Ashjar>();
            loop=false;
        }
        else {
            arrayList.remove(arrayList.indexOf(shajara));
        }
    }

    void searchSons(Ashjar shajara){
        int tsons=0,tlvl=0,tempid=0,tab=0;
        String tname="";

        SQLiteDatabase dbread = mysql.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("select * from trees where ab="+shajara.id, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tempid=cursor.getInt(cursor.getColumnIndex("id"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tname=cursor.getString(cursor.getColumnIndex("name"));
            tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
            arrayList.add(new Ashjar(tname,tempid,tlvl,tab,tsons));
            cursor.moveToNext();
        }
    }

    void subDelete(Ashjar shajara){

        decAb(shajara.ab);
        finalDelete(shajara);
    }

    void decAb(int ab){
        int tempid=0,tsons=0,tab=0,tlvl=0,newsons=0;
        String tname="";
        ArrayList<Ashjar> tempArray=new ArrayList<Ashjar>();

        SQLiteDatabase dbwrite=mysql.getWritableDatabase();
        SQLiteDatabase dbread = mysql.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("select * from trees where id="+ab, null);
        cursor.moveToFirst();


        while (!cursor.isAfterLast()) {
            tempid=cursor.getInt(cursor.getColumnIndex("id"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tname=cursor.getString(cursor.getColumnIndex("name"));
            tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
            tempArray.add(new Ashjar(tname,tempid,tlvl,tab,tsons));
            cursor.moveToNext();
        }
        Ashjar dad=tempArray.get(0);
        newsons=dad.sons-1;
        dbwrite.execSQL("UPDATE trees SET sons="+newsons+" WHERE _rowid_='"+dad.id+"'");
    }

    void lastfinaldelete(Ashjar shajara){
        SQLiteDatabase dbwrite=mysql.getWritableDatabase();
        dbwrite.execSQL("DELETE FROM trees WHERE _rowid_ IN ('"+shajara.id+"');");
    }*/

    void calculateProgress(){
        boolean isfirst=false;
        ArrayList<Ashjar> myashjara=new ArrayList<Ashjar>();
        ArrayList<Ashjar> aba=new ArrayList<Ashjar>();
        int tempid,tab,tsons,tlvl,tprog;
        String tname;
        SQLiteDatabase dbread = mysql.getReadableDatabase();
        Cursor cursor = dbread.rawQuery("select * from trees where sons='"+0+"'", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            tempid=cursor.getInt(cursor.getColumnIndex("id"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tname=cursor.getString(cursor.getColumnIndex("name"));
            tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
            tprog=cursor.getInt(cursor.getColumnIndex("prog"));
            myashjara.add(new Ashjar(tname,tempid,tlvl,tab,tsons,tprog));
            cursor.moveToNext();
        }
        ashjar.clear();
        ashjar=myashjara;

        for (int i=0;i<myashjara.size();i++){
            for (int j=0;j<aba.size();j++){
                if (myashjara.get(i).ab==aba.get(j).ab){isfirst=false;}
                else {isfirst=true;}
            }
            if (isfirst&&myashjara.get(i).ab!=0){aba.add(myashjara.get(i));}
        }

        tocalculateprog=aba;
        calculateforabaList();
        //updatetrees(ashjar);
    }

    void calculateforabaList(){
        //the list calculate progress for ab not for id
        SQLiteDatabase dbwrite=mysql.getWritableDatabase();
        SQLiteDatabase dbread = mysql.getReadableDatabase();
        ArrayList<Ashjar> directSons=new ArrayList<Ashjar>();
        int tempid,tab,tsons,tlvl,tprog,progCount;
        String tname;

        for (int i=0;i < tocalculateprog.size(); i++){

            Cursor cursor = dbread.rawQuery("select * from trees where ab='"+tocalculateprog.get(i).ab+"'", null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                tempid=cursor.getInt(cursor.getColumnIndex("id"));
                tsons=cursor.getInt(cursor.getColumnIndex("sons"));
                tab=cursor.getInt(cursor.getColumnIndex("ab"));
                tname=cursor.getString(cursor.getColumnIndex("name"));
                tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
                tprog=cursor.getInt(cursor.getColumnIndex("prog"));
                directSons.add(new Ashjar(tname,tempid,tlvl,tab,tsons,tprog));
                cursor.moveToNext();
            }
            //we got all sons >> now calculating
            progCount=0;
            for (int k=0; k<directSons.size(); k++){
                if (directSons.get(i).prog==1){progCount++;}
            }
            //save to database
            dbwrite.execSQL("UPDATE trees SET prog="+progCount+" WHERE _rowid_='"+directSons.get(0).ab+"'");
            //add to ashjar arrayList
            cursor = dbread.rawQuery("select * from trees where id='"+tocalculateprog.get(0).ab+"'", null);
            cursor.moveToFirst();
            tempid=cursor.getInt(cursor.getColumnIndex("id"));
            tsons=cursor.getInt(cursor.getColumnIndex("sons"));
            tab=cursor.getInt(cursor.getColumnIndex("ab"));
            tname=cursor.getString(cursor.getColumnIndex("name"));
            tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
            tprog=cursor.getInt(cursor.getColumnIndex("prog"));
            ashjar.add(new Ashjar(tname,tempid,tlvl,tab,tsons,tprog));
        }
    }


    void readHafid(){
        ahfad.clear();
        SQLiteDatabase dbread = mysql.getReadableDatabase();
        int tempid,tab,tsons,tlvl,tprog,progCount;
        String tname;

        for (int i=0 ;i<ashjar.size() ;i++){
            if (ashjar.get(i).sons>0){
                Cursor cursor = dbread.rawQuery("select * from trees where ab='"+ashjar.get(i).id+"'", null);
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    tempid=cursor.getInt(cursor.getColumnIndex("id"));
                    tsons=cursor.getInt(cursor.getColumnIndex("sons"));
                    tab=cursor.getInt(cursor.getColumnIndex("ab"));
                    tname=cursor.getString(cursor.getColumnIndex("name"));
                    tlvl=cursor.getInt(cursor.getColumnIndex("lvl"));
                    tprog=cursor.getInt(cursor.getColumnIndex("prog"));
                    ahfad.add(new Ashjar(tname,tempid,tlvl,tab,tsons,tprog));
                    cursor.moveToNext();
                }
            }
        }

    }

    class ExpandAhfadThread extends Thread{

        Ashjar shajara;
        int index;

        public ExpandAhfadThread(Ashjar shajara,int index){
            this.shajara=shajara;
            this.index=index;
        }

        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    restoreSP();

                    int count=0;
                    int oldAshjarSize=0,restAshjar;
                    tempAhfad.clear();
                    while (tempAhfad.size()!=shajara.sons) {
                        if (ahfad.get(count).ab==shajara.id){tempAhfad.add(ahfad.get(count));}
                        count++;
                    }
                    //add ahfad to ashjar
                    //if (ExpandedAshjar.size()>0){}
                    oldAshjarSize=oldashjar.size();
                    restAshjar=oldAshjarSize-(index+1);
                    ashjar.clear();
                    for (int j=0 ;j<(index+1) ;j++){
                        ashjar.add(oldashjar.get(j));
                    }
                    for (int k=0 ;k<tempAhfad.size() ;k++){
                        ashjar.add(tempAhfad.get(k));

                    }
                    for (int l=(index+1) ;l<(restAshjar+1)+(index) ;l++){
                        ashjar.add(oldashjar.get(l));
                    }
                    //we need only id index sons for undo expanding
                    ExpandedAshjar.add(new Ashjar("",shajara.id,index,0,shajara.sons,0));
                    adaptmain.setexasjar(ExpandedAshjar);
                    for (int b=0; b<ExpandedAshjar.size(); b++)
                        //Toast.makeText(this,"id "+ExpandedAshjar.get(b).id,Toast.LENGTH_SHORT).show();

                        adaptmain.setArrowUp();
                    noOfListItems=ashjar.size();
        /*String tttt="";
        for (int j=0 ;j<ashjar.size() ;j++){tttt=tttt+"\n"+ashjar.get(j).name;}
        Toast.makeText(this,tttt,Toast.LENGTH_SHORT).show();*/
                    updatetrees(ashjar);

                    //
                    //Toast.makeText(this,String.valueOf(ashjar.size()),Toast.LENGTH_SHORT).show();
                    triggerExpand=false;

                }


            });
        }
    }

    void ExpandAhfad(Ashjar shajara,int index){
        ExpandAhfadThread thread=new ExpandAhfadThread(shajara,index);
        thread.start();
    }

    private void restoreSP() {
        oldashjar.clear();
        SharedPreferences sp =getSharedPreferences("ashjar",MODE_PRIVATE);
        for (int i=0;i<ashjar.size();i++)
        {

            oldashjar.add(new Ashjar(sp.getString("name"+(i),"j"),
                    sp.getInt("id"+(i),0),
                    sp.getInt("lvl"+(i),0),
                    sp.getInt("ab"+(i),0),
                    sp.getInt("sons"+(i),0),
                    sp.getInt("prog"+(i),0)));

        }
    }

    class dexpandAhfadThread extends Thread{
        Ashjar shajara;
        int index;
        public dexpandAhfadThread(Ashjar shajara,int index){
            this.shajara=shajara;
            this.index=index;
        }

        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    for (int ii=0; ii<shajara.sons ;ii++){ashjar.remove(index+1);}

                    adaptmain.setArrowDown();
                    updatetrees(ashjar);


                    for (int k=0 ;k<ExpandedAshjar.size() ;k++){
                        if (ExpandedAshjar.get(k).lvl==index){ExpandedAshjar.remove(k);}}

                }
            });
        }
    }

    void dexpandAhfad(Ashjar shajara,int index){

        dexpandAhfadThread thread= new dexpandAhfadThread(shajara,index);
        thread.start();


        /*String tttt="";
        for (int j=0 ;j<ashjar.size() ;j++){tttt=tttt+"\n"+ashjar.get(j).name;}
        Toast.makeText(this,tttt,Toast.LENGTH_SHORT).show();*/
    }

    class ExpandThread extends Thread{

        Ashjar shajara;
        int index;
        public ExpandThread(Ashjar shajara,int index){
            this.shajara=shajara;
            this.index=index;
        }

        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
        }
    }

    void expandOrder(Ashjar shajara,int index) {

        int addedSons = 0, newindex;
        if (ExpandedAshjar.size() > 0) {
            //tasfeyah
            for (int t = 1; t < index; t++) {

                if (ashjar.get(t).ab != id) {
                    addedSons++;
                }
            }

            newindex = index - addedSons;
            //Toast.makeText(this,String.valueOf(shajara.id)+"\n"+ExpandedAshjar.get(0).id,Toast.LENGTH_SHORT).show();
            for (int j = 0; j < ExpandedAshjar.size(); j++) {
                if (newindex == ExpandedAshjar.get(j).lvl) {
                    //if (abOnly.get(newindex).id == ExpandedAshjar.get(j).id) {
                    if (triggerExpand){ dexpandAhfad(shajara, index);
                    }
                } else {
                    if (triggerExpand){ExpandAhfad(shajara, index);}
                }
            }
        }
        else {

            ExpandAhfad(shajara, index);
        }

        LoadProgBar();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.tohome :
                drawer.closeDrawer(GravityCompat.START);
                break;

            case R.id.toaccount :
                drawer.closeDrawer(GravityCompat.START);
                if (auth.getCurrentUser()==null){
                    startActivity(new Intent(MainActivity.this,ActAccount.class));
                }
                else {
                    startActivity(new Intent(MainActivity.this,MyAcoount.class));
                }
                break;

            case R.id.toexit:
                finish();
                break;

            case R.id.tofav:
                drawer.closeDrawer(GravityCompat.START);
                Intent intent=new Intent(MainActivity.this,ACTfav.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    private void removeFromFav(int id) {

        SharedPreferences sp= getSharedPreferences("fav", MODE_PRIVATE);
        SharedPreferences nosp= getSharedPreferences("noOfFav", MODE_PRIVATE);
        int indexttedelete=0;
        int noOfFav=nosp.getInt("noOfFav",0);

        SharedPreferences.Editor ed= sp.edit();
        SharedPreferences.Editor noed= nosp.edit();
        ed.remove(String.valueOf(id));
        ed.commit();
        ArrayList<Integer> list=new ArrayList<Integer>();
        int tempid;

        for (int i=1 ;i<=noOfFav; i++){
            tempid=sp.getInt("id"+i,0);
            list.add(tempid);
            if (tempid==id){
                indexttedelete=i-1;
            }
        }

        if (list.size()>1){
            list.remove(indexttedelete);
        }else {
            list.clear();
        }


        if (list.size()>0){
            for (int j=1;j<=list.size();j++){
                ed.remove("id"+j);
                ed.putInt("id"+j,list.get(j-1));
                ed.commit();
            }
        }
        noed.remove("noOfFav");
        noed.commit();
        noed.putInt("noOfFav",list.size());
        noed.commit();
    }
    
    public String getDate(){
        Calendar calendar = Calendar.getInstance();
        DateFormat simpledate= new SimpleDateFormat("MM/dd/yyyy");
        String date = simpledate.format(calendar.getTime());

        String mm=date.substring(0,2);
        String dd=date.substring(3,5);
        String yy=date.substring(6,10);

        String myDateString = yy+"-"+mm+"-"+dd;

        return myDateString;
    }

    private void mystatusbarcolor() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.statuscolor));
        }


    }

    class LoadProgBarThread extends Thread{
        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loading.show(getSupportFragmentManager(),"Load");
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    loading.dismiss();
                }
            });
        }
    }

    public void LoadProgBar(){
        LoadProgBarThread thread=new LoadProgBarThread();
        thread.start();
    }


    class LoadingThread extends Thread{
        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {


                    nav.setNavigationItemSelectedListener((NavigationView.OnNavigationItemSelectedListener) thiss);

                    nav.setCheckedItem(R.id.tohome);
                    lvmain.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                            deletemode = true;
                            floatminimain.setVisibility(View.VISIBLE);
                            adaptmain.setFirstChecked(i);
                            checklistToBeDeleted.clear();
                            readibn(id);
                            //if (i>0){adaptmain.setchecked(0);adaptmain.notifyDataSetChanged();}

                            return false;
                        }
                    });

                    lvmain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if (deletemode) {
                                adaptmain.setchecked(i);
                                adaptmain.notifyDataSetChanged();
                            } else {
                                Intent intent = new Intent(MainActivity.this, ActShajara.class);

                                intent.putExtra("id", ashjar.get(i).id);

                                startActivityForResult(intent, 2);
                                //Toast.makeText(getApplicationContext(),String.valueOf(i),Toast.LENGTH_SHORT).show();
                            }
                            //Toast.makeText(getApplicationContext(),"click",Toast.LENGTH_SHORT).show();
                        }
                    });


                    btadd.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String temp = edmain.getText().toString();
                            if (!temp.isEmpty()) {
                                Ashjar shajara = new Ashjar(temp, id, lvl + 1, id, sons, 0);
                                addibn(shajara);
                                edmain.setText("");
                                readibn(id);
                            }
                            //Toast.makeText(getApplicationContext(),String.valueOf(ashjar.get(ashjar.size()-1).id),Toast.LENGTH_SHORT).show();
                        }
                    });

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i = 0; i < checklistToBeDeleted.size(); i++) {
                                deletetree(checklistToBeDeleted.get(i));
                            }
                            checklistToBeDeleted.clear();
                            fab.setVisibility(View.INVISIBLE);
                            floatMovemain.setVisibility(View.INVISIBLE);
                            floatShareactmain.setVisibility(View.INVISIBLE);
                            floatminimain.setVisibility(View.INVISIBLE);
                            deletemode = false;
                            moveMode = false;
                            SharedPreferences.Editor edismove = spmoveref.edit();
                            edismove.putBoolean("ismove", false);
                            edismove.commit();
                            floatPastemain.setVisibility(View.INVISIBLE);
                            floatCancelPastemain.setVisibility(View.INVISIBLE);
                            readibn(id);
                        }
                    });

                    floatMovemain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int noOfMovers = 0;
                            fab.setVisibility(View.INVISIBLE);
                            floatMovemain.setVisibility(View.INVISIBLE);
                            moveMode = true;
                            SharedPreferences.Editor edismove = spmoveref.edit();
                            edismove.putBoolean("ismove", true);
                            edismove.commit();
                            SharedPreferences sp = getSharedPreferences("moving", MODE_PRIVATE);
                            SharedPreferences.Editor ed = sp.edit();
                            for (int i = 0; i < checklistToBeDeleted.size(); i++) {
                                ed.putInt("id" + i, checklistToBeDeleted.get(i).id);
                                noOfMovers++;
                            }
                            ed.putInt("noOfMovers", noOfMovers);
                            ed.putInt("oldab", id);
                            ed.putInt("oldsons", sons);
                            ed.commit();
                            deletemode = false;
                            readibn(id);
                        }
                    });

                    floatPastemain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SQLiteDatabase dbwrite = mysql.getWritableDatabase();
                            int noOfMovers = 0, oldab, oldsons;
                            ArrayList<Integer> ints = new ArrayList<Integer>();

                            SharedPreferences sp = getSharedPreferences("moving", MODE_PRIVATE);
                            noOfMovers = sp.getInt("noOfMovers", 0);
                            oldab = sp.getInt("oldab", 0);
                            oldsons = sp.getInt("oldsons", 0);

                            for (int i = 0; i < noOfMovers; i++) {
                                ints.add(sp.getInt("id" + i, 0));
                            }


                            for (int m = 0; m < ints.size(); m++) {
                                dbwrite.execSQL("UPDATE trees SET ab=" + id + " WHERE _rowid_='" + ints.get(m) + "'");
                                dbwrite.execSQL("UPDATE trees SET lvl=" + (lvl + 1) + " WHERE _rowid_='" + ints.get(m) + "'");
                                //Toast.makeText(getApplicationContext(),"moved = "+ints.get(0),Toast.LENGTH_SHORT).show();
                            }
                            dbwrite.execSQL("UPDATE trees SET sons=" + (oldsons - ints.size()) + " WHERE _rowid_='" + oldab + "'");
                            //dbwrite.execSQL("UPDATE trees SET sons=" + (sons+ints.size()) + " WHERE _rowid_='" + id + "'");

                            SharedPreferences.Editor ed = sp.edit();
                            ed.clear();
                            ed.commit();
                            moveMode = false;
                            SharedPreferences.Editor edismove = spmoveref.edit();
                            edismove.putBoolean("ismove", false);
                            edismove.commit();
                            floatPastemain.setVisibility(View.INVISIBLE);
                            floatCancelPastemain.setVisibility(View.INVISIBLE);
                            readHafid();
                            readibn(id);

                        }
                    });

                    floatCancelPastemain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            moveMode = false;
                            SharedPreferences.Editor edismove = spmoveref.edit();
                            edismove.putBoolean("ismove", false);
                            edismove.commit();
                            floatPastemain.setVisibility(View.INVISIBLE);
                            floatCancelPastemain.setVisibility(View.INVISIBLE);
                        }
                    });

                    floatShareactmain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!isGranted()) {
                                requestMyPer();
                            } else {
                                Mysql shareSql = new Mysql(getApplicationContext(), "share");
                                SQLiteDatabase shareDB = shareSql.getWritableDatabase();
                                int tempab = 0;


                                //setup root ab to checklistToBeDeleted

                                ArrayList<Ashjar> myashjar = new ArrayList<Ashjar>();
                                for (int kk = 0; kk < checklistToBeDeleted.size(); kk++) {
                                    myashjar.add(new Ashjar(
                                            checklistToBeDeleted.get(kk).name, checklistToBeDeleted.get(kk).id,
                                            checklistToBeDeleted.get(kk).lvl, 0,
                                            checklistToBeDeleted.get(kk).sons, checklistToBeDeleted.get(kk).prog
                                    ));
                                }

                                ArrayList<Ashjar> allashjar = new ArrayList<Ashjar>();
                                Toast.makeText(getApplicationContext(),
                                        "size = " + myashjar.size(), Toast.LENGTH_SHORT).show();


                                while (myashjar.size() > 0) {
                                    if (myashjar.get(0).sons > 0) {
                                        //add sons to list
                                        String tname = "";
                                        int tsons = 0, tid = 0, tprog = 0, tab = 0, tlvl;
                                        SQLiteDatabase db = mysql.getReadableDatabase();

                                        Cursor cursor = db.rawQuery("select * from (trees) where ab=" + myashjar.get(0).id, null);
                                        cursor.moveToFirst();
                                        while (!cursor.isAfterLast()) {
                                            tname = cursor.getString(cursor.getColumnIndex("name"));
                                            tid = cursor.getInt(cursor.getColumnIndex("id"));
                                            tab = tempab;
                                            tsons = cursor.getInt(cursor.getColumnIndex("sons"));
                                            tprog = cursor.getInt(cursor.getColumnIndex("prog"));
                                            tlvl = cursor.getInt(cursor.getColumnIndex("lvl"));

                                            allashjar.add(new Ashjar(tname, tid, tlvl, tab, tsons, tprog));
                                            cursor.moveToNext();
                                        }
                                        for (int ik = 0; ik < allashjar.size(); ik++) {
                                            myashjar.add(allashjar.get(ik));
                                        }
                                    }


                                    //add father to share db
                                    ContentValues cv = new ContentValues();
                                    cv.put("ab", myashjar.get(0).ab);
                                    cv.put("lvl", myashjar.get(0).lvl);
                                    cv.put("name", myashjar.get(0).name);
                                    cv.put("sons", myashjar.get(0).sons);
                                    cv.put("prog", myashjar.get(0).prog);
                                    shareDB.insert("trees", null, cv);

                                    tempab++;
                                    myashjar.remove(0);
                                }

                                DiaExport diaExport = new DiaExport(getApplicationContext(), false);
                                diaExport.setCancelable(true);
                                diaExport.show(getSupportFragmentManager(), "export");
                                deletemode = false;
                                fab.setVisibility(View.INVISIBLE);
                                floatMovemain.setVisibility(View.INVISIBLE);
                                floatShareactmain.setVisibility(View.INVISIBLE);
                            }
                        }
                    });


                    floatminimain.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (floatsExpanded){

                                fab.animate().translationY(6).setDuration(300).start();
                                floatMovemain.animate().translationY(6).setDuration(300).start();
                                floatShareactmain.animate().translationY(6).setDuration(300).start();


                                        try {
                                            Thread.sleep(300);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                fab.setVisibility(View.INVISIBLE);
                                floatMovemain.setVisibility(View.INVISIBLE);
                                floatShareactmain.setVisibility(View.INVISIBLE);

                                floatsExpanded=false;
                            }else{
                                fab.animate().translationY(-9).setDuration(300).start();
                                floatMovemain.animate().translationY(-9).setDuration(300).start();
                                floatShareactmain.animate().translationY(-9).setDuration(300).start();


                                fab.setVisibility(View.VISIBLE);
                                floatMovemain.setVisibility(View.VISIBLE);
                                floatShareactmain.setVisibility(View.VISIBLE);
                                floatsExpanded=true;
                            }
                        }
                    });
                    //readibn(id);
                    ExpandedAshjar.clear();
                    calculateProgress();
                    readibn(id);
                    abOnly = ashjar;
                    readHafid();
                    if (moveMode) {
                        moveAshjar();
                    }

                    if (!isGranted()) {
                        Toast.makeText(getApplicationContext(), "try to get", Toast.LENGTH_SHORT).show();
                        requestMyPer();
                    }

        /*if (ExpandedAshjar.size()==0){
        oldashjar=ashjar;}*/


                }



            });
        }
    }

    class onResumeThread extends Thread{
        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    nav.setCheckedItem(R.id.tohome);
                    ExpandedAshjar.clear();
                    readHafid();
                    readibn(id);
                    restoreSP();
                }
            });
        }
    }



}




