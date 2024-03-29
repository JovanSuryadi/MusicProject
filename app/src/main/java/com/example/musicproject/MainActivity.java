package com.example.musicproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    //Refrence UI elements

    LinearLayout L1,L2;
    TextView tv;

    Animation DowntoTop,Fade;

    //animasi text loading ehe
    //fading animation on linear layout 2, and down to top animation
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        L1 = (LinearLayout)findViewById(R.id.I1);
        L2 = (LinearLayout) findViewById(R.id.I2);

        tv = (TextView) findViewById(R.id.tag);

        DowntoTop = AnimationUtils.loadAnimation(this,R.anim.downtotop);
        Fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        L2.setAnimation(DowntoTop);
        tv.setAnimation(Fade);

        final Intent i = new Intent(MainActivity.this,HomeActivity.class);

        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    sleep(5000);//jeda buat pindah ke main dari loading screen
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    startActivity(i);
                    finish();
                }
            }
        };thread.start();


    }
}