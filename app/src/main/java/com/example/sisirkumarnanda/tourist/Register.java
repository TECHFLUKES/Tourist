package com.example.sisirkumarnanda.tourist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    private EditText mail,pass,cnfpass;
    FirebaseAuth mAuth;
    Button register;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        register = (Button) findViewById(R.id.regButton);
        mail = findViewById(R.id.entrEmail);
        pass = findViewById(R.id.entPass);
        cnfpass = findViewById(R.id.cnfPass);
        progressDialog = new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(mail.getText())||TextUtils.isEmpty(pass.getText())||TextUtils.isEmpty(cnfpass.getText()))
                {
                    Toast.makeText(getApplicationContext(),"Please Fill Up all the fields",Toast.LENGTH_SHORT).show();
                }else {
                    if(pass.getText().toString().equals(cnfpass.getText().toString()))
                    {
                        progressDialog.setMessage("Registering new user!");
                        progressDialog.show();
                                createUser(mail.getText().toString(),pass.getText().toString());
                    }else
                    {
                        Toast.makeText(getApplicationContext(),"Password Mismatch",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
    public void createUser(String mail,String pass){
        mAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),"You are now a registered user",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this,MakeProfile.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
