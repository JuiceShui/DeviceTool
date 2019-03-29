package com.jojo.devicetool.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

/**
 * Description: Jojo on 2019/3/25
 */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected List<T> mData;
    protected Context mContext;
    protected LayoutInflater mInflater;

    public BaseAdapter(List<T> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
