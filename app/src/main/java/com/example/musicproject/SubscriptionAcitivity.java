package com.example.musicproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SubscriptionAcitivity extends AppCompatActivity {
    
    String phone;
    DatabaseReference mRef;
    RelativeLayout subscriptionRL;
    String themeValue;

    @Override
    protected void onResume() {
        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        subscriptionRL = (RelativeLayout) findViewById(R.id.subscriptionRL);
        
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                themeValue = snapshot.child(phone).child("theme").getValue().toString();
                checkTheme(themeValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        super.onResume();
    }

    private void checkTheme(String themeValue) {
        if(themeValue.equals("0")){
            subscriptionRL.setBackgroundResource(R.drawable.background);
        }
        else{
            subscriptionRL.setBackgroundResource(R.drawable.background2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_acitivity);
    }
}