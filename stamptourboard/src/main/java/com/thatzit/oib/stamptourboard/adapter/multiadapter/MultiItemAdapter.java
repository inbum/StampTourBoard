package com.thatzit.oib.stamptourboard.adapter.multiadapter;

import android.support.v7.widget.RecyclerView;

import com.thatzit.oib.stamptourboard.adapter.viewholder.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by inbum on 2017-02-02.
 */

public abstract class MultiItemAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<Row<?>> mRows = new ArrayList<>();

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBindView(getItem(position), position);
    }

    @SuppressWarnings("unchecked")
    public <ITEM> ITEM getItem(int position) {
        return (ITEM) mRows.get(position).getItem();
    }

    public void addRow(Row<?> row) {
        this.mRows.add(row);
    }

    public void addRows(List<Row<?>> rows) {
        this.mRows.addAll(rows);
    }

    public void setRow(int position, Row<?> row) {
        this.mRows.set(position, row);
    }

    public void setRows(List<Row<?>> mRows) {
        clear();
        this.mRows.addAll(mRows);
    }

    public List<Row<?>> getRows() {
        return mRows;
    }

    public void remove(int position) {
        if (getItemCount() - 1 < position) {
            return;
        }
        this.mRows.remove(position);
    }

    public void remove(Row<?> row) {
        this.mRows.remove(row);
    }

    public void clear() {
        this.mRows.clear();
    }

    @Override
    public int getItemCount() {
        return mRows.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mRows.get(position).getItemViewType();
    }

    public static class Row<ITEM> {
        private ITEM item;
        private int itemViewType;

        private Row(ITEM item, int itemViewType) {
            this.item = item;
            this.itemViewType = itemViewType;
        }

        public static <T> Row<T> create(T item, int itemViewType) {
            return new Row<>(item, itemViewType);
        }

        public ITEM getItem() {
            return item;
        }

        public int getItemViewType() {
            return itemViewType;
        }
    }

}