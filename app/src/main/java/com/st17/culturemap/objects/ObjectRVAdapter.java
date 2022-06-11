package com.st17.culturemap.objects;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.st17.culturemap.R;

import java.util.List;

public class ObjectRVAdapter extends RecyclerView.Adapter<ObjectRVAdapter.ViewHolder> {

    private List<PlaceObject> objects;
    private LayoutInflater inflater;

            // data is passed into the constructor
            public ObjectRVAdapter(Context context, List<PlaceObject> objects) {
            this.inflater = LayoutInflater.from(context);
            this.objects = objects;
            }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.rv_object, parent, false);
            return new ViewHolder(view);
            }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ObjectRVAdapter.ViewHolder holder, int position) {
                PlaceObject object = objects.get(position);
                holder.nameView.setText(object.name);
                holder.capitalView.setText(object.description);
            }

    // total number of rows
    @Override
    public int getItemCount() {
                return objects.size();
            }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder{
        final TextView nameView, capitalView;
        ViewHolder(View view){
            super(view);
            nameView = view.findViewById(R.id.name);
            capitalView = view.findViewById(R.id.description);
        }
    }
}