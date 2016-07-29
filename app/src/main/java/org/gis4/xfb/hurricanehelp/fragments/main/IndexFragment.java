package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;
import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndexFragment extends BaseFragment
{
    private static final int SCROLL_BY_PX = 100;
    private boolean locChgFirst = true;

    public static IndexFragment instance()
    {
        return new IndexFragment();
    }

    private AMap aMap;

    //TODO: 更优雅的解决地图移动问题
    @BindView(R.id.indexMap)
    public MapView mMapView;

    @OnClick(R.id.buttonL)
    public void Left()
    {
        changeCamera(CameraUpdateFactory.scrollBy(-SCROLL_BY_PX, 0), null);
    }

    @OnClick(R.id.buttonR)
    public void Right()
    {
        changeCamera(CameraUpdateFactory.scrollBy(SCROLL_BY_PX, 0), null);
    }

    @OnClick(R.id.buttonU)
    public void Up()
    {
        changeCamera(CameraUpdateFactory.scrollBy(0, -SCROLL_BY_PX), null);
    }

    @OnClick(R.id.buttonD)
    public void Down()
    {
        changeCamera(CameraUpdateFactory.scrollBy(0, SCROLL_BY_PX), null);
    }

    @OnClick(R.id.buttonIn)
    public void ZIn()
    {
        changeCamera(CameraUpdateFactory.zoomIn(), null);
    }

    @OnClick(R.id.buttonOut)
    public void ZOut()
    {
        changeCamera(CameraUpdateFactory.zoomOut(), null);
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.bind(this, view);

        InitMap(savedInstanceState);
        return view;
    }

    private void InitMap(Bundle s)
    {
        mMapView.onCreate(s);
        if (aMap == null)
        {
            aMap = mMapView.getMap();
        }

        UiSettings ui = aMap.getUiSettings();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        myLocationStyle.strokeColor(Color.WHITE);
        myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 180));
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(super.locationSource);
        aMap.setMyLocationEnabled(true);

        ui.setMyLocationButtonEnabled(true);
        ui.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        ui.setZoomControlsEnabled(false);
        ui.setScaleControlsEnabled(true);
        ui.setAllGesturesEnabled(false);

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener()
        {
            @Override
            public void onMyLocationChange(Location location)
            {
                if (!locChgFirst) return;
                changeCamera(CameraUpdateFactory.zoomTo(17), null);
                locChgFirst = false;
            }
        });
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback)
    {
        if (callback == null)
        {
            aMap.animateCamera(update);
            return;
        }
        aMap.animateCamera(update, 1000, callback);
    }

}
