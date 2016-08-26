package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MyLocationStyle;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.APSTSViewPager;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;
import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IndexFragment extends BaseFragment
{
    private static final int SCROLL_BY_PX = 100;
    private boolean locChgFirst = true;
    private Toolbar toolBar;

    public static IndexFragment instance()
    {
        return new IndexFragment();
    }

    public AMap aMap;

    //TODO: 更优雅的解决地图移动问题
    //// TODO: 2016-8-19 已经被我优雅的解决了。
    @BindView(R.id.indexMap)
    public MapView mMapView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_index, container, false);
        ButterKnife.bind(this, view);

        InitMap(savedInstanceState);
        return view;
    }

    private void InitMap(Bundle s) {
        mMapView.onCreate(s);
        if (aMap == null)
        {
            aMap = mMapView.getMap();

            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationIcon(BitmapDescriptorFactory
                    .fromResource(R.drawable.location_marker));// 设置小蓝点的图标
            myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
            myLocationStyle.radiusFillColor(Color.argb(100, 0, 0, 180));// 设置圆形的填充颜色
            // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
            myLocationStyle.strokeWidth(0);// 设置圆形的边框粗细
            aMap.setMyLocationStyle(myLocationStyle);
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

        ui.setMyLocationButtonEnabled(false);
        ui.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        ui.setZoomControlsEnabled(true);
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

    private boolean isZoom = false;
    private LatLngBounds latLngBounds;

    @Override
    public void onStart(){
        super.onStart();

        final APSTSViewPager mVP =(APSTSViewPager) getActivity().findViewById(R.id.vp_main);

        aMap.setOnMapTouchListener(new AMap.OnMapTouchListener() {
            @Override
            public void onTouch(MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_UP:
                        mVP.requestDisallowInterceptTouchEvent(false);
                        isZoom = false;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if(isZoom) {
                            aMap.moveCamera(
                                    CameraUpdateFactory.newLatLngBounds(
                                            latLngBounds,
                                            (int) Math.abs(motionEvent.getX(0) - motionEvent.getX(1)),
                                            (int) Math.abs(motionEvent.getY(0) - motionEvent.getY(1)), 0));
                        }
                        else {
                            float changeX = motionEvent.getX() - motionEvent.getHistoricalX(0);
                            float changeY = motionEvent.getY() - motionEvent.getHistoricalY(0);
                            changeCamera(CameraUpdateFactory.scrollBy(-changeX, -changeY), null);
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:
                        mVP.requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_POINTER_2_DOWN:
                        mVP.requestDisallowInterceptTouchEvent(true);
                        isZoom = true;
                        latLngBounds = new LatLngBounds(
                                aMap.getProjection().fromScreenLocation(new Point(
                                        (int) Math.min(motionEvent.getX(0), motionEvent.getX(1)),
                                        (int) Math.max(motionEvent.getY(0), motionEvent.getY(1)))),
                                aMap.getProjection().fromScreenLocation(new Point(
                                        (int) Math.max(motionEvent.getX(0), motionEvent.getX(1)),
                                        (int) Math.min(motionEvent.getY(0), motionEvent.getY(1)))));
                        break;
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        if (callback == null)
        {
            aMap.animateCamera(update);
            return;
        }
        aMap.animateCamera(update, 1000, callback);
    }

}
