package org.gis4.xfb.hurricanehelp.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.*;

/**
 * 定位信息类 目前使用高德定位
 * @deprecated 请使用AmapLocationSource更新地图位置
 * @author TonyZ
 */
public class LocationManager
{
    private AMapLocationClient mLocationClient;
    private AMapLocation myLocation;

    // 单例模式
    private static LocationManager instance;

    public static synchronized LocationManager getInstance(Context c)
    {
        if (instance == null)
        {
            instance = new LocationManager(c);
        }
        return instance;
    }

    /**
     * 初始化定位系统
     *
     * @param c getApplicationContext()
     */
    private LocationManager(Context c)
    {
        mLocationClient = new AMapLocationClient(c);
        mLocationClient.setLocationOption(GetOptions());
        mLocationClient.setLocationListener(new AMapLocationListener()
        {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation)
            {
                if (aMapLocation != null)
                {
                    if (aMapLocation.getErrorCode() != 0)
                        Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", errInfo:" + aMapLocation.getErrorInfo());
                    else
                    {
                        // 获取位置没有问题的逻辑
                        myLocation = aMapLocation;
                        return;
                    }
                }
                myLocation = mLocationClient.getLastKnownLocation();
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
