package com.thatzit.oib.stamptourboard.adapter.viewholder;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thatzit.oib.stamptourboard.R;


/**
 * Created by inbum on 2017-02-02.
 */

public class PostBoardCommentNoneHolder extends BaseViewHolder<Object> {


    public static PostBoardCommentNoneHolder newInstance(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_postboardcomment_none, parent, false);
        return new PostBoardCommentNoneHolder(itemView);
    }

    public PostBoardCommentNoneHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void onBindView(Object object, final int position) {

    }

}
