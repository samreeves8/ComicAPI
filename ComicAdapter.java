package com.example.comicapi;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.URL;

public class ComicAdapter extends RecyclerView.Adapter{


    ComicAdapterListener comicAdapterListener;
    ComicSQLiteHelper comicSQLiteHelper;

    public interface ComicAdapterListener {
        public void click(int position);
    }

    public ComicAdapter(ComicSQLiteHelper comicSQLiteHelper){
        this.comicSQLiteHelper = comicSQLiteHelper;
    }

    class ComicViewHolder extends RecyclerView.ViewHolder {

        WebView webView;
        TextView textViewTop;
        TextView textViewBottom;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            webView = itemView.findViewById(R.id.webView);
            textViewTop = itemView.findViewById(R.id.textViewTop);
            textViewBottom = itemView.findViewById(R.id.textViewBottom);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    Intent intent = new Intent(view.getContext(), DetailActivity.class);
                    intent.putExtra("position", position);
                    view.getContext().startActivity(intent);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyComic myComic = comicSQLiteHelper.getComic(position);
        ComicViewHolder comicViewHolder = (ComicViewHolder) holder;
        comicViewHolder.webView.loadUrl(myComic.getUrl());
        comicViewHolder.textViewTop.setText(String.valueOf(myComic.getNumber()));
        comicViewHolder.textViewBottom.setText(myComic.getName());
        //holder.itemView.setOnClickListener(new View.OnClickListener() {
            /*@Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailActivity.class);
                intent.putExtra("url", comicViewHolder.webView.getUrl());
                Log.v("Here", comicViewHolder.webView.getUrl());
                view.getContext().startActivity(intent);
            }*/
        ///});
    }

    @Override
    public int getItemCount() {
        return comicSQLiteHelper.getCount();
    }
}
