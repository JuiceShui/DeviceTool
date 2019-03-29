package com.jojo.devicetool.ui.wifi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.BaseAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Description: Jojo on 2019/3/27
 */
public class DeviceAdapter extends BaseAdapter<IP_MAC> {
    private String mLocalIp;
    private String mGateIp;

    public DeviceAdapter(List<IP_MAC> mData, Context mContext, String mLocalIp, String mGateIp) {
        super(mData, mContext);
        this.mLocalIp = mLocalIp;
        this.mGateIp = mGateIp;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DeviceHolder(mInflater
                .inflate(R.layout.item_wifi_device, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        DeviceHolder holder = (DeviceHolder) viewHolder;
        IP_MAC ip_mac = mData.get(position);
        if (ip_mac != null) {
            holder.tvDeviceIp.setText(String.format(
                    mContext.getResources().getString(R.string.ip_address), ip_mac.getmIp()));
            holder.tvDeviceMac.setText(String.format(
                    mContext.getResources().getString(R.string.mac_address), ip_mac.getmMac()));
            if (ip_mac.getmIp().equals(mLocalIp)) {
                holder.tvDeviceName.setText(mContext.getString(R.string.your_phone) + ip_mac.getmDeviceName());
            } else if (ip_mac.getmIp().equals(mGateIp)) {
                holder.tvDeviceName.setText(mContext.getString(R.string.gate_net));
            } else {
                holder.tvDeviceName.setText(ip_mac.getmDeviceName());
            }

            holder.tvDeviceCompany.setText(ip_mac.getmManufacture());
        }
    }

    static class DeviceHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_device_ip)
        TextView tvDeviceIp;
        @BindView(R.id.tv_device_mac)
        TextView tvDeviceMac;
        @BindView(R.id.tv_device_name)
        TextView tvDeviceName;
        @BindView(R.id.tv_device_company)
        TextView tvDeviceCompany;

        public DeviceHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
