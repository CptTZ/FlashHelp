package org.gis4.xfb.hurricanehelp.test;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MyLocationStyle;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
    private AMap aMap;
    private AmapLocationSource locationSource;

    @BindView(R.id.drawer_layout)
    public DrawerLayout drawer;
    @BindView(R.id.nav_view)
    public NavigationView navigationView;
    @BindView(R.id.map)
    public MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);

        navigationView.setNavigationItemSelectedListener(this);
        setUpMap();
    }

    private void setUpMap()
    {
        if (aMap == null)
        {
            aMap = mMapView.getMap();
        }

        UiSettings ui=aMap.getUiSettings();
        this.locationSource = new AmapLocationSource(this);
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker));
        myLocationStyle.strokeColor(Color.WHITE);
        myLocationStyle.radiusFillColor(Color.argb(50, 0, 0, 180));
        myLocationStyle.strokeWidth(1.0f);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.setLocationSource(this.locationSource);
        aMap.setMyLocationEnabled(true);

        ui.setMyLocationButtonEnabled(true);
        ui.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);
        ui.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);
        ui.setScaleControlsEnabled(true);

     }

    @Override
    public void onBackPressed()
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        } else
        {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera)
        {

        } else if (id == R.id.nav_gallery)
        {

        } else if (id == R.id.nav_slideshow)
        {

        } else if (id == R.id.nav_manage)
        {

        } else if (id == R.id.nav_share)
        {

        } else if (id == R.id.nav_send)
        {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        mMapView.onResume();
        locationSource.startLocation();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        mMapView.onPause();
        locationSource.stopLocation();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        mMapView.onDestroy();
        locationSource.deactivate();
    }

}
