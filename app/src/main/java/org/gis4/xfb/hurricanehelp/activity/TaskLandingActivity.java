package org.gis4.xfb.hurricanehelp.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
import com.avos.avoscloud.AVAnalytics;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.widget.SlidingUpBaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 显示收到的任务的信息
 * @author zc
 */
public class TaskLandingActivity extends SlidingUpBaseActivity<ObservableScrollView> implements ObservableScrollViewCallbacks, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private ObservableScrollView scrollView;
    private SliderLayout mDemoSlider;
    private View mFab;

    public MapView mMapView;
    private AMap aMap;

    private List<XfbTask> xfbTaskList;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_landing;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);

        return scrollView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        XfbTask xfbTask = bundle.getParcelable("xfbTask");
        xfbTaskList = new ArrayList<>();
        xfbTaskList.add(xfbTask);

        super.mTitle.setText(xfbTask.getTaskType() + "，奖励：" + xfbTask.getRewardPoint() + "积分");

        TextView textviewTimeStart = (TextView) findViewById(R.id.textview_time_start);
        TextView textviewTimeEnd= (TextView) findViewById(R.id.textview_time_end);
        TextView textviewTaskDesc= (TextView) findViewById(R.id.textview_task_desc);

        textviewTimeStart.setText(getDateString(xfbTask.getStartTime()));
        textviewTimeEnd.setText(getDateString(xfbTask.getEndTime()));
        textviewTaskDesc.setText(xfbTask.getDesc());

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);

        mDemoSlider.setPresetTransformer("ZoomIn");

        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());

        mMapView = (MapView) findViewById(R.id.showTwoPointsMap);
        InitMap(savedInstanceState);

        mFab = findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //// TODO: 2016-09-02 接单
                Toast.makeText(TaskLandingActivity.this, "接单成功", Toast.LENGTH_SHORT).show();
            }
        });
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
        myLocationStyle.strokeWidth(0);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(super.locationSource);
        aMap.setMyLocationEnabled(true);

        ui.setMyLocationButtonEnabled(true);
        ui.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        ui.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        ui.setZoomControlsEnabled(true);
        ui.setScaleControlsEnabled(true);
        ui.setAllGesturesEnabled(true);

        //添加地图上的标记点,执行点
        LatLng happenLatLng = new LatLng(xfbTaskList.get(0).getHappenLat(),xfbTaskList.get(0).getHappenLng());
        MarkerOptions happenMarkerOptions = new MarkerOptions();
        happenMarkerOptions.position(happenLatLng);
        Bitmap bitmap = BitmapFactory. decodeResource (getResources(), XfbTask.getLogoOfTaskType("执行"));
        Bitmap smallBitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth()/2, bitmap.getHeight()/2, true);
        happenMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallBitmap));
        aMap.addMarker(happenMarkerOptions);
        //送达点
        LatLng sendLatLng =new LatLng(xfbTaskList.get(0).getSenderLat(),xfbTaskList.get(0).getSenderLng());
        MarkerOptions sendMarkerOptions = new MarkerOptions();
        sendMarkerOptions.position(sendLatLng);
        Bitmap bitmap2 = BitmapFactory. decodeResource (getResources(), XfbTask.getLogoOfTaskType("送达"));
        Bitmap smallBitmap2 = bitmap2.createScaledBitmap(bitmap2, bitmap2.getWidth()*3/4, bitmap2.getHeight()*3/4, true);
        sendMarkerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallBitmap2));

        LatLngBounds latLngBounds = new LatLngBounds(happenLatLng, sendLatLng);

        aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 200));

        aMap.addMarker(sendMarkerOptions);
    }

    @Override
    public void onPause() {
        super.onPause();
        AVAnalytics.onPause(this);
        mMapView.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        AVAnalytics.onResume(this);
        mMapView.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected void onStop() {
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    private String getDateString(Date date) {
        return date.getYear() + "年" + date.getMonth() + "月" + date.getDay() + "日" + date.getHours() + "点" + date.getMinutes() + "分";
    }
}
