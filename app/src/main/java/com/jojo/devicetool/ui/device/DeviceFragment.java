package com.jojo.devicetool.ui.device;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.BaseFragment;
import com.jojo.devicetool.widget.CommonItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.os.BatteryManager.BATTERY_PLUGGED_WIRELESS;
import static android.os.BatteryManager.BATTERY_STATUS_DISCHARGING;
import static android.os.BatteryManager.EXTRA_PLUGGED;

// 充电器类型
// 充电器
// 其他
// USB
// 无线充电

/**
 * Description: Jojo on 2019/3/25
 */
public class DeviceFragment extends BaseFragment {
    private static final int BATTERY_PLUGGED_ANY = -1000;
    Unbinder unbinder;
    @BindView(R.id.ci_brand)
    CommonItem ciBrand;
    @BindView(R.id.ci_model)
    CommonItem ciModel;
    @BindView(R.id.ci_hardware)
    CommonItem ciHardware;
    @BindView(R.id.ci_board)
    CommonItem ciBoard;
    @BindView(R.id.ci_abi)
    CommonItem ciAbi;
    @BindView(R.id.ci_id)
    CommonItem ciId;
    @BindView(R.id.ci_display)
    CommonItem ciDisplay;
    @BindView(R.id.ci_product)
    CommonItem ciProduct;
    @BindView(R.id.ci_device)
    CommonItem ciDevice;
    @BindView(R.id.ci_jidai)
    CommonItem ciJidai;
    @BindView(R.id.ci_serial)
    CommonItem ciSerial;
    @BindView(R.id.ci_screen_size)
    CommonItem ciScreenSize;
    @BindView(R.id.ci_screen_pixels)
    CommonItem ciScreenPixels;
    @BindView(R.id.ci_dpi)
    CommonItem ciDpi;
    @BindView(R.id.ci_ram)
    CommonItem ciRam;
    @BindView(R.id.ci_rom)
    CommonItem ciRom;
    @BindView(R.id.ci_bat_status)
    CommonItem ciBatStatus;
    @BindView(R.id.ci_bat_type)
    CommonItem ciBatType;
    @BindView(R.id.ci_bat_health)
    CommonItem ciBatHealth;
    @BindView(R.id.ci_bat_power)
    CommonItem ciBatPower;
    @BindView(R.id.ci_bat_percent)
    CommonItem ciBatPercent;
    @BindView(R.id.ci_bat_u)
    CommonItem ciBatU;
    @BindView(R.id.ci_bat_temp)
    CommonItem ciBatTemp;
    @BindView(R.id.ci_bat_volume)
    CommonItem ciBatVolume;
    @BindView(R.id.ci_sdk_int)
    CommonItem ciSdkInt;
    @BindView(R.id.ci_release)
    CommonItem ciRelease;
    @BindView(R.id.ci_version_name)
    CommonItem ciVersionName;
    @BindView(R.id.ci_bootloader)
    CommonItem ciBootloader;
    @BindView(R.id.ci_java_vm)
    CommonItem ciJavaVm;
    @BindView(R.id.ci_os_arc)
    CommonItem ciOsArc;
    @BindView(R.id.ci_os_version)
    CommonItem ciOsVersion;
    @BindView(R.id.ci_runtime_version)
    CommonItem ciRuntimeVersion;
    private int BatteryN;       //目前电量
    private int BatteryV;       //电池电压
    private double BatteryT;        //电池温度
    private String BatteryStatus;   //电池状态
    private String BatteryTemp;     //电池使用情况
    private String BatteryTechnology;//电池类型
    private String BatteryChargeStatus;//充电情况

    @Override

    protected int getLayout() {
        return R.layout.fragment_device;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void initView() {
        ciBrand.setRightText(DeviceUtil.getBrand());
        ciModel.setRightText(DeviceUtil.getModel());
        ciHardware.setRightText(DeviceUtil.getHardwareInfo());
        ciBoard.setRightText(DeviceUtil.getBoard());
        ciAbi.setRightText(DeviceUtil.getABI());
        ciId.setRightText(DeviceUtil.getID());
        ciDisplay.setRightText(DeviceUtil.getDisPlay());
        ciProduct.setRightText(DeviceUtil.getProduct());
        ciDevice.setRightText(DeviceUtil.getDevice());
        ciJidai.setRightText(DeviceUtil.getJidai());
        ciSerial.setRightText(DeviceUtil.getDeviceSerial());
        ciScreenSize.setRightText(DeviceUtil.getScreenSize(mActivity));
        ciScreenPixels.setRightText(DeviceUtil.getScreenPixel(mActivity));
        ciDpi.setRightText(DeviceUtil.getScreenDpi(mActivity));
        ciRam.setRightText(DeviceUtil.getRAMInfo(mActivity));
        ciRom.setRightText(DeviceUtil.getROMInfo(mActivity, 0));


        ciSdkInt.setRightText(DeviceUtil.getSDK_INT() + "");
        ciRelease.setRightText(DeviceUtil.getRelease());
        ciVersionName.setRightText(DeviceUtil.getVersionName());
        ciBootloader.setRightText(DeviceUtil.getBootLoader());
        ciJavaVm.setRightText(DeviceUtil.getJavaVm());
        ciOsArc.setRightText(DeviceUtil.getOSArch());
        ciOsVersion.setRightText(DeviceUtil.getOSArchVersion());
        //ciRuntimeVersion.setRightText(DeviceUtil);

    }

    @Override
    protected void initData() {
        getActivity().registerReceiver(mBatInfoReceiver,
                new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        getActivity().unregisterReceiver(mBatInfoReceiver);
    }

    /* 创建广播接收器 */
    public BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            if (!getUserVisibleHint()) {
                return;
            }
            String action = intent.getAction();

            //如果捕捉到的action是ACTION_BATTERY_CHANGED， 就运行onBatteryInfoReceiver()

            if (Intent.ACTION_BATTERY_CHANGED.equals(action)) {
                int plugged = intent.getIntExtra(EXTRA_PLUGGED, BATTERY_PLUGGED_ANY);
                BatteryN = intent.getIntExtra("level", 0);    //目前电量（0~100）
                BatteryV = intent.getIntExtra("voltage", 0);  //电池电压(mv)
                BatteryT = intent.getIntExtra("temperature", 0);  //电池温度(数值)
                double T = BatteryT / 10.0; //电池摄氏温度，默认获取的非摄氏温度值，需做一下运算转换
                BatteryTechnology = intent.getStringExtra("technology");//电池类型 Li-ion
                int status = intent.getIntExtra("status", BatteryManager.BATTERY_STATUS_UNKNOWN);
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING:
                        BatteryStatus = "充电状态";
                        break;
                    case BATTERY_STATUS_DISCHARGING:
                        BatteryStatus = "放电状态";
                        break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                        BatteryStatus = "未充电";
                        break;
                    case BatteryManager.BATTERY_STATUS_FULL:
                        BatteryStatus = "充满电";
                        break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN:
                        BatteryStatus = "未知道状态";
                        break;

                }
                switch (intent.getIntExtra("health", BatteryManager.BATTERY_HEALTH_UNKNOWN)) {
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN:
                        BatteryTemp = "未知错误";
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        BatteryTemp = "状态良好";
                        break;
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        BatteryTemp = "电池没有电";

                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        BatteryTemp = "电池电压过高";
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        BatteryTemp = "电池过热";
                        break;
                }
                switch (plugged) {
                    case BatteryManager.BATTERY_PLUGGED_AC://充电器
                        BatteryChargeStatus = "充电器";
                        break;
                    case BatteryManager.BATTERY_PLUGGED_USB://USB
                        BatteryChargeStatus = "USB";
                        break;
                    case BATTERY_PLUGGED_WIRELESS://无线
                        BatteryChargeStatus = "无线充电";
                        break;
                    default:
                        if (status == BATTERY_STATUS_DISCHARGING) {//放电
                            BatteryChargeStatus = "电池";
                        } else {
                            BatteryChargeStatus = "异常";
                        }
                        break;
                }

                ciBatStatus.setRightText(BatteryStatus);
                ciBatType.setRightText(BatteryTechnology);
                ciBatHealth.setRightText(BatteryTemp);
                ciBatPower.setRightText(BatteryChargeStatus);
                ciBatPercent.setRightText(BatteryN + "%");
                double U = Double.parseDouble(BatteryV + "");
                ciBatU.setRightText(U / 1000 + "V");
                ciBatTemp.setRightText(BatteryT / 10 + "℃");
                ciBatVolume.setRightText(DeviceUtil.getBatteryCapacity(mActivity));
            }
        }
    };
}