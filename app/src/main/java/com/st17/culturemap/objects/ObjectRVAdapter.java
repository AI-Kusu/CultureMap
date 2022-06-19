package com.st17.culturemap.objects;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.st17.culturemap.R;

import java.util.List;

public class ObjectRVAdapter extends RecyclerView.Adapter<ObjectRVAdapter.ViewHolder> {

    private final OnObjectClickListener onClickListener;
    private final List<PlaceObject> objects;
    private final LayoutInflater inflater;


            // data is passed into the constructor
        public ObjectRVAdapter(Context context, List<PlaceObject> objects, OnObjectClickListener onClickListener) {
            this.inflater = LayoutInflater.from(context);
            this.objects = objects;
            this.onClickListener = onClickListener;
        }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_object, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ObjectRVAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PlaceObject object = objects.get(position);
        holder.nameView.setText(object.name);
        holder.descriptionView.setText(object.descriptionShort);

        Glide.with(inflater.getContext()).load(object.imageURLs.get(0)).into(holder.imageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.onObjectClick(object, position);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return objects.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView nameView, descriptionView;
        final ImageView imageView;
        ViewHolder(View itemView){
            super(itemView);
            nameView = itemView.findViewById(R.id.name);
            descriptionView = itemView.findViewById(R.id.description);
            imageView = itemView.findViewById(R.id.RVObjectImageView);
        }
    }

    public interface OnObjectClickListener{
        void onObjectClick(PlaceObject object, int position);
    }
}