package com.example.mooj;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MyAcoount extends AppCompatActivity {

    FirebaseAuth auth;
    TextView tvactMyacou;
    Button btSignOut;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_acoount);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();
        tvactMyacou=(TextView)findViewById(R.id.tvactMyacou);
        tvactMyacou.setText(auth.getCurrentUser().getEmail());

        btSignOut=(Button)findViewById(R.id.btSignOut);
        btSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyDiaglog();
                auth.signOut();
                if (auth.getCurrentUser()==null){
                    Toast.makeText(getApplicationContext(),"تم تسجيل الخروج",Toast.LENGTH_SHORT).show();
                    finish();
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(getApplicationContext(),"خطأ\nتأكد من الإتصال",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });

    }

    private void showMyDiaglog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("جارٍ تسجيل الخروج");
        progressDialog.show();
    }
}