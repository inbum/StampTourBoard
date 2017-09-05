package com.thatzit.oib.stamptourboard.adapter.multiadapter;

import android.content.Context;
import android.view.ViewGroup;

import com.thatzit.oib.stamptourboard.StampTourBoardConfig;
import com.thatzit.oib.stamptourboard.adapter.ViewType;
import com.thatzit.oib.stamptourboard.adapter.viewholder.BaseViewHolder;
import com.thatzit.oib.stamptourboard.adapter.viewholder.PostBoardCommentNoneHolder;
import com.thatzit.oib.stamptourboard.adapter.viewholder.PostBoardCommentNormalHolder;
import com.thatzit.oib.stamptourboard.listeners.BoardListener;
import com.thatzit.oib.stamptourboard.model.PostsRes;


/**
 * Created by inbum on 2017-04-13.
 */

public class PostBoardCommentAdapter extends MultiItemAdapter{

    Context context;
    StampTourBoardConfig config;
    BoardListener boardListener;
    PostsRes.PostData postData;

    public PostBoardCommentAdapter(Context context, StampTourBoardConfig config, BoardListener boardListener, PostsRes.PostData postData) {
        this.context = context;
        this.config = config;
        this.boardListener = boardListener;
        this.postData = postData;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ViewType.POSTBOARD_COMMENT_NONE.getCode()) {
            return PostBoardCommentNoneHolder.newInstance(parent);
        } else {
            return PostBoardCommentNormalHolder.newInstance(context, parent, postData, config, boardListener);
        }
    }
}
