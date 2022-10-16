package com.example.mooj;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ACTfav extends AppCompatActivity {

    Mysql mysql=new Mysql(this,"mydb");
    ListView lvactfav;
    TextView tvnofav;
    Button btback;
    SharedPreferences sp;
    ArrayList<Integer> favlist;
    ArrayList<Ashjar> ashjar=new ArrayList<Ashjar>();
    AdaptFav adaptfav;
    int noOfFav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_fav);

        Toolbar toolbar= (Toolbar) findViewById(R.id.favtoolbar);
        toolbar.setSubtitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("المفضلة");
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(getResources().getColor(R.color.statuscolor));}

        favlist=new ArrayList<Integer>();

        btback=(Button)findViewById(R.id.btback_from_fav);
        btback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        ashjar.clear();
        favlist.clear();
        getmeyfavlistids();
        getmeyfavlist();

        lvactfav=(ListView)findViewById(R.id.lvactfav);
        if (noOfFav>0){
            adaptfav=new AdaptFav(this,ashjar);
            lvactfav.setAdapter(adaptfav);
        }else {
            lvactfav.setVisibility(View.INVISIBLE);
            tvnofav=(TextView)findViewById(R.id.tvnofav);
            tvnofav.setVisibility(View.VISIBLE);
        }

        lvactfav.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(ACTfav.this,ActShajara.class);
                intent.putExtra("name",ashjar.get(i).name);
                intent.putExtra("id",ashjar.get(i).id);
                intent.putExtra("sons",ashjar.get(i).sons);
                intent.putExtra("prog",ashjar.get(i).prog);
                startActivity(intent);
            }
        });

    }

    private void getmeyfavlistids() {
        sp=getSharedPreferences("fav", MODE_PRIVATE);
        SharedPreferences nosp= getSharedPreferences("noOfFav", MODE_PRIVATE);
        noOfFav=nosp.getInt("noOfFav",0);

        for (int i=1;i<=noOfFav;i++){
            favlist.add(sp.getInt("id"+i,0));
        }
    }

    private void getmeyfavlist() {
        for (int i=0;i<noOfFav;i++){
            //Toast.makeText(this,"ids = id"+favlist.get(i),Toast.LENGTH_SHORT).show();
            readMe(favlist.get(i));
        }
    }

    void readMe(int id){
        String shajaratxt;
        int ab,sons,prog,lvl;
        SQLiteDatabase db = mysql.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from (trees) where id="+id, null);
        cursor.moveToFirst();
        shajaratxt=cursor.getString(cursor.getColumnIndex("name"));
        //tid=cursor.getInt(cursor.getColumnIndex("id"));
        ab=cursor.getInt(cursor.getColumnIndex("ab"));
        sons=cursor.getInt(cursor.getColumnIndex("sons"));
        prog=cursor.getInt(cursor.getColumnIndex("prog"));
        lvl=cursor.getInt(cursor.getColumnIndex("lvl"));
        ashjar.add(new Ashjar(shajaratxt,id,lvl,ab,sons,prog));
        // Toast.makeText(this,"sons = "+sons+"\nprog = "+prog,Toast.LENGTH_SHORT).show();
    }
}