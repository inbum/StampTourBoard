package com.thatzit.oib.stamptourboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thatzit.oib.stamptourboard.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by ib on 2016-11-07.
 */

public class PostWriteImagesAdapter extends RecyclerView.Adapter<PostWriteImagesAdapter.ViewHolder> {

    Context mContext;

    private List<File> selectedImages = new ArrayList<>();

    @Override
    public PostWriteImagesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_postwrite_image, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Glide.with(mContext)
                .load(selectedImages.get(position))
                .into( holder.imageView );

    }

    public void setSelectedImages(List<File> selectedImages) {
        this.selectedImages.clear();
        this.selectedImages.addAll(selectedImages);
    }

    @Override
    public int getItemCount() {
        return selectedImages.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;


        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView_item_postwrite_image);
        }
    }
}
