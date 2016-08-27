package org.gis4.xfb.hurricanehelp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;


import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.location.LocationManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChooseLocationActivity extends BaseActivity implements AMap.OnCameraChangeListener {

    public static final int REQUEST_SEND_LOCATION = 201;
    public static final int REQUEST_EXECUTE_LOCATION = 202;

    private boolean locChgFirst = true;

    private Toolbar toolbar;
    private ImageView imageViewPinPin;
    private ImageView imageViewPinPinShadow;
    private RelativeLayout locationTextDescription;
    private TextView locationTextview;

    private AMap aMap;
    private GeocodeSearch geocoderSearch;
    private LocationManager locationManager;

    @BindView(R.id.selectLocationMap)
    public MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_location);

        imageViewPinPin = (ImageView) findViewById(R.id.pinpin_imageview);
        imageViewPinPinShadow = (ImageView) findViewById(R.id.pinpin_shadow_imageview);

        super.locationOld = new LocationManager(this.getApplicationContext());
        locationTextDescription = (RelativeLayout) findViewById(R.id.location_text_description);
        locationTextview = (TextView) findViewById(R.id.location_textview);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_location_select_menu);

        Bundle bundle = this.getIntent().getExtras();
        String title = bundle.getString("title");
        toolbar.setTitle(title);

        initialMenu(toolbar.getMenu());

        ButterKnife.bind(this);
        InitMap(savedInstanceState);

        locationManager = super.locationOld;
    }

    private void initialMenu(Menu menu) {
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_location:
                        AMapLocation location = locationManager.getLocation();
                        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        break;
                    case R.id.action_ok:
                        //获取页面的截图，并传到上个activity显示
                        RelativeLayout mapRelativeLayout = (RelativeLayout) findViewById(R.id.map_relativeLayout);
                        mapRelativeLayout.setDrawingCacheEnabled(true);
                        Bitmap bitmap = mapRelativeLayout.getDrawingCache();
                        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/4, bitmap.getWidth(), bitmap.getHeight()/2);
                        LatLng mTarget = aMap.getCameraPosition().target;

                        Intent intent = new Intent();
                        intent.putExtra("mapPic", Bitmap2Bytes(bitmap1));
                        mapRelativeLayout.setDrawingCacheEnabled(false);

                        intent.putExtra("locationText", locationTextview.getText());
                        intent.putExtra("locationLatitude", mTarget.latitude);
                        intent.putExtra("locationLongitude", mTarget.longitude);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                }
                return false;
            }
        });
    }

    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private void InitMap(Bundle s) {
        mMapView.onCreate(s);
        if (aMap == null) {
            aMap = mMapView.getMap();
            aMap.setOnCameraChangeListener(this);
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
        ui.setAllGesturesEnabled(true);

        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (!locChgFirst) return;
                changeCamera(CameraUpdateFactory.zoomTo(17), null);
                locChgFirst = false;
            }
        });

        formZoomLevel = aMap.getCameraPosition().zoom;

        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            //逆地理编码
            @Override
            public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
                String addressName;
                if (rCode == 1000) {
                    if (result != null && result.getRegeocodeAddress() != null
                            && result.getRegeocodeAddress().getFormatAddress() != null) {
                        addressName = result.getRegeocodeAddress().getFormatAddress()
                                + "附近";
                    } else {
                        addressName = "位置太偏，找不到";
                    }
                } else {
                    addressName = "查询失败";
                }
                locationTextview.setText(addressName);

                locationTextDescription.setVisibility(View.VISIBLE);
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });
    }

    private void changeCamera(CameraUpdate update, AMap.CancelableCallback callback) {
        if (callback == null) {
            aMap.animateCamera(update);
            return;
        }
        aMap.animateCamera(update, 1000, callback);
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

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        if (locationTextDescription.getVisibility() == View.VISIBLE) {
            locationTextDescription.setVisibility(View.GONE);
        }
    }

    private float formZoomLevel;

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {
        //pinpin的移动效果动画
        if (formZoomLevel == aMap.getCameraPosition().zoom) {
            Animation animationTranslate = AnimationUtils.loadAnimation(this, R.anim.pinpin_translate);

            animationTranslate.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                    Animation animationScale = AnimationUtils.loadAnimation(ChooseLocationActivity.this, R.anim.pinpin_shadow_scale);
                    imageViewPinPinShadow.startAnimation(animationScale);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    LatLng mTarget = aMap.getCameraPosition().target;
                    LatLonPoint latLonPoint = new LatLonPoint(mTarget.latitude, mTarget.longitude);
                    RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
                    geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });

            imageViewPinPin.startAnimation(animationTranslate);
        }

        if (locationTextDescription.getVisibility() == View.VISIBLE) {
            locationTextDescription.setVisibility(View.VISIBLE);
        }

        formZoomLevel = aMap.getCameraPosition().zoom;
    }
}
