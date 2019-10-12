package com.example.procontexttestwork;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class RVPhotoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG = "RVPhotoAdapter";
    private Context mContext;
    private ArrayList<Photo> array;
    private int arraySize;
    private int recyclerSize = 10;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 0;


    public RVPhotoAdapter(ArrayList<Photo> a, Context mContext){
        array = a;
        this.mContext = mContext;
        arraySize = array.size();



    }

    private class PhotoViewHolder extends RecyclerView.ViewHolder {
        public ImageView photoView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoView = itemView.findViewById(R.id.photo_view);
        }
    }

    private class ButtonViewHolder extends RecyclerView.ViewHolder {
        public Button button;

        public ButtonViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.more_button);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_ITEM){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_photo_item, viewGroup, false);
            return new PhotoViewHolder(v);
        }

        else if (viewType == TYPE_FOOTER){
            View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.more_button, viewGroup, false);
            return new ButtonViewHolder(v);
        }
        else return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int i) {
        if(holder instanceof ButtonViewHolder) {
            final ButtonViewHolder buttonHolder = (ButtonViewHolder) holder;
            buttonHolder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(arraySize - recyclerSize >=10){
                        recyclerSize +=10;
                        notifyDataSetChanged();
                    } else if (arraySize - recyclerSize == 0) {
                        buttonHolder.button.setVisibility(View.GONE);
                    }
                    else if (arraySize - recyclerSize < 10){
                        recyclerSize =+ (arraySize - recyclerSize);
                        notifyDataSetChanged();
                    }

                }
            });
        }
        else if (holder instanceof PhotoViewHolder){
            PhotoViewHolder photoHolder = (PhotoViewHolder) holder;
            final Photo item = array.get(i);
            if (item.getUrl() != null && !item.getUrl().isEmpty()) {
                Picasso.get()
                        .load(item.getUrl())
                        .fit()
                        .centerCrop()
                        .into(photoHolder.photoView);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == recyclerSize)
            return TYPE_FOOTER;
        else return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return recyclerSize + 1;
    }

}