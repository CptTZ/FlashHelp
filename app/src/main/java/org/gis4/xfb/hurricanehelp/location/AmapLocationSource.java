package org.gis4.xfb.hurricanehelp.location;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;

/**
 * 位置更新类，参考高德
 * Created by Tony on 2016/7/26.
 */
public class AmapLocationSource implements LocationSource, AMapLocationListener
{
    /**
     * 更新位置
     */
    private AMapLocation location;

    private Context oriCon;
    private LocationSource.OnLocationChangedListener mListener;
    private AMapLocationClient mlocationClient;

    public CameraPosition getLocCam(float level)
    {
        if (this.location == null)
        {
            return null;
        } else
        {
            return new CameraPosition(
                    new LatLng(this.location.getLatitude(), this.location.getLongitude()),
                    level, 0f, 0f);
        }
    }

    /**
     * 初始化位置更新
     *
     * @param c 界面上下文
     */
    public AmapLocationSource(Context c)
    {
        this.oriCon = c;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation)
    {
        if (mListener != null && aMapLocation != null)
        {
            if (aMapLocation.getErrorCode() == 0)
            {
                mListener.onLocationChanged(aMapLocation);
                this.location = aMapLocation;
            } else
            {
                String errText = "定位失败," + aMapLocation.getErrorCode() + ": " + aMapLocation.getErrorInfo();
                Log.e("AmapErr", errText);
            }
        }
    }

    @Override
    public void activate(OnLocationChangedListener listener)
    {
        mListener = listener;
        if (mlocationClient == null)
        {
            mlocationClient = new AMapLocationClient(this.oriCon);
            AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
            mlocationClient.setLocationListener(this);

            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            mLocationOption.setInterval(5000);

            mlocationClient.setLocationOption(mLocationOption);
            mlocationClient.startLocation();
        }
    }

    public void stopLocation()
    {
        if (mlocationClient != null)
            this.mlocationClient.stopLocation();
    }

    public void startLocation()
    {
        if (mlocationClient != null)
            this.mlocationClient.startLocation();
    }

    @Override
    public void deactivate()
    {
        mListener = null;
        if (mlocationClient != null)
        {
            mlocationClient.stopLocation();
            mlocationClient.onDestroy();
        }
        mlocationClient = null;
    }
}
