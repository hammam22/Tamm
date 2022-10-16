package com.example.mooj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActSin extends AppCompatActivity {

    TextInputEditText EdSinPassw,EdSinEmail;
    Button btSigninuser;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_sin);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth=FirebaseAuth.getInstance();
        EdSinPassw=(TextInputEditText)findViewById(R.id.EdSinPassw);
        EdSinEmail=(TextInputEditText)findViewById(R.id.EdSinEmail);
        btSigninuser=(Button)findViewById(R.id.btSigninuser);
        btSigninuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMyDiaglog();
                String email=EdSinEmail.getText().toString();
                String passW=EdSinPassw.getText().toString();
                showMyDiaglog();
                auth.signInWithEmailAndPassword(email,passW).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        finish();
                        Toast.makeText(getApplicationContext(),"تم تسجيل الدخول بنجاح",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"خطأ في تسجيل الدخول\nتأكد من الاتصال ومعلومات الدخول",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

    }
    private void showMyDiaglog() {
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("جارٍ تسجيل الدخول");
        progressDialog.show();
    }
}