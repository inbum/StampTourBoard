package com.thatzit.oib.stamptourboard.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.thatzit.oib.stamptourboard.R;
import com.thatzit.oib.stamptourboard.helper.Constants;
import com.thatzit.oib.stamptourboard.model.FileModel;

import java.util.ArrayList;


/**
 * Created by ib on 2016-11-07.
 */

public class PostBoardListItemImageAdapter extends RecyclerView.Adapter<PostBoardListItemImageAdapter.ViewHolder> {

    Context mContext;
    ArrayList<FileModel> images = new ArrayList<>();

    public PostBoardListItemImageAdapter(Context mContext, ArrayList<FileModel> images) {
        this.mContext = mContext;
        this.images = images;
    }

    @Override
    public PostBoardListItemImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_postboardlistitem_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String imagePath = Constants.BOARD_API_URL + "files/" + images.get(position).getContainer() + "/" + images.get(position).getName();
        Glide.with(mContext)
                .load(imagePath)
                .into(holder.imageView);
    }



    @Override
    public int getItemCount() {
        return images == null ? 0 : images.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public ViewHolder(View view) {
            super(view);
            imageView = (ImageView) view.findViewById(R.id.imageView_item_postboardlistitem);
        }
    }
}
