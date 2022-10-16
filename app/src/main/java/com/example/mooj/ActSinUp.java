package com.example.mooj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ActSinUp extends AppCompatActivity {

    TextInputEditText EdnewName,EdnewEmail,EdnewPassW;
    Button BtnewAccount;
    FirebaseAuth auth;
    private DatabaseReference dbRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_sin_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbRef= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        EdnewName=(TextInputEditText)findViewById(R.id.QEdnewName);
        EdnewEmail=(TextInputEditText)findViewById(R.id.QEdnewEmail);
        EdnewPassW=(TextInputEditText)findViewById(R.id.QEdnewPassW);
        BtnewAccount=(Button)findViewById(R.id.BtnewAccount);
        BtnewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usrName,usrEmail,usrPW;
                usrName=EdnewName.getText().toString();
                usrEmail=EdnewEmail.getText().toString();
                usrPW=EdnewPassW.getText().toString();
                if (usrName.isEmpty()||usrEmail.isEmpty()||usrPW.isEmpty()){
                    Toast.makeText(getApplicationContext(),"اكمل البيانات المطلوبة لإكمال التسجيل",Toast.LENGTH_SHORT).show();
                }
                else {
                    if (usrPW.length()<6){
                        Toast.makeText(getApplicationContext(),"كلمة المرور يجب ان تكون على الأقل 6 أحرف",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        registerNewAcou(usrName,usrEmail,usrPW);
                    }
                }
            }
        });
    }

    void registerNewAcou(String usrName,String usrEmail,String usrPW){
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("جارٍ إنشاء الحساب");
        progressDialog.show();
        auth.createUserWithEmailAndPassword(usrEmail,usrPW).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                HashMap<String,Object> map= new  HashMap<>();
                map.put("usrName",usrName);
                map.put("usrEmail",usrEmail);
                map.put("usrPW",usrPW);
                map.put("id",auth.getCurrentUser().getUid());

                dbRef.child("users").child(auth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"تم إنشاء الحساب بنجاح",Toast.LENGTH_SHORT).show();
                            startActivityForResult(new Intent(ActSinUp.this,MainActivity.class),3);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"يوجد خطأ ما (1)",Toast.LENGTH_SHORT).show();
                        }
                        progressDialog.dismiss();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"يوجد خطأ ما /n تأكد من الإتصال",Toast.LENGTH_SHORT).show();
            }
        });
    }
}