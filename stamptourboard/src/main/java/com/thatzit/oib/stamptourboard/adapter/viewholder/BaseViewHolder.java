package com.thatzit.oib.stamptourboard.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by inbum on 2017-02-02.
 */

public abstract class BaseViewHolder<ITEM> extends RecyclerView.ViewHolder {

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void onBindView(ITEM item, int position);

}
