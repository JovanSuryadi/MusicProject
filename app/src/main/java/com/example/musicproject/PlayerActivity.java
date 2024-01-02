package com.example.musicproject;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {

    ImageView CoverImage;
    TextView SongTitile, SongArtist;
    //button for next and prev
    Button Play,Pause,Next,Prev, Share;
    MediaPlayer mediaPlayer; //untuk mainin musicnya
    SeekBar seekBar;
    TextView Pass,Due;//buat nampilin udah menit brp
    Handler handler;

    ArrayList<String> arraylistUrl;
    ArrayList<String> arraylistSong;
    ArrayList<String> arraylistArtist;
    ArrayList<String> arraylistImage;

    Animation uptodown, fade;
    Integer Position;
    String phone;
    DatabaseReference mRef;
    RelativeLayout playerRL;
    String themeValue;
    String shareUrl;

    @Override
    protected void onResume() {
        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        playerRL = (RelativeLayout) findViewById(R.id.playerRL);

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
            playerRL.setBackgroundResource(R.drawable.background);
        }
        else{
            playerRL.setBackgroundResource(R.drawable.background2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        fade = AnimationUtils.loadAnimation(this,R.anim.fade);

        CoverImage = (ImageView) findViewById(R.id.coverImageView);
        SongTitile = (TextView) findViewById(R.id.song_title);


        Play = (Button)findViewById(R.id.playBtn);
        Pause = (Button)findViewById(R.id.pauseBtn);
        seekBar = (SeekBar) findViewById(R.id.seek_bar);
        Pass = (TextView) findViewById(R.id.tv_pass);
        Due = (TextView)findViewById(R.id.tv_due);

        Next = (Button)findViewById(R.id.next);
        Prev = (Button)findViewById(R.id.prev);
        Share = (Button) findViewById(R.id.share);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("label", shareUrl);
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(PlayerActivity.this,"Song url : " + shareUrl+ "copied to clibboard.." , Toast.LENGTH_LONG).show();
            }
        });

        //refrences untuk next dan prev
        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cek kondisi dipencet/ga biar pas di next kalau playlistnya abis bakal load ke 1 lgi
                if(arraylistUrl.size() == Position+1){
                    Position = 0;
                    init(arraylistSong.get(Position),arraylistArtist.get(Position),arraylistUrl.get(Position),arraylistImage.get(Position));
                }
                else{
                    //ini kondisi dimana user ngga lagi di last song, jadi langsung next songnya aja
                    Position = Position + 1;
                    init(arraylistSong.get(Position),arraylistArtist.get(Position),arraylistUrl.get(Position),arraylistImage.get(Position));
                }
            }
        });
        Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if user at first song and click prev maka akan main last song
                if(Position == 0){
                    Position = arraylistUrl.size() - 1;
                    init(arraylistSong.get(Position),arraylistArtist.get(Position),arraylistUrl.get(Position),arraylistImage.get(Position));
                }
                else{
                    Position = Position - 1;
                    init(arraylistSong.get(Position),arraylistArtist.get(Position),arraylistUrl.get(Position),arraylistImage.get(Position));

                }



            }
        });

        //media player
        mediaPlayer = new MediaPlayer();

        handler = new Handler();

        //parsing in intent untuk ambil datanya dari kelas adapter
        Intent i = getIntent();
        
        String song = i.getStringExtra("song");
        String url = i.getStringExtra("url");
        String artist = i.getStringExtra("artist");
        String cover_image = i.getStringExtra("cover_image");

        arraylistUrl = i.getStringArrayListExtra("arrayListUrl");
        arraylistSong = i.getStringArrayListExtra("arraylistSong ");
        arraylistArtist = i.getStringArrayListExtra("arraylistArtist");
        arraylistImage = i.getStringArrayListExtra(" arraylistImage");
        Position = Integer.parseInt(i.getStringExtra("position"));



        //cover image url
        
        //pastiin namanya sama :D
        Toast.makeText(this, song, Toast.LENGTH_SHORT).show();

        //untuk tampilan textnya
        init(song,artist,url,cover_image);
        Pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pause();
            }
        });

        Play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();
            }
        });

        //create method to initiliaze seekbar
        initilizeSeekBar();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    mediaPlayer.seekTo(progress*1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void init(String song, String artist, String url, String cover_image) {
        SongTitile.setText(song);
        SongArtist.setText(artist);

        //set animationnya
        SongTitile.setAnimation(fade);
        SongArtist.setAnimation(fade);

        //ini fungsi buat masukin gambar :>
        Glide.with(this)
                .load(cover_image)
                .override(300,200)
                .into(CoverImage);

        CoverImage.setAnimation(uptodown);

        //setting url lagunya untuk di play pake media player
        //urlny didapeting dari intennt

        //check masih ada lagu yang di play ga kalau masih ada kita harus stop dulu
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }

        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        mediaPlayer.start();
        initilizeSeekBar();

        shareUrl = url;
        //so every time new song will play then its url will be set to shareURL
    }

    private void initilizeSeekBar() {
        seekBar.setMax(mediaPlayer.getDuration()/1000); //set limit dari sebuah lagu

        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mCurrentPosition);

                    String out = String.format("%02d:%02d", seekBar.getProgress() / 60, seekBar.getProgress() % 60);
                    //ini bakal store string of time dan second

                    Pass.setText(out);

                    //bikin buat sisa brp menit
                    Integer diffrences = mediaPlayer.getDuration()/1000 - mediaPlayer.getCurrentPosition()/1000;

                    String out2 = String.format("%02d:%02d", diffrences/60, diffrences %60);
                    Due.setText(out2);
                }
                handler.postDelayed(this, 1000);
            }
        });
    }

    private void play() {
        //buat validasi play button pas d pencet maka pause buttonnya invisible
        mediaPlayer.start();
        Play.setVisibility(View.INVISIBLE);
        Pause.setVisibility(View.VISIBLE);

    }

    private void pause() {
        //sebaliknya dari play
        mediaPlayer.pause();
        Play.setVisibility(View.VISIBLE);
        Pause.setVisibility(View.INVISIBLE);
    }


}