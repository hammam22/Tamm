package com.example.mooj;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ActAccount extends AppCompatActivity {

    Button btSignin,btSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_account);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //sign up or in
        btSignin=(Button)findViewById(R.id.btSignin);
        btSignup=(Button)findViewById(R.id.btSignup);

        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActAccount.this,ActSin.class));

            }
        });

        btSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActAccount.this,ActSinUp.class));
                
            }
        });



    }
}