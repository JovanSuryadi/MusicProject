package com.example.musicproject;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicproject.Model.SongModel;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.CustomViewHolder> {

    private List<SongModel> songList;
    private String phone;
    ArrayList<String> arrayListUrl = new ArrayList<>();
    ArrayList<String> arrayListSong = new ArrayList<>();
    ArrayList<String> arrayListArtist = new ArrayList<>();
    ArrayList<String> arrayListImage = new ArrayList<>();


    public MyAdapter(List<SongModel> songList, String phone){
        this.songList = songList;
        this.phone = phone;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.row_layout,parent,false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.CustomViewHolder holder, int position) {

        //access text viewnya disini untuk ngambil dari api (name,url,image dll)
        holder.songNameTextView.setText(songList.get(position).getSong());
        holder.songArtistTextView.setText(songList.get(position).getArtist());
        holder.songUrlTextView.setText(songList.get(position).getUrl());
        holder.songCoverImageTextView.setText(songList.get(position).getCover_image());


        //datanya ke dalem array list
        //jadi kalau arraylist url ga ada song url maka url bakal tambahin ke arrarylist
        if(!(arrayListUrl.contains(songList.get(position).getUrl())))
            arrayListUrl.add(songList.get(position).getUrl());
        if(!(arrayListSong.contains(songList.get(position).getSong())))
            arrayListSong.add(songList.get(position).getSong());
        if(!(arrayListArtist.contains(songList.get(position).getArtist())))
            arrayListArtist.add(songList.get(position).getArtist());
        if(!(arrayListImage.contains(songList.get(position).getCover_image())))
            arrayListImage.add(songList.get(position).getCover_image());






        //parsing string di intentnya'
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(),PlayerActivity.class);

                i.putExtra("song", holder.songNameTextView.getText().toString());
                i.putExtra("url", holder.songUrlTextView.getText().toString());
                i.putExtra("artist", holder.songArtistTextView.getText().toString());
                i.putExtra("cover_image", holder.songCoverImageTextView.getText().toString());

                //parsing arraynya dan posisi to next activity kita pake these detail to play song
                i.putExtra("arrayListUrl", arrayListUrl);
                i.putExtra("arrayListSong", arrayListSong);
                i.putExtra("arrayListArtist", arrayListArtist);
                i.putExtra(" arrayListImage", arrayListImage);
                i.putExtra("position", String.valueOf(position));
                //ini buat ketika user pencet masuk di queue buat lagu yang akan diputer

                i.putExtra("phone",phone);
                view.getContext().startActivity(i);
            }
        });


    }

    @Override
    public int getItemCount() {
        return songList.size(); //return ukuran yang harus disiapin sesuai jumlah lagu yang ada di API
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView songNameTextView, songArtistTextView, songUrlTextView, songCoverImageTextView;
        //to show all string which contain url for song and url for image and song name and artist
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);

            //we will access there text item in OnBindViewHolder

            songNameTextView = itemView.findViewById(R.id.title);
            songArtistTextView = itemView.findViewById(R.id.artist);
            songUrlTextView = itemView.findViewById(R.id.url);
            songCoverImageTextView = itemView.findViewById(R.id.cover);
        }
    }
}
