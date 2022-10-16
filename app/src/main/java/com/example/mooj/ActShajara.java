package com.example.mooj;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ActShajara extends AppCompatActivity {

    int lvl,id,ab,sons,prog;
    int noOfListItems;
    String shajaratxt;
    float realprog,fsons,fprog;
    Activity thiss;
    Mysql mysql=new Mysql(this,"mydb");
    ArrayList<Ashjar> ashjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> abOnly=new ArrayList<Ashjar>();
    ArrayList<Ashjar> tocalculateprog=new ArrayList<Ashjar>();
    ArrayList<Ashjar> checklistToBeDeleted=new ArrayList<Ashjar>();
    ArrayList<Ashjar> arrayList;
    ArrayList<Ashjar> oldashjar=new ArrayList<Ashjar>();
    ArrayList<Ashjar> tempAhfad=new ArrayList<Ashjar>();
    ArrayList<Ashjar> ahfad=new ArrayList<Ashjar>();
    ArrayList<Ashjar> ExpandedAshjar=new ArrayList<Ashjar>();
    Button btUpdateActSha,btDoneActSha,btback_from_shajara,btCopyTxt,btshareThis;
    ImageButton btfav,btunfav,btaddibn;
    ImageView btDeleteActSha;
    ProgressBar PbarActsha;
    EditText edab;
    TextView tvEmptyActSha,tvlvl,tvshajara,tvprogActShajara;
    FloatingActionButton fab,floatPasteactSha,floatMoveActSha,floatPasteCancelactSha,floatShareactSha,floatminiShajara;
    boolean deletemode=false,increament=false,isToFinish=false,floatsExpanded=false,iscreated=false,triggerExpand,isInputTime=false,moveMode=false,faved;
    SharedPreferences spmoveref,spfav;
    //Adaptmain adaptmain;
    Adaptsub adaptsub;
    NonScrollListView lvactshajara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_shajara);

        thiss=this;

        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        readMe(id);
        spmoveref =getSharedPreferences("ismove",MODE_PRIVATE);

        Toolbar shaToolbar=(Toolbar)findViewById(R.id.edtoolbar);
        setSupportActionBar(shaToolbar);

        mystatusbarcolor();

        //prog=intent.getIntExtra("prog",0);
        tvshajara=(TextView)findViewById(R.id.tvshajara);
        tvshajara.setText(shajaratxt);
        tvlvl=(TextView)findViewById(R.id.tvlvl);
        tvlvl.setText(String.valueOf(lvl));
        //tvprog=(TextView)findViewById(R.id.tvprog);
        //tvprog.setText("%"+String.valueOf(realprog));
        tvprogActShajara=(TextView)findViewById(R.id.tvprogActShajara);
        PbarActsha=(ProgressBar)findViewById(R.id.PbarActsha);
        lvactshajara=(NonScrollListView)findViewById(R.id.lvactshajara);
        btCopyTxt=(Button)findViewById(R.id.btCopyTxt);
        floatminiShajara=(FloatingActionButton)findViewById(R.id.floatminiShajara);
        fab = (FloatingActionButton) findViewById(R.id.floatdeleteshajara);
        floatMoveActSha=(FloatingActionButton)findViewById(R.id.floatMoveActSha);
        floatPasteactSha=(FloatingActionButton)findViewById(R.id.floatPasteactSha);
        floatPasteCancelactSha=(FloatingActionButton)findViewById(R.id.floatPasteCancelactSha);
        floatShareactSha=(FloatingActionButton)findViewById(R.id.floatShareactSha);

        btshareThis=(Button)findViewById(R.id.btshareThis);
        btfav=(ImageButton) findViewById(R.id.btfav);
        btunfav=(ImageButton) findViewById(R.id.btunfav);
        edab=(EditText)findViewById(R.id.edab);
        btDeleteActSha=(ImageView)findViewById(R.id.btDeleteActSha);
        btUpdateActSha=(Button)findViewById(R.id.btUpdateActSha);
        btback_from_shajara=(Button)findViewById(R.id.btback_from_shajara);
        tvEmptyActSha=(TextView)findViewById(R.id.tvEmptyActSha);

    }



    private void mystatusbarcolor() {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.statuscolor));
        }
    }

    class OnCreatThread extends Thread{
        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //start thread

                    setProgtext();
                    //Toast.makeText(getApplicationContext(),String.valueOf(id),Toast.LENGTH_SHORT).show();

                    lvactshajara.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                            deletemode=true;
                            floatminiShajara.setVisibility(View.VISIBLE);
                            checklistToBeDeleted.clear();

                            readibn(id);
                            adaptsub.setFirstChecked(i);
                            adaptsub.notifyDataSetChanged();
                            return false;
                        }
                    });
                    lvactshajara.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(deletemode){
                                adaptsub.setchecked(i);
                                adaptsub.notifyDataSetChanged();
                            }
                            else {
                                //not delete mode
                                Intent intent=new Intent(ActShajara.this,ActShajara.class);
                                intent.putExtra("name",ashjar.get(i).name);
                                intent.putExtra("lvl",lvl+1);
                                intent.putExtra("id",ashjar.get(i).id);
                                intent.putExtra("ab",id);
                                intent.putExtra("sons",ashjar.get(i).sons);
                                intent.putExtra("prog",ashjar.get(i).prog);
                                startActivityForResult(intent,2);
                            }
                        }
                    });

                    spfav =getSharedPreferences("fav",MODE_PRIVATE);
                    faved=spfav.getBoolean(String.valueOf(id),false);

                    btback_from_shajara.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent();
                            setResult(2,intent);
                            intent.putExtra("movemode",moveMode);
                            finish();
                        }
                    });

                    btshareThis.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //to share
                            Mysql shareSql=new Mysql(getApplicationContext(),"share");
                            SQLiteDatabase shareDB = shareSql.getWritableDatabase();
                            int tempab=id;


                            //setup root ab to checklistToBeDeleted

                            ArrayList<Ashjar> allashjar=new ArrayList<Ashjar>();
                            ArrayList<Ashjar> myashjar=new ArrayList<Ashjar>();

                            myashjar.add(new Ashjar(
                                    shajaratxt,id,lvl,ab,sons,prog
                            ));



                            while (myashjar.size()>0){
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

                            DiaExport diaExport=new DiaExport(getApplicationContext(),false);
                            diaExport.setCancelable(true);
                            diaExport.show(getSupportFragmentManager(),"export");
                            deletemode=false;
                            fab.setVisibility(View.INVISIBLE);
                            floatMoveActSha.setVisibility(View.INVISIBLE);
                            floatShareactSha.setVisibility(View.INVISIBLE);
                            floatminiShajara.setVisibility(View.INVISIBLE);
                            //end of share
                        }
                    });

                    btDoneActSha=(Button)findViewById(R.id.btDoneActSha);
                    if (prog<1){btDoneActSha.setText("اكتمل");}
                    else {btDoneActSha.setText("تصفير");}
                    btDoneActSha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //no sons
                            if (prog<1){
                                increament=true;
                                if (sons>0){prog=sons;}
                                else{prog=1;}
                                UpdateProg(prog,id,true,ab);
                            }
                            else {
                                prog=0;
                                increament=false;
                                UpdateProg(prog,id,false,ab);
                            }


                            if(sons>0) {
                                //there is sons
                                //make sons compelete
                                ArrayList<Ashjar> readyList=new ArrayList<Ashjar>();
                                ArrayList<Ashjar> searchingList=new ArrayList<Ashjar>();
                                searchingList.add(new Ashjar(shajaratxt,id,lvl,ab,sons,prog));

                                String tname="";
                                int tid=0,tab=0,tsons=0,tprog=0,lastItem=0;
                                SQLiteDatabase dbread = mysql.getReadableDatabase();
                                SQLiteDatabase dbwrite=mysql.getWritableDatabase();
                                //get all sons
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
                                //all sons in ready list
                                //update progress
                                int noSonNewprog;
                                if (increament){
                                    noSonNewprog=1;
                                    for (int i=0;i<readyList.size();i++){
                                        if (readyList.get(i).sons>0){dbwrite.execSQL("UPDATE trees SET prog="+readyList.get(i).sons+" WHERE _rowid_='"+readyList.get(i).id+"'");
                                        }
                                        else {dbwrite.execSQL("UPDATE trees SET prog="+noSonNewprog+" WHERE _rowid_='"+readyList.get(i).id+"'");
                                        }
                                    }
                                }

                                else {//decreament
                                    noSonNewprog = 0;
                                    for (int i = 0; i < readyList.size(); i++) {
                                        dbwrite.execSQL("UPDATE trees SET prog=" + noSonNewprog + " WHERE _rowid_='" + readyList.get(i).id + "'");
                                    }
                                }
                                //progress updated fo all sons

                            }
                            if (prog<1){//btDoneActSha.setText("اكتمل");
                            }
                            else {//btDoneActSha.setText("تصفير وتهيئة");
                            }

                            //calculateProgress();

                            //readibn(id);
                            readibn(id);
                            setProgtext();
                        }
                    });

                    btUpdateActSha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(ActShajara.this,EditShajara.class);
                            intent.putExtra("txt",shajaratxt);
                            intent.putExtra("id",id);
                            startActivityForResult(intent,1);
                        }
                    });

                    btDeleteActSha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Drawable drawable=btDeleteActSha.getDrawable();
                            if (drawable instanceof AnimatedVectorDrawableCompat){
                                AnimatedVectorDrawableCompat avd1=(AnimatedVectorDrawableCompat)drawable;
                                avd1.start();
                            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                if (drawable instanceof AnimatedVectorDrawable){
                                    AnimatedVectorDrawable avd2=(AnimatedVectorDrawable)drawable;
                                    avd2.start();
                                }
                            }
                            isToFinish=true;
                            Ashjar sha=new Ashjar("",id,lvl,ab,sons,prog);
                            //decSons(sha);
                            deletetree(sha);
                            Intent intent=new Intent();
                            setResult(2,intent);
                            intent.putExtra("movemode",moveMode);
                            removeFromFav(id);
                            finish();
                        }
                    });
                    edab.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                /*if (keyEvent != null && (keyEvent.getKeyCode())==KeyEvent.KEYCODE_ENTER){
                    inputMethodManager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(,InputMethodManager.HIDE_NOT_ALWAYS);
                }
                isInputTime=true;*/
                            //inputLinear.setTranslationY(2f);
                            return false;
                        }
                    });

                    btaddibn=(ImageButton)findViewById(R.id.btaddibn);
                    btaddibn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            fprog=prog;
                            fsons=sons;
                            realprog=fprog/fsons;
                            Ashjar shajara=new Ashjar("",0,0,0,0,0);
                            String temp = edab.getText().toString();

                            SQLiteDatabase db=mysql.getWritableDatabase();
                            if (!temp.isEmpty()) {

                                if ((int) realprog < 1) {
                                    shajara = new Ashjar(temp, 0, lvl + 1, id, 0, 0);
                                } else {
                                    //Toast.makeText(getApplicationContext(),"prg=1",Toast.LENGTH_SHORT).show();
                                    shajara = new Ashjar(temp, 0, lvl + 1, id, 0, 1);
                                }


                                addibn(shajara);
                                edab.setText("");
                            }
                        }
                    });



                    floatminiShajara.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (floatsExpanded){

                                fab.animate().translationY(6).setDuration(300).start();
                                floatMoveActSha.animate().translationY(6).setDuration(300).start();
                                floatShareactSha.animate().translationY(6).setDuration(300).start();


                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                fab.setVisibility(View.INVISIBLE);
                                floatMoveActSha.setVisibility(View.INVISIBLE);
                                floatShareactSha.setVisibility(View.INVISIBLE);

                                floatsExpanded=false;
                            }else{
                                fab.animate().translationY(-6).setDuration(300).start();
                                floatMoveActSha.animate().translationY(-6).setDuration(300).start();
                                floatShareactSha.animate().translationY(-6).setDuration(300).start();


                                fab.setVisibility(View.VISIBLE);
                                floatMoveActSha.setVisibility(View.VISIBLE);
                                floatShareactSha.setVisibility(View.VISIBLE);
                                floatsExpanded=true;
                            }
                        }
                    });
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            for (int i=0;i<checklistToBeDeleted.size();i++){
                                if (checklistToBeDeleted.get(i).prog>0 && checklistToBeDeleted.get(i).ab!=0){
                                    decAbProg(checklistToBeDeleted.get(i).ab,checklistToBeDeleted.get(i).lvl);
                                    // Toast.makeText(getApplicationContext(),"progress decreased",Toast.LENGTH_SHORT).show();
                                }
                                if (checklistToBeDeleted.get(i).ab!=0){//Toast.makeText(getApplicationContext(),"decrease sons",Toast.LENGTH_SHORT).show();
                                    //decSons(checklistToBeDeleted.get(i));
                                }
                                deletetree(checklistToBeDeleted.get(i));

                            }
                            checklistToBeDeleted.clear();
                            fab.setVisibility(View.INVISIBLE);
                            floatMoveActSha.setVisibility(View.INVISIBLE);
                            floatShareactSha.setVisibility(View.INVISIBLE);
                            floatminiShajara.setVisibility(View.INVISIBLE);
                            deletemode=false;
                            moveMode=false;
                            SharedPreferences.Editor edismove=spmoveref.edit();
                            edismove.putBoolean("ismove",false);
                            edismove.commit();
                            floatPasteactSha.setVisibility(View.INVISIBLE);
                            floatPasteCancelactSha.setVisibility(View.INVISIBLE);
                            readMe(id);
                            readibn(id);
                            setProgtext();
                        }
                    });


                    floatMoveActSha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            int noOfMovers=0;
                            fab.setVisibility(View.INVISIBLE);
                            floatMoveActSha.setVisibility(View.INVISIBLE);
                            floatShareactSha.setVisibility(View.INVISIBLE);
                            moveMode=true;
                            SharedPreferences.Editor edismove=spmoveref.edit();
                            edismove.putBoolean("ismove",true);
                            edismove.commit();
                            SharedPreferences sp =getSharedPreferences("moving",MODE_PRIVATE);
                            SharedPreferences.Editor ed=sp.edit();
                            for (int i=0; i<checklistToBeDeleted.size(); i++){
                                ed.putInt("id"+i,checklistToBeDeleted.get(i).id);
                                noOfMovers++;
                            }
                            ed.putInt("noOfMovers",noOfMovers);
                            ed.putInt("oldab",id);
                            ed.putInt("oldsons",sons);
                            ed.commit();
                            deletemode=false;
                            readMe(id);
                            readibn(id);
                            setProgtext();
                        }
                    });

                    floatPasteactSha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            SQLiteDatabase dbwrite=mysql.getWritableDatabase();
                            int noOfMovers=0,oldab,oldsons;
                            ArrayList<Integer> ints=new ArrayList<Integer>();

                            SharedPreferences sp =getSharedPreferences("moving",MODE_PRIVATE);
                            noOfMovers=sp.getInt("noOfMovers",0);
                            oldab=sp.getInt("oldab",0);
                            oldsons=sp.getInt("oldsons",0);

                            for (int i=0; i<noOfMovers; i++){
                                ints.add(sp.getInt("id"+i,0));
                            }


                            for (int m=0; m<ints.size(); m++) {
                                dbwrite.execSQL("UPDATE trees SET ab=" + id + " WHERE _rowid_='" + ints.get(m) + "'");
                                dbwrite.execSQL("UPDATE trees SET lvl=" + (lvl+1) + " WHERE _rowid_='" + ints.get(m) + "'");
                            }
                            if (oldab>0){
                                dbwrite.execSQL("UPDATE trees SET sons=" + (oldsons-ints.size()) + " WHERE _rowid_='" + oldab + "'");}
                            dbwrite.execSQL("UPDATE trees SET sons=" + (sons+ints.size()) + " WHERE _rowid_='" + id + "'");

                            SharedPreferences.Editor ed =sp.edit();
                            ed.clear();
                            ed.commit();
                            moveMode=false;
                            SharedPreferences.Editor edismove=spmoveref.edit();
                            edismove.putBoolean("ismove",false);
                            edismove.commit();
                            floatPasteCancelactSha.setVisibility(View.INVISIBLE);
                            floatPasteactSha.setVisibility(View.INVISIBLE);
                            readHafid();
                            readMe(id);
                            readibn(id);

                        }
                    });

                    floatPasteCancelactSha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            moveMode=false;
                            SharedPreferences.Editor edismove=spmoveref.edit();
                            edismove.putBoolean("ismove",false);
                            edismove.commit();
                            floatPasteactSha.setVisibility(View.INVISIBLE);
                            floatPasteCancelactSha.setVisibility(View.INVISIBLE);
                        }
                    });


                    floatShareactSha.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (checklistToBeDeleted.size()>0){
                                Mysql shareSql=new Mysql(getApplicationContext(),"share");
                            SQLiteDatabase shareDB = shareSql.getWritableDatabase();
                            int tempab=0;


                            //setup root ab to checklistToBeDeleted

                            ArrayList<Ashjar> myashjar=new ArrayList<Ashjar>();
                            for (int kk=0; kk<checklistToBeDeleted.size(); kk++){
                                myashjar.add(new Ashjar(
                                        checklistToBeDeleted.get(kk).name,checklistToBeDeleted.get(kk).id,
                                        checklistToBeDeleted.get(kk).lvl,0,
                                        checklistToBeDeleted.get(kk).sons,checklistToBeDeleted.get(kk).prog
                                ));
                            }

                            ArrayList<Ashjar> allashjar=new ArrayList<Ashjar>();
                            Toast.makeText(getApplicationContext(),
                                    "size = "+myashjar.size(),Toast.LENGTH_SHORT).show();



                            while (myashjar.size()>0){
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

                            DiaExport diaExport=new DiaExport(getApplicationContext(),false);
                            diaExport.setCancelable(true);
                            diaExport.show(getSupportFragmentManager(),"export");
                            deletemode=false;
                            fab.setVisibility(View.INVISIBLE);
                            floatMoveActSha.setVisibility(View.INVISIBLE);
                            floatShareactSha.setVisibility(View.INVISIBLE);
                            floatminiShajara.setVisibility(View.INVISIBLE);
                            }
                            //no items selected to share
                            else{}
                        }
                    });

                    btCopyTxt.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ClipboardManager clip = (ClipboardManager)getApplicationContext().getSystemService(CLIPBOARD_SERVICE);
                            ClipData data = ClipData.newPlainText("shajaratxt",shajaratxt);
                            clip.setPrimaryClip(data);
                            Toast.makeText(getApplicationContext(),"تم النسخ",Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Toast.makeText(this,String.valueOf(prog)+"\n"+String.valueOf(sons),Toast.LENGTH_LONG).show();
                    ExpandedAshjar.clear();
                    //calculateProgress();
                    readHafid();
                    readMe(id);
                    readibn(id);
                    abOnly=ashjar;
                    moveMode=spmoveref.getBoolean("ismove",false);
                    if (moveMode){
                        moveAshjar();
                    }

                    //end thread

                    iscreated=true;

                    OnResumeThread resumeThread=new OnResumeThread();
                    resumeThread.start();
                }
            });
        }
    }

    class OnResumeThread extends Thread{

        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //start thread

                    if (faved){
                        btunfav.setVisibility(View.VISIBLE);
                        btfav.setVisibility(View.INVISIBLE);
                        btunfav.setImageResource(R.drawable.faved);
                    }else{
                        btfav.setVisibility(View.VISIBLE);
                        btunfav.setVisibility(View.INVISIBLE);
                        btfav.setImageResource(R.drawable.unfaved);
                    }

                    btfav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (!faved){
                                btfav.setImageResource(R.drawable.fromunfav);
                                Drawable drawable = btfav.getDrawable();

                                SharedPreferences sp = getSharedPreferences("fav", MODE_PRIVATE);
                                SharedPreferences nosp = getSharedPreferences("noOfFav", MODE_PRIVATE);
                                int noOfFav=nosp.getInt("noOfFav",0);
                                SharedPreferences.Editor ed = sp.edit();
                                SharedPreferences.Editor noed = nosp.edit();

                                int nnn=noOfFav+1;
                                noed.putInt("noOfFav",nnn);
                                noed.commit();
                                ed.putBoolean(String.valueOf(id),true);
                                ed.commit();
                                ed.putInt("id"+nnn,id);
                                ed.commit();

                                if (drawable instanceof AnimatedVectorDrawableCompat) {

                                    AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) drawable;
                                    avd.start();
                                    faved = true;
                                    //btfav.setImageDrawable(getResources().getDrawable(R.drawable.fromfav));


                                } else {
                                    AnimatedVectorDrawable avd = null;

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        avd = (AnimatedVectorDrawable) drawable;
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        avd.reset();
                                    }
                                    avd.start();
                                    faved = true;
                                }
                            }else{
                                btfav.setVisibility(View.INVISIBLE);
                                btunfav.setVisibility(View.VISIBLE);

                                btunfav.setImageResource(R.drawable.fromfav);
                                Drawable drawable=btunfav.getDrawable();
                                removeFromFav(id);


                                if (drawable instanceof AnimatedVectorDrawableCompat) {

                                    AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) drawable;
                                    avd.start();
                                    faved =false;
                                }else{
                                    AnimatedVectorDrawable avd = null;

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        avd = (AnimatedVectorDrawable) drawable;
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        avd.reset();
                                    }
                                    avd.start();
                                    faved = false;
                                }
                            }

                        }
                    });

                    btunfav.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (faved){
                                btunfav.setImageResource(R.drawable.fromfav);
                                Drawable drawable=btunfav.getDrawable();
                                removeFromFav(id);

                                if (drawable instanceof AnimatedVectorDrawableCompat) {

                                    AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) drawable;
                                    avd.start();
                                    faved =false;
                                }else{
                                    AnimatedVectorDrawable avd = null;

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        avd = (AnimatedVectorDrawable) drawable;
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        avd.reset();
                                    }
                                    avd.start();
                                    faved = false;
                                }
                            }else{
                                btunfav.setVisibility(View.INVISIBLE);
                                btfav.setVisibility(View.VISIBLE);
                                btfav.setImageResource(R.drawable.fromunfav);
                                Drawable drawable = btfav.getDrawable();

                                SharedPreferences sp = getSharedPreferences("fav", MODE_PRIVATE);
                                SharedPreferences nosp = getSharedPreferences("noOfFav", MODE_PRIVATE);
                                SharedPreferences.Editor ed = sp.edit();
                                SharedPreferences.Editor noed = nosp.edit();
                                int noOfFav=nosp.getInt("noOfFav",0);

                                int nnn=noOfFav+1;
                                ed.putBoolean(String.valueOf(id), true);
                                ed.commit();
                                ed.putInt("id"+nnn,id);
                                ed.commit();
                                noed.putInt("noOfFav",nnn);
                                ed.commit();

                                if (drawable instanceof AnimatedVectorDrawableCompat) {

                                    AnimatedVectorDrawableCompat avd = (AnimatedVectorDrawableCompat) drawable;
                                    avd.start();
                                    faved = true;
                                    //btfav.setImageDrawable(getResources().getDrawable(R.drawable.fromfav));


                                } else {
                                    AnimatedVectorDrawable avd = null;

                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        avd = (AnimatedVectorDrawable) drawable;
                                    }
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        avd.reset();
                                    }
                                    avd.start();
                                    faved = true;
                                }
                            }
                        }
                    });
        /*SQLiteDatabase db = mysql.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from (trees) where ab="+ab, null);
        cursor.moveToFirst();
        prog=cursor.getInt(cursor.getColumnIndex("prog"));*/
                    ExpandedAshjar.clear();
                    readHafid();
                    readMe(id);
                    readibn(id);
                    setProgtext();

                    //end thread



                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!iscreated){
            OnCreatThread creatThread= new OnCreatThread();
            creatThread.start();
        }else {
            OnResumeThread resumeThread=new OnResumeThread();
            resumeThread.start();
        }

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

    @Override
    public void onBackPressed() {
        if (deletemode){deletemode=false;readibn(id);
        fab.setVisibility(View.INVISIBLE);
        floatMoveActSha.setVisibility(View.INVISIBLE);
        floatShareactSha.setVisibility(View.INVISIBLE);
        floatminiShajara.setVisibility(View.INVISIBLE);
        }
        else if(isInputTime){
            //inputLinear.setTranslationY(-500f);
            isInputTime=false;
        }
        else {
            Intent intent=new Intent();
            setResult(2,intent);
            intent.putExtra("movemode",moveMode);
            finish();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inf=getMenuInflater();
        inf.inflate(R.menu.actshajaramenu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.makesorting)
        {
            Intent intent=new Intent(ActShajara.this,SortingItems.class);
            intent.putExtra("id",id);
            startActivityForResult(intent,4);
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int req, int result, Intent nn) {

        if (req==1&&result==RESULT_OK){
            readMe(id);
            //Toast.makeText(this,"req==1",Toast.LENGTH_SHORT).show();
            String txt=nn.getStringExtra("txt");
            SQLiteDatabase dbwrite=mysql.getWritableDatabase();
            dbwrite.execSQL("UPDATE trees SET 'name'='"+txt+"' WHERE _rowid_='"+id+"'");
            //Toast.makeText(this,txt,Toast.LENGTH_SHORT).show();
            tvshajara.setText(txt);
            readibn(id);
        }
        if (req==2){
                moveMode=nn.getBooleanExtra("movemode",false);
                if (moveMode){
                    moveAshjar();
                }


            readMe(id);
            readibn(id);
            setProgtext();
        }
    }

    private void moveAshjar() {
        floatPasteactSha.setVisibility(View.VISIBLE);
        floatPasteCancelactSha.setVisibility(View.VISIBLE);
    }


    void addibn(Ashjar sha){
        SQLiteDatabase db=mysql.getWritableDatabase();
        //SQLiteDatabase dbread=mysql.getReadableDatabase();
        if (sha.prog==1){
            if (sons>0){//already have kids
                int tempint=sons;
                tempint++;
                //Toast.makeText(this,"id= "+id,Toast.LENGTH_SHORT).show();
                db.execSQL("UPDATE trees SET prog="+tempint+" WHERE _rowid_='"+sha.ab+"'");
                prog=tempint; }
            else {//first kid

            }

        }


        ContentValues cv=new ContentValues();
        cv.put("ab",sha.ab);
        cv.put("lvl",sha.lvl);
        cv.put("name",sha.name);
        cv.put("sons",sha.sons);
        cv.put("prog",sha.prog);
        db.insert("trees",null,cv);
        incSons();
        readMe(id);
        readibn(id);
    }

    void updatetrees(ArrayList<Ashjar> ashjar){

        //ashjar.size()
        if (ashjar.size()==0)
        {
            lvactshajara.setVisibility(View.INVISIBLE);
            tvEmptyActSha.setVisibility(View.VISIBLE);
        }
        else {
                tvEmptyActSha.setVisibility(View.INVISIBLE);
                lvactshajara.setVisibility(View.VISIBLE);


            adaptsub = new Adaptsub(this,ashjar);

            lvactshajara.setAdapter(adaptsub);
        }
       // Toast.makeText(this,ttt,Toast.LENGTH_LONG);
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

    void readMe(int id){
        SQLiteDatabase db = mysql.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from (trees) where id="+id, null);
        cursor.moveToFirst();
            shajaratxt=cursor.getString(cursor.getColumnIndex("name"));
            //tid=cursor.getInt(cursor.getColumnIndex("id"));
            ab=cursor.getInt(cursor.getColumnIndex("ab"));
            sons=cursor.getInt(cursor.getColumnIndex("sons"));
            prog=cursor.getInt(cursor.getColumnIndex("prog"));
            lvl=cursor.getInt(cursor.getColumnIndex("lvl"));
           // Toast.makeText(this,"sons = "+sons+"\nprog = "+prog,Toast.LENGTH_SHORT).show();
        }

    void readibn(int ab) {


        ArrayList<Ashjar> allashjar=new ArrayList<Ashjar>();

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
        ashjar=allashjar;
        updatetrees(ashjar);

        cursor = db.rawQuery("select * from (trees) where id="+id, null);
        cursor.moveToFirst();
            prog=cursor.getInt(cursor.getColumnIndex("prog"));
            sons=cursor.getInt(cursor.getColumnIndex("sons"));
        //tvprog.setText("Changed  "+String.valueOf(prog)+"/"+String.valueOf(sons));
    }


    void incSons(){
        sons++;
        SQLiteDatabase db=mysql.getWritableDatabase();
        db.execSQL("UPDATE trees SET sons="+sons+" WHERE _rowid_='"+id+"'");
    }

    /*void decSons(Ashjar root){
        int tsons,tempid,tab;
        SQLiteDatabase dbread=mysql.getReadableDatabase();
        SQLiteDatabase dbwrite=mysql.getWritableDatabase();

        if (root.lvl>2){
            tab=root.ab;
            for (int i=0;i<root.lvl-1;i++) {
                Cursor cursor = dbread.rawQuery("select * from trees where id='" + tab + "'", null);
                cursor.moveToFirst();
                tempid = cursor.getInt(cursor.getColumnIndex("id"));
                tsons = cursor.getInt(cursor.getColumnIndex("sons"));
                tab = cursor.getInt(cursor.getColumnIndex("ab"));
                tsons--;
                dbwrite.execSQL("UPDATE trees SET sons=" + tsons + " WHERE _rowid_='" + tempid + "'");
            }}

    }*/

    void deletetree(Ashjar root){

        SQLiteDatabase dbwrite=mysql.getWritableDatabase();
        SQLiteDatabase dbread = mysql.getReadableDatabase();
        Cursor cursor;

        int lastab=root.ab;
        int lastid;
        int lastsons;
        while (lastab!=0){
            cursor = dbread.rawQuery("select * from (trees) where id="+lastab, null);
            cursor.moveToFirst();
            lastsons=cursor.getInt(cursor.getColumnIndex("sons"));
            lastid=cursor.getInt(cursor.getColumnIndex("ab"));
            lastsons--;
            dbwrite.execSQL("UPDATE trees SET sons="+lastsons+" WHERE _rowid_='"+lastab+"'");
            lastab=lastid;
        }

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
        if (isToFinish){}
        else {readibn(id);}

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
        SQLiteDatabase dbwrite=mysql.getWritableDatabase();

        SharedPreferences sp = getSharedPreferences("fav", MODE_PRIVATE);
        boolean infavedlist;
        int noOfFav;
        SharedPreferences.Editor ed = sp.edit();

        for (int i=0;i<readylist.size();i++){
            dbwrite.execSQL("DELETE FROM trees WHERE _rowid_ IN ('"+readylist.get(i).id+"');");
            infavedlist=sp.getBoolean(String.valueOf(readylist.get(i).id),false);
            if (infavedlist){
                removeFromFav(readylist.get(i).id);
            }
        }
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

    void UpdateProg(int newprog,int progid,boolean increament,int firstAb){
        SQLiteDatabase db=mysql.getWritableDatabase();
        SQLiteDatabase dbread = mysql.getReadableDatabase();
        int lvl=this.lvl;
        lvl--;
        db.execSQL("UPDATE trees SET prog="+newprog+" WHERE _rowid_='"+progid+"'");
        if (lvl>0){
            int tempid,lastProg;
            int abSequence=firstAb;
            for (int i=0;i<lvl;i++){


                Cursor cursor = dbread.rawQuery("select * from trees where id='"+abSequence+"'", null);
                cursor.moveToFirst();
                tempid=abSequence;
                abSequence=cursor.getInt(cursor.getColumnIndex("ab"));
                lastProg=cursor.getInt(cursor.getColumnIndex("prog"));
                if (increament){
                    lastProg=lastProg+1;
                    db.execSQL("UPDATE trees SET prog="+lastProg+" WHERE _rowid_='"+tempid+"'");
                }
                else {
                    lastProg=lastProg-1;
                    db.execSQL("UPDATE trees SET prog="+lastProg+" WHERE _rowid_='"+tempid+"'");
                }

            }

        }
        readibn(id);
        }


        void setProgtext() {
            //prog=getmyProg(id);
            //tvprog.setText(String.valueOf(realprog));
            float fprog=prog,fsons=sons;
            float realprog;
            if (sons > 0) {
                realprog = (fprog / fsons) * 100;
            } else {
                realprog = (fprog) * 100;
            }
            PbarActsha.setProgress((int) realprog);
            tvprogActShajara.setText(String.valueOf((int) realprog));
        }

        void decAbProg(int id,int lvl){
        int tempid,lastProg,tab;
            SQLiteDatabase db=mysql.getWritableDatabase();
            SQLiteDatabase dbread = mysql.getReadableDatabase();
            lvl--;
           // for (int i=0;i<lvl;i++){
                Cursor cursor = dbread.rawQuery("select * from trees where ab='"+id+"'", null);
                cursor.moveToFirst();
                tempid=cursor.getInt(cursor.getColumnIndex("id"));
                tab=cursor.getInt(cursor.getColumnIndex("ab"));
                lastProg=cursor.getInt(cursor.getColumnIndex("prog"));
                lastProg=lastProg-1;
                db.execSQL("UPDATE trees SET prog="+lastProg+" WHERE _rowid_='"+tempid+"'");
                id=tab;
          // }
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
        } else {

            ExpandAhfad(shajara, index);
        }

    }


    void dexpandAhfad(Ashjar shajara,int index){

        for (int ii=0; ii<shajara.sons ;ii++){ashjar.remove(index+1);}

        adaptsub.setArrowDown();
        updatetrees(ashjar);


        for (int k=0 ;k<ExpandedAshjar.size() ;k++){
            if (ExpandedAshjar.get(k).lvl==index){ExpandedAshjar.remove(k);}}

        /*String tttt="";
        for (int j=0 ;j<ashjar.size() ;j++){tttt=tttt+"\n"+ashjar.get(j).name;}
        Toast.makeText(this,tttt,Toast.LENGTH_SHORT).show();*/
    }


    class ExpandAhfadThread extends Thread{

        Ashjar shajara;
        int index;
        ExpandAhfadThread(Ashjar shajara,int index){
        this.shajara=shajara;
        this.index=index;
        }

        public void run(){
            thiss.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //start thread
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
                    for (int b=0; b<ExpandedAshjar.size(); b++)
                        //Toast.makeText(this,"id "+ExpandedAshjar.get(b).id,Toast.LENGTH_SHORT).show();

                        adaptsub.setArrowUp();
                    noOfListItems=ashjar.size();
        /*String tttt="";
        for (int j=0 ;j<ashjar.size() ;j++){tttt=tttt+"\n"+ashjar.get(j).name;}
        Toast.makeText(this,tttt,Toast.LENGTH_SHORT).show();*/
                    updatetrees(ashjar);

                    //
                    //Toast.makeText(this,String.valueOf(ashjar.size()),Toast.LENGTH_SHORT).show();
                    triggerExpand=false;

                    //end thread

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



}