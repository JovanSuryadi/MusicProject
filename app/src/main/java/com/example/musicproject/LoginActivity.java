package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText LoginPhone, LoginPassword;
    Button LoginButton;
    String Phone,Password;//temp untuk data login
    String userPassword; //buat ambil pass yang ada di database

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //loading bar
        LoginPhone = (EditText) findViewById(R.id.login_phone);
        LoginPassword = (EditText) findViewById(R.id.login_password);
        LoginButton = (Button) findViewById(R.id.login_btn );

        //harusnya ada loading bar :))

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Phone = LoginPhone.getText().toString();
                Password = LoginPassword.getText().toString();
                LoginAccount(Phone,Password);
            }
        });
    }

    private void LoginAccount(String phone, String password) {

        //validasi textnya ga bole empty
        if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "Please enter phone number !!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Password)){
            Toast.makeText(this, "Please enter password !!",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, "Please wait...", Toast.LENGTH_SHORT).show();

            final DatabaseReference mRef;
            mRef = FirebaseDatabase.getInstance().getReference();

            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //kalau nomor telp diisi maka kita akan cek sama yang ada di database ada/tidak,
                    //kalau ada maka kita bakal terima password di temp "userPassword"
                    //abis itu bakal cocokin password temp sama yang di edittext
                    //kalau match maka direct ke music activityy
                    if(snapshot.child("Users").child(Phone).exists()){
                        //kalau ada kita ngambil pass di database
                        userPassword = snapshot.child("Users").child(Phone).child("password").getValue().toString(); //ini pasttin sama kayak di firedatabase
                        
                        //baru di cek 
                        if(Password.equals(userPassword)){
                            //kalau bener ya pergi ke music activity
                            Intent i = new Intent(LoginActivity.this, MusicLibActivity.class);
                            i.putExtra("phone",phone);
                            startActivity(i);
                            Toast.makeText(LoginActivity.this, "Welcomeeeee !!" , Toast.LENGTH_SHORT).show();


                        }
                        else{
                            //kalau usernya ada tapi passnya salah
                            Toast.makeText(LoginActivity.this, "Please enter valid password !", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else{
                        //kalau usernya ga ada
                        Toast.makeText(LoginActivity.this, "User with this number does not exist !", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}