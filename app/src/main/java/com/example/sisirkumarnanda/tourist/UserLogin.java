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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class UserLogin extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private EditText email,pass;
    private TextView register;
    private Button login;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        mAuth = FirebaseAuth.getInstance();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent intent = new Intent(UserLogin.this,MainActivity.class
            );
            startActivity(intent);
            finish();
        }
        register = (TextView)findViewById(R.id.register);
        email = (EditText) findViewById(R.id.entrEmail);
        pass = (EditText) findViewById(R.id.entPass);
        login = (Button)findViewById(R.id.lgnButton);
        progressDialog = new ProgressDialog(this);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLogin.this,Register.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(email.getText())|| TextUtils.isEmpty(pass.getText())){
                    Toast.makeText(UserLogin.this,"Fill up all the fields",Toast.LENGTH_LONG).show();
                }
                else {
                    progressDialog.setMessage("Signing In");
                    progressDialog.show();
                    String mail = email.getText().toString();
                    String  password = pass.getText().toString();

                    login(mail,password);
                }
            }
        });
    }
    public void login(String mail,String password){
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Intent intent = new Intent(UserLogin.this,MainActivity.class);
                    startActivity(intent);
                    finish();

                }else
                {
                    Toast.makeText(getApplicationContext(),"You are not a registered user!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
