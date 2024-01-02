package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    EditText RegisterPhone, RegisterPassword,RegisterName;
    String Phone, Password, Name;  //temp buat nampung
    Button RegisterBtn;
    //ProgressBar LoadingBar; loading

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        RegisterPhone = (EditText) findViewById(R.id.register_phone);
        RegisterPassword = (EditText) findViewById(R.id.register_password);
        RegisterName = (EditText) findViewById(R.id.register_name);

        RegisterBtn = (Button)findViewById(R.id.register_btn);

        RegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phone = RegisterPhone.getText().toString();
                Password = RegisterPassword.getText().toString();
                Name = RegisterName.getText().toString();
                
                CreateNewAccount(Phone,Password,Name);
            }
        });
    }

    private void CreateNewAccount(String phone, String password, String name) {
        //using api hereeeeeeee & validasi manjah
        if(TextUtils.isEmpty((phone))){
            Toast.makeText(this, "Please enter your phone number..", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please enter your password...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please enter your name...", Toast.LENGTH_SHORT).show();
        }else{
            //buat akun
            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //checking data udh ada belom
                    if(!(snapshot.child("Users").child(phone).exists()))
                    {
                        //kalau tidak ada yang pake kita buat akun didatabasenya
                        HashMap<String, Object> userdata = new HashMap<>();
                        userdata.put("phone",phone);
                        userdata.put("password",password);
                        userdata.put("name",name);
                        userdata.put("nsong","2");
                        userdata.put("token","2");
                        userdata.put("theme","0"); //light them, 01 dark them

                        mRef.child("Users").child(phone).updateChildren(userdata).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful())
                                {
                                    Toast.makeText(RegisterActivity.this, "Registration Success", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(RegisterActivity.this, "Coba lagi yuk <3 tapi nanti", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else
                    {
                        //kalau udah ada yang pake
                        Toast.makeText(RegisterActivity.this, "User with this number already exist", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}