package com.jojo.devicetool.ui.device;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.BaseAdapter;
import com.jojo.devicetool.widget.CommonItem;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: Jojo on 2019/3/25
 */
public class DeviceInfoAdapter extends BaseAdapter<DeviceItem> {
    final int TYPE_TITLE = 1;
    final int TYPE_NORMAL = 2;


    public DeviceInfoAdapter(List<DeviceItem> mData, Context mContext) {
        super(mData, mContext);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (i == TYPE_TITLE) {
            return new TitleHolder(mInflater.inflate(R.layout.item_device_title, viewGroup, false));
        }
        return new NormalHolder(mInflater.inflate(R.layout.item_device_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        DeviceItem item = mData.get(i);
        if (getItemViewType(i) == TYPE_TITLE) {
            TitleHolder holder = (TitleHolder) viewHolder;
            holder.tvDeviceInfoTitle.setText(item.getLabel());

        } else {
            NormalHolder holder = (NormalHolder) viewHolder;
            holder.ciDevice.setLeftText(item.getLabel());
            holder.ciDevice.setRightText(item.getValue());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(position).isTitle()) {
            return TYPE_TITLE;
        }
        return TYPE_NORMAL;
    }

    static class NormalHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ci_device)
        CommonItem ciDevice;

        public NormalHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class TitleHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device_info_title)
        TextView tvDeviceInfoTitle;

        public TitleHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
