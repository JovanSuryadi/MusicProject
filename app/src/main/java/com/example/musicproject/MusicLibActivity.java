package com.example.musicproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MusicLibActivity extends AppCompatActivity {

    ImageView MusicLibraryIV, SubscriptionIV;
    Switch mSwitch;
    DatabaseReference mRef;
    RelativeLayout musicLibRl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_lib);
        Intent i = getIntent();
        String phone = i.getStringExtra("phone");

        MusicLibraryIV = (ImageView) findViewById(R.id.musicLibraryIV);
        SubscriptionIV = (ImageView) findViewById(R.id.subscriptionIV);

        mSwitch = (Switch) findViewById(R.id.switch1);
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        musicLibRl = (RelativeLayout)findViewById(R.id.musicLibrary);

        MusicLibraryIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MusicLibActivity.this,SongsListActivity.class);
                i.putExtra("phone", phone);
                startActivity(i);
            }
        });
        SubscriptionIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MusicLibActivity.this, SubscriptionAcitivity.class);
                i.putExtra("phone", phone);
                startActivity(i) ;
            }
        });

        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean ischecked) {
                if(ischecked){
                    musicLibRl.setBackgroundResource(R.drawable.background2);
                    mRef.child(phone).child("theme").setValue("1");
                }
                else{
                    musicLibRl.setBackgroundResource(R.drawable.background);
                    mRef.child(phone).child("theme").setValue("0");
                }
            }
        });
    }
}