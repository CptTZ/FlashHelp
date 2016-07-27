package org.gis4.xfb.hurricanehelp.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.*;

/**
 * 定位信息类 目前使用高德定位
 *
 * @author TonyZ
 * @deprecated 请使用AmapLocationSource更新地图位置
 */
public class LocationManager
{
    private AMapLocationClient mLocationClient;
    private AMapLocation myLocation;

    /**
     * 初始化定位系统
     *
     * @param c getApplicationContext()
     */
    public LocationManager(Context c)
    {
        mLocationClient = new AMapLocationClient(c);
        mLocationClient.setLocationOption(GetOptions());
        myLocation = mLocationClient.getLastKnownLocation();
        mLocationClient.setLocationListener(new AMapLocationListener()
        {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation)
            {
                if (aMapLocation != null)
                {
                    if (aMapLocation.getErrorCode() != 0)
                        Log.e("AmapError", "eLocation, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    else
                        myLocation = aMapLocation;
                }
            }
        });
        StartUpdateLocation();
    }

    /**
     * 获取位置信息
     *
     * @return 位置信息
     */
    public AMapLocation getLocation()
    {
        return myLocation;
    }

    private AMapLocationClientOption GetOptions()
    {
        AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        mLocationOption.setNeedAddress(true);
        mLocationOption.setOnceLocation(false);
        mLocationOption.setWifiActiveScan(true);
        mLocationOption.setMockEnable(false);
        mLocationOption.setInterval(5000);
        mLocationOption.setGpsFirst(true);
        return mLocationOption;
    }

    /**
     * 销毁位置相关资源
     */
    public void DestoryLocation()
    {
        if (mLocationClient.isStarted() == false) return;
        StopUpateLocation();
        mLocationClient.onDestroy();
    }

    /**
     * 开始更新位置
     */
    public void StartUpdateLocation()
    {
        if (!mLocationClient.isStarted())
            mLocationClient.startLocation();
    }

    /**
     * 停止更新位置
     */
    public void StopUpateLocation()
    {
        if (mLocationClient.isStarted())
            mLocationClient.stopLocation();
    }

}
