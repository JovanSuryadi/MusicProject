package com.example.musicproject;



import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicproject.Model.SongModel;
import com.example.musicproject.Retrofit.APIclient;
import com.example.musicproject.Retrofit.APIinterface;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongsListActivity extends AppCompatActivity {

    private MyAdapter myAdapter;
    private RecyclerView recyclerView;

    TextView TokenTV;
    Button showMore;
    String phone;
    DatabaseReference mRef;
    String tokenValues="";
    String nsong="";
    Integer totalSong;

    String themeValue;
    RelativeLayout songListRL;

    @Override
    protected void onResume() {
        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        mRef = FirebaseDatabase.getInstance().getReference("Users");
        songListRL = (RelativeLayout)findViewById(R.id.songListRL);
        
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
            songListRL.setBackgroundResource(R.drawable.background);
        }
        else{
            songListRL.setBackgroundResource(R.drawable.background2);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_list);

        Intent i = getIntent();
        phone = i.getStringExtra("phone");
        TokenTV = (TextView) findViewById(R.id.token_main);
        showMore = (Button) findViewById(R.id.showmore);

        mRef = FirebaseDatabase.getInstance().getReference("Users");

        //init interface buat panggil dan nerima data dari API
        APIinterface service = APIclient.getRetrofitInstance().create(APIinterface.class);
        mRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tokenValues = snapshot.child("token").getValue().toString();
                nsong = snapshot.child("nsong").getValue().toString();
                TokenTV.setText("Tokens : "+ tokenValues);
                //set theme value on create method untuk better performance
                themeValue = snapshot.child("theme").getValue().toString();
                checkTheme(themeValue);
                //ini bakal fetch value dari firebase dan di set dalam text viewnya
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        showMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Integer.parseInt(tokenValues)>0){
                    //decrese token karena dh dpake buat play
                    tokenValues = String.valueOf((Integer.parseInt(tokenValues))- 1);
                    nsong = String.valueOf((Integer.parseInt(nsong)) + 1);

                    mRef.child(phone).child("token").setValue(tokenValues);
                    mRef.child(phone).child("nsong").setValue(nsong);

                }else{
                    Toast.makeText(SongsListActivity.this, "Please purchase tokens...beli token lgi yaa",Toast.LENGTH_LONG).show();
                }
            }
        });

        Call<List<SongModel>> call = service.getStudio();

        call.enqueue(new Callback<List<SongModel>>() {
            @Override
            public void onResponse(Call<List<SongModel>> call, Response<List<SongModel>> response) {
                loadDataList(response.body());
            }

            @Override
            public void onFailure(Call<List<SongModel>> call, Throwable t) {

            }
        });

    }

    private void loadDataList(List<SongModel> songsList) {

        recyclerView = findViewById(R.id.myRecylerView);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SongsListActivity.this);
        recyclerView.setLayoutManager(layoutManager);


        mRef.child(phone).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 //fetch nsongvalue here
                totalSong = Integer.parseInt(snapshot.child("nsong").getValue().toString());
                myAdapter = new MyAdapter(songsList.subList(0,totalSong),phone);
                recyclerView.setAdapter(myAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        //set adapter to recylerview

    }
}