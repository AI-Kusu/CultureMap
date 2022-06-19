package com.st17.culturemap.objects;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.st17.culturemap.R;

import java.util.List;

public class ImageRVAdapter extends RecyclerView.Adapter<ImageRVAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    List<String> URLs;

    public ImageRVAdapter(LayoutInflater inflater, List<String> URLs) {
        this.inflater = inflater;
        this.URLs = URLs;
    }

    @NonNull
    @Override
    public ImageRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.bottomsheet_rvimage, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageRVAdapter.ViewHolder holder, int position) {
        String URL = URLs.get(position);

        Glide.with(inflater.getContext()).load(URL).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return URLs.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(@NonNull View view) {
            super(view);
            imageView = view.findViewById(R.id.BottomSheet_RVImageView);
        }
    }
}
