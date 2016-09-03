package org.gis4.xfb.hurricanehelp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.XfbTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailsMapActivity extends BaseActivity {

    @BindView(R.id.taskLocationMap)
    public MapView mMapView;

    private AMap aMap;

    //保存数据
    private XfbTask xfbTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details_map);
        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();
        xfbTask = bundle.getParcelable("xfbTask");

        InitMap(savedInstanceState);
    }

    private void InitMap(Bundle s) {
        mMapView.onCreate(s);
        if (aMap == null) {
            aMap = mMapView.getMap();
        }

        UiSettings ui = aMap.getUiSettings();
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        myLocationStyle.strokeColor(Color.WHITE);
        myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 180));
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setMyLocationEnabled(true);

        ui.setMyLocationButtonEnabled(false);
        ui.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        ui.setZoomControlsEnabled(true);
        ui.setScaleControlsEnabled(true);
        ui.setAllGesturesEnabled(true);

        //添加地图上的标记点,执行点
        LatLng happenLatLng = new LatLng(xfbTask.getHappenLat(),xfbTask.getHappenLng());
        MarkerOptions happenMarkerOptions = new MarkerOptions();
        happenMarkerOptions.position(happenLatLng);
        Bitmap bitmap = BitmapFactory. decodeResource (getResources(), XfbTask.getLogoOfTaskType("执行"));
        Bitmap smallBitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
        happenMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallBitmap));
        if(!xfbTask.getHappenLocationDescription().isEmpty()) {
            happenMarkerOptions.title(xfbTask.getHappenLocationDescription());
        }
        aMap.addMarker(happenMarkerOptions);
        //送达点
        LatLng sendLatLng =new LatLng(xfbTask.getSenderLat(),xfbTask.getSenderLng());
        MarkerOptions sendMarkerOptions = new MarkerOptions();
        sendMarkerOptions.position(sendLatLng);
        Bitmap bitmap2 = BitmapFactory. decodeResource (getResources(), XfbTask.getLogoOfTaskType("送达"));
        Bitmap smallBitmap2 = bitmap2.createScaledBitmap(bitmap2, bitmap2.getWidth()*3/4, bitmap2.getHeight()*3/4, true);
        sendMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallBitmap2));

        LatLngBounds latLngBounds = new LatLngBounds(happenLatLng, sendLatLng);

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));
        if(!xfbTask.getSenderLocationManualDesc().isEmpty()) {
            happenMarkerOptions.title(xfbTask.getSenderLocationManualDesc());
        }
        aMap.addMarker(sendMarkerOptions);
    }
}
