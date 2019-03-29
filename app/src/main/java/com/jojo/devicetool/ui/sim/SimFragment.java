package com.jojo.devicetool.ui.sim;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.telephony.CellInfo;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoWcdma;
import android.telephony.PhoneStateListener;
import android.telephony.SignalStrength;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jojo.devicetool.R;
import com.jojo.devicetool.ui.BaseFragment;
import com.jojo.devicetool.ui.device.DeviceUtil;
import com.jojo.devicetool.widget.CommonItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

import static android.content.Context.TELEPHONY_SERVICE;

/**
 * Description: Jojo on 2019/3/26
 */
public class SimFragment extends BaseFragment {
    @BindView(R.id.sim_imei)
    CommonItem simImei;
    @BindView(R.id.sim_company)
    CommonItem simCompany;
    @BindView(R.id.sim_imsi)
    CommonItem simImsi;
    @BindView(R.id.sim_iccid)
    CommonItem simIccid;
    @BindView(R.id.ll_sim_info)
    LinearLayout llSimInfo;
    @BindView(R.id.iv_sim_company_logo)
    ImageView ivSimCompanyLogo;
    @BindView(R.id.ci_sim_company)
    CommonItem ciSimCompany;
    @BindView(R.id.ci_sim_mcc)
    CommonItem ciSimMcc;
    @BindView(R.id.ci_sim_mnc)
    CommonItem ciSimMnc;
    public static final int NP_CELL_INFO_UPDATE = 1001;
    @BindView(R.id.ll_server)
    LinearLayout llServer;
    private SimActivity.PhoneInfoThread phoneInfoThread;
    public Handler mMainHandler;
    // for current
    public PhoneGeneralInfo phoneGeneralInfo;
    public CellGeneralInfo serverCellInfo;

    private RecyclerView historyrecyclerView;
    TelephonyManager phoneManager;
    private MyPhoneStateListener myPhoneStateListener;
    private HashMap<String, CommonItem> map = new HashMap<>();

    @Override
    protected int getLayout() {
        return R.layout.fragment_sim;
    }

    @Override
    protected void initView() {
        simImei.setRightText(DeviceUtil.getIMEI(mContext));
        simCompany.setRightText(DeviceUtil.getSimCompany(mContext));
        ciSimCompany.setRightText(DeviceUtil.getSimCompany(mContext));
        SignalStrengthsHandler signalStrengthsHandler = SignalStrengthsHandler.getInstance();
        switch (DeviceUtil.getSimCompany(mContext)) {
            case "中国移动":
                ivSimCompanyLogo.setImageResource(R.drawable.ico_cn_mobile);
                break;
            case "中国联通":
                ivSimCompanyLogo.setImageResource(R.drawable.ico_cn_unicom);
                break;
            case "中国电信":
                ivSimCompanyLogo.setImageResource(R.drawable.ico_cn_telcom);
                break;
            default:
                ivSimCompanyLogo.setImageResource(R.drawable.ico_unknow);
                break;
        }
        simImsi.setRightText(DeviceUtil.getIMSI(mActivity));
        simIccid.setRightText(DeviceUtil.getICCID(mActivity));
        ciSimMcc.setRightText(DeviceUtil.getMCC(mActivity));
        ciSimMnc.setRightText(DeviceUtil.getMNC(mActivity));
        serverCellInfo = new CellGeneralInfo();
        phoneGeneralInfo = new PhoneGeneralInfo();
        myPhoneStateListener = new MyPhoneStateListener();
        phoneManager = (TelephonyManager) mActivity.getSystemService(TELEPHONY_SERVICE);
        phoneManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_SIGNAL_STRENGTHS);
        map.put("Rat", createCommonItem("Rat", ""));
        map.put("Tac", createCommonItem("Tac", ""));
        map.put("Ci", createCommonItem("Ci", ""));
        map.put("Pci", createCommonItem("Pci", ""));
        map.put("Rsrp", createCommonItem("Rsrp", ""));
        map.put("Rsrq", createCommonItem("Rsrq", ""));
        map.put("Sinr", createCommonItem("Sinr", ""));
        map.put("Cqi", createCommonItem("Cqi", ""));
        for (String key : map.keySet()) {
            llServer.addView(map.get(key));
        }
    }

    @Override
    protected void initData() {

    }

    public void updateServerCellView() {
       /* TextView tvCellType = (TextView) mView.findViewById(R.id.tvCellType);
        tvCellType.setText("Rat:" + serverCellInfo.type);*/
        map.get("Rat").setRightText(serverCellInfo.type + "");
        map.get("Tac").setRightText(serverCellInfo.tac + "");
        map.get("Ci").setRightText(serverCellInfo.CId + "");
        map.get("Pci").setRightText(serverCellInfo.pci + "");
        map.get("Rsrp").setRightText(serverCellInfo.rsrp + "");
        map.get("Rsrq").setRightText(serverCellInfo.rsrq + "");
        map.get("Sinr").setRightText(serverCellInfo.sinr + "");
        map.get("Cqi").setRightText(serverCellInfo.cqi + "");
    }

    class PhoneGeneralInfo {
        public String serialNumber;
        public String operaterName;
        public String operaterId;
        public String deviceId;
        public String deviceSoftwareVersion;
        public String Imsi;
        public String Imei;
        public int mnc;
        public int mcc;
        public int ratType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
        public int phoneDatastate;
        public String phoneModel;
        public int sdk;
    }

    class CellGeneralInfo implements Cloneable {
        public int type;
        public int CId;
        public int lac;
        public int tac;
        public int psc;
        public int pci;
        public int RatType = TelephonyManager.NETWORK_TYPE_UNKNOWN;
        public int rsrp;
        public int rsrq;
        public int sinr;
        public int rssi;
        public int cqi;
        public int asulevel;
        public int getInfoType;
        public String time;

        @Override

        public Object clone() {
            SimActivity.CellGeneralInfo cellinfo = null;
            try {
                cellinfo = (SimActivity.CellGeneralInfo) super.clone();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
            return cellinfo;
        }
    }

    class MyPhoneStateListener extends PhoneStateListener {
        @Override
        public void onSignalStrengthsChanged(SignalStrength signalStrength) {
            super.onSignalStrengthsChanged(signalStrength);
            getPhoneGeneralInfo();
            getServerCellInfo();
            if (phoneGeneralInfo.ratType == TelephonyManager.NETWORK_TYPE_LTE) {
                try {
                    serverCellInfo.rssi = (Integer) signalStrength.getClass().getMethod("getLteSignalStrength").invoke(signalStrength);
                    serverCellInfo.rsrp = (Integer) signalStrength.getClass().getMethod("getLteRsrp").invoke(signalStrength);
                    serverCellInfo.rsrq = (Integer) signalStrength.getClass().getMethod("getLteRsrq").invoke(signalStrength);
                    serverCellInfo.sinr = (Integer) signalStrength.getClass().getMethod("getLteRssnr").invoke(signalStrength);
                    serverCellInfo.cqi = (Integer) signalStrength.getClass().getMethod("getLteCqi").invoke(signalStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            } else if (phoneGeneralInfo.ratType == TelephonyManager.NETWORK_TYPE_GSM) {
                try {
                    serverCellInfo.rssi = signalStrength.getGsmSignalStrength();
                    serverCellInfo.rsrp = (Integer) signalStrength.getClass().getMethod("getGsmDbm").invoke(signalStrength);
                    serverCellInfo.asulevel = (Integer) signalStrength.getClass().getMethod("getAsuLevel").invoke(signalStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            } else if (phoneGeneralInfo.ratType == TelephonyManager.NETWORK_TYPE_TD_SCDMA) {
                try {
                    serverCellInfo.rssi = (Integer) signalStrength.getClass().getMethod("getTdScdmaLevel").invoke(signalStrength);
                    serverCellInfo.rsrp = (Integer) signalStrength.getClass().getMethod("getTdScdmaDbm").invoke(signalStrength);
                    serverCellInfo.asulevel = (Integer) signalStrength.getClass().getMethod("getAsuLevel").invoke(signalStrength);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
            Date now = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss");
            serverCellInfo.time = formatter.format(now);
            updateServerCellView();
        }

        @SuppressLint("MissingPermission")
        public void getPhoneGeneralInfo() {
            phoneGeneralInfo.operaterName = phoneManager.getNetworkOperatorName();
            phoneGeneralInfo.operaterId = phoneManager.getNetworkOperator();
            phoneGeneralInfo.mnc = Integer.parseInt(phoneGeneralInfo.operaterId.substring(0, 3));
            phoneGeneralInfo.mcc = Integer.parseInt(phoneGeneralInfo.operaterId.substring(3));
            phoneGeneralInfo.phoneDatastate = phoneManager.getDataState();
            phoneGeneralInfo.deviceId = phoneManager.getDeviceId();
            phoneGeneralInfo.Imei = phoneManager.getSimSerialNumber();
            phoneGeneralInfo.Imsi = phoneManager.getSubscriberId();
            phoneGeneralInfo.serialNumber = phoneManager.getSimSerialNumber();
            phoneGeneralInfo.deviceSoftwareVersion = Build.VERSION.RELEASE;
            phoneGeneralInfo.phoneModel = Build.MODEL;
            phoneGeneralInfo.ratType = phoneManager.getNetworkType();
            phoneGeneralInfo.sdk = Build.VERSION.SDK_INT;
        }

        @SuppressLint("MissingPermission")
        public void getServerCellInfo() {
            try {
                List<CellInfo> allCellinfo;
                allCellinfo = phoneManager.getAllCellInfo();
                if (allCellinfo != null) {
                    CellInfo cellInfo = allCellinfo.get(0);
                    serverCellInfo.getInfoType = 1;
                    if (cellInfo instanceof CellInfoGsm) {
                        CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                        serverCellInfo.CId = cellInfoGsm.getCellIdentity().getCid();
                        serverCellInfo.rsrp = cellInfoGsm.getCellSignalStrength().getDbm();
                        serverCellInfo.asulevel = cellInfoGsm.getCellSignalStrength().getAsuLevel();
                        serverCellInfo.lac = cellInfoGsm.getCellIdentity().getLac();
                        serverCellInfo.RatType = TelephonyManager.NETWORK_TYPE_GSM;
                    } else if (cellInfo instanceof CellInfoWcdma) {
                        CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                        serverCellInfo.CId = cellInfoWcdma.getCellIdentity().getCid();
                        serverCellInfo.psc = cellInfoWcdma.getCellIdentity().getPsc();
                        serverCellInfo.lac = cellInfoWcdma.getCellIdentity().getLac();
                        serverCellInfo.rsrp = cellInfoWcdma.getCellSignalStrength().getDbm();
                        serverCellInfo.asulevel = cellInfoWcdma.getCellSignalStrength().getAsuLevel();
                        serverCellInfo.RatType = TelephonyManager.NETWORK_TYPE_UMTS;
                    } else if (cellInfo instanceof CellInfoLte) {
                        CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                        serverCellInfo.CId = cellInfoLte.getCellIdentity().getCi();
                        serverCellInfo.pci = cellInfoLte.getCellIdentity().getPci();
                        serverCellInfo.tac = cellInfoLte.getCellIdentity().getTac();
                        serverCellInfo.rsrp = cellInfoLte.getCellSignalStrength().getDbm();
                        serverCellInfo.asulevel = cellInfoLte.getCellSignalStrength().getAsuLevel();
                        serverCellInfo.RatType = TelephonyManager.NETWORK_TYPE_LTE;
                    }
                } else
                //for older devices
                {
                    getServerCellInfoOnOlderDevices();
                }
            } catch (Exception e) {
                getServerCellInfoOnOlderDevices();
            }

        }

        void getServerCellInfoOnOlderDevices() {
            @SuppressLint("MissingPermission") GsmCellLocation location = (GsmCellLocation) phoneManager.getCellLocation();
            serverCellInfo.getInfoType = 0;
            serverCellInfo.CId = location.getCid();
            serverCellInfo.tac = location.getLac();
            serverCellInfo.psc = location.getPsc();
            serverCellInfo.type = phoneGeneralInfo.ratType;
        }
    }

    private CommonItem createCommonItem(String label, String value) {
        CommonItem commonItem = new CommonItem(mActivity);
        commonItem.setLeftText(label);
        if (TextUtils.isEmpty(value)) {
            value = "获取中";
        }
        commonItem.setRightText(value);
        return commonItem;
    }
}
