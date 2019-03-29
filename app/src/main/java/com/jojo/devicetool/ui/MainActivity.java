package com.jojo.devicetool.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.device.DeviceFragment;
import com.jojo.devicetool.ui.location.LocationFragment;
import com.jojo.devicetool.ui.map.MapActivity;
import com.jojo.devicetool.ui.network.NetworkTestActivity;
import com.jojo.devicetool.ui.sim.SimFragment;
import com.jojo.devicetool.ui.wifi.WifiFragment;
import com.jojo.devicetool.util.PermissionUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.tl)
    TabLayout tl;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_map)
    TextView tvMap;
    @BindView(R.id.tv_net_work_test)
    TextView tvNetworkTest;
    private List<String> mTabs = new ArrayList<>();
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if (!PermissionUtil.permissionGranted(this) && Build.VERSION.SDK_INT >= 23) {
            PermissionUtil.checkPermissions(this);
        } else {
            init();
        }
        tvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapActivity.class);
                startActivity(intent);
            }
        });
        tvNetworkTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NetworkTestActivity.class);
                startActivity(intent);
            }
        });
        getSimInfo();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.PERMISSION_REQUEST_CODE:
                if (!PermissionUtil.permissionGranted(this)) {
                    Toast.makeText(MainActivity.this, "需要权限", Toast.LENGTH_SHORT).show();
                } else {
                    // launch();
                    init();
                }
        }
    }

    private void init() {
        mFragments.add(new SimFragment());
        mFragments.add(new WifiFragment());
        mFragments.add(new LocationFragment());
        mFragments.add(new DeviceFragment());
        mTabs.add("SIM");
        mTabs.add("WIFI");
        mTabs.add("GPS");
        mTabs.add("DEVICE");
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get(position);
            }

            @Override
            public int getCount() {
                return mFragments.size();
            }
        });
        for (int i = 0, len = mTabs.size(); i < len; i++) {
            tl.addTab(tl.newTab());
        }
        tl.setupWithViewPager(vp);
        for (int i = 0, len = mTabs.size(); i < len; i++) {
            tl.getTabAt(i).setText(mTabs.get(i));
        }
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private void getSimInfo() {
        SubscriptionManager mSubscriptionManager = (SubscriptionManager) getSystemService(Context.TELEPHONY_SUBSCRIPTION_SERVICE);
        int simCount = mSubscriptionManager.getActiveSubscriptionInfoCountMax();//手机SIM卡数
        int usedSimCount = mSubscriptionManager.getActiveSubscriptionInfoCount();//手机使用的SIM卡数
        List<SubscriptionInfo> activeSubscriptionInfoList = mSubscriptionManager.getActiveSubscriptionInfoList();//手机SIM卡信息
        Log.e("xcc", "xx");
    }
}
