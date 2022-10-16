package com.example.mooj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EditShajara extends AppCompatActivity {

    ImageButton btAddAcEd;
    Button btPaste;
    EditText edActEd;
    String txt;
    int passedId;
    boolean isInputTime=false;
    Mysql mysql;
    InputMethodManager inputMethodManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_shajara);

        Intent intent=getIntent();
        mysql=new Mysql(this,"mydb");

        passedId=intent.getIntExtra("id",0);
        txt=intent.getStringExtra("txt");
        edActEd=(EditText)findViewById(R.id.edActEd);
        edActEd.setText(txt);


        btAddAcEd=(ImageButton) findViewById(R.id.btAddAcEd);
        btAddAcEd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt=edActEd.getText().toString();
                //Toast.makeText(getApplicationContext(),String.valueOf(passedId),Toast.LENGTH_SHORT).show();

                Intent intent=new Intent(EditShajara.this,ActShajara.class);

                intent.putExtra("txt",txt);
                setResult(RESULT_OK,intent);
                finish();
            }
        });



    }

}