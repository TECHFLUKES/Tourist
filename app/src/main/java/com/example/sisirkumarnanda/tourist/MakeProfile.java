package com.example.sisirkumarnanda.tourist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sisirkumarnanda.tourist.Model.UserRegistration;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class MakeProfile extends AppCompatActivity {

    //private static final java.util.UUID UUID = ;
    private EditText firstname,lastname,country;
    Spinner cause;
    Button chooseIamge,save;
    private final int PICK_IMAGE_REQUEST = 71;
    private Uri filePath;
    FirebaseStorage storage;
    FirebaseAuth mAuth;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    UserRegistration userRegistration;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_profile);
        firstname = findViewById(R.id.fname);
        lastname = findViewById(R.id.lname);
        country = findViewById(R.id.country);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        chooseIamge = findViewById(R.id.upload);
        save = findViewById(R.id.save);
        cause = (Spinner) findViewById(R.id.cause);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cause, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        cause.setAdapter(adapter);
        chooseIamge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        final EditText finalFirstname = firstname;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(firstname.getText())||TextUtils.isEmpty(lastname.getText())||TextUtils.isEmpty(country.getText()))
                {
                    Toast.makeText(getApplicationContext(),"Fill Up All The Fields",Toast.LENGTH_SHORT).show();
                }else
                {
                    userRegistration = new UserRegistration(firstname.getText().toString(),lastname.getText().toString(),
                            country.getText().toString(),cause.getSelectedItem().toString());
                    if(filePath==null)
                    {
                        final DatabaseReference databaseReference = firebaseDatabase.getReference();
                        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userRegistration);  Intent intent  = new Intent(getApplicationContext(),MapsActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        final DatabaseReference databaseReference = firebaseDatabase.getReference();
                        databaseReference.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(userRegistration);
                        uploadImage();

                    }
                    Toast.makeText(getApplicationContext(),"Saved", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            // uploadImage();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);


            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+mAuth.getCurrentUser().getUid());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(MakeProfile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            Intent intent  = new Intent(getApplicationContext(),MapsActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MakeProfile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
}
