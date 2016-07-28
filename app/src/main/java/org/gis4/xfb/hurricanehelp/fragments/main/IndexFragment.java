package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;
import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class IndexFragment extends BaseFragment
{
    public static IndexFragment instance()
    {
        IndexFragment view = new IndexFragment();
        return view;
    }

    private AMap aMap;

    @BindView(R.id.indexMap)
    public MapView mMapView;

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
        aMap.setTrafficEnabled(true);

        //BUG: 性能问题？
        ui.setMyLocationButtonEnabled(true);
        ui.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        ui.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        ui.setScaleControlsEnabled(true);
        ui.setZoomGesturesEnabled(false);
    }

}
