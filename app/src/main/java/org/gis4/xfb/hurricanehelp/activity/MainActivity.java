package org.gis4.xfb.hurricanehelp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.feedback.FeedbackAgent;
import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.Margins;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.Dbconnect;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.data.initiateSearch;
import org.gis4.xfb.hurricanehelp.data.testXfbTask;
import org.gis4.xfb.hurricanehelp.fragments.main.*;
import org.gis4.xfb.hurricanehelp.lbs.location.LocationManager;
import org.gis4.xfb.hurricanehelp.widget.APSTSViewPager;

import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, GeocodeSearch.OnGeocodeSearchListener
{
    private static final int VIEW_INDEX = 0;
    private static final int VIEW_TASK = 1;
    private static final int VIEW_MY = 2;
    private static final int VIEW_ACTIVITIES = 3;
    private static final int VIEW_CAPACITY = 4;
    private int mSize = 0;

    public IndexFragment mFirstFragment = null;
    public TaskFragment mSecondFragment = null;
    public MeFragment mThirdFragment = null;
    public ActivitiesFragment mFourthFragment = null;

    //因为点击搜索会莫名其妙搜索两次，所以设置一个计数，强制搜索一次
    private int searchCount = 0;
    private GeocodeSearch geocoderSearch;

    //刷新页面时获得的数据存在着个里面。
    private List<XfbTask> taskData;

    //从indexFragment里面获取到的地图,为了实现toolBar上的定位，刷新，搜索功能。
    //因为涉及到初始化问题，只能在每次使用时赋值。
    private AMap aMap;

    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip mAPSTS;
    @BindView(R.id.vp_main)
    APSTSViewPager mVP;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.view_search)
    RelativeLayout view_search;
    @BindView(R.id.line_divider)
    View line_divider;
    @BindView(R.id.card_search)
    CardView card_search;
    @BindView(R.id.edit_text_search)
    EditText edit_text_search;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.listContainer)
    ListView listContainer;
    @BindView(R.id.image_search_back)
    ImageView image_search_back;

    @OnClick(R.id.ivCenterBtn)
    public void onClickPostTask(View v) {
        String text;
        if(!super.hasLogined())
        {
            Toast.makeText(MainActivity.this, getString(R.string.user_not_reg_yet), Toast.LENGTH_SHORT).show();
            return;
        }
        if (locationManager.getLocation() == null) {
            text = "无有效位置，无法发布！";
            Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
        }
        else {
            startActivity(new Intent(this,PublishActivity.class));
    }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.locationOld = new LocationManager(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        onPageSelected(0);

        super.feedBackAgent = new FeedbackAgent(this);
        super.feedBackAgent.sync();
        locationManager = super.locationOld;
        InitTab();
        handleFragmentToolBarClick();
    }

    private void InitTab() {
        mSize = getResources().getDimensionPixelSize(R.dimen.xfb_tab_size);
        mVP.setOffscreenPageLimit(VIEW_CAPACITY);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        mVP.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mAPSTS.setViewPager(mVP);
        mAPSTS.setOnPageChangeListener(this);
        mVP.setCurrentItem(VIEW_INDEX);
        mAPSTS.showDot(VIEW_TASK, "❤");
        mAPSTS.showDot(VIEW_ACTIVITIES, "❤");
    }

    private long mExitTime;
    private LocationManager locationManager;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果搜索框打开了得话，按返回键先隐藏搜索框，否则按正常流程走
            if (card_search.getVisibility() == View.VISIBLE){
                image_search_back.callOnClick();
            }
            else{
                // 1.5s内再次按下即退出
                if ((System.currentTimeMillis() - mExitTime) > 1500) {
                    Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
                    mExitTime = System.currentTimeMillis();
                }
                else {
                    super.locationOld.DestoryLocation();
                    System.runFinalization();
                    System.exit(0);
                }
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        super.locationOld.DestoryLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();
        super.locationOld.StopUpateLocation();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    //滑动时每个ViewPager都有一个对应的position，根据poision设置不同页面的toolbar的内容
    @Override
    public void onPageSelected(int position) {
        switch(position) {
            case 0:
                InitiateToolbarTabs("首页", R.mipmap.tabbar_home_logo, R.menu.fragment_index);
                break;
            case 1:
                InitiateToolbarTabs("我的任务", R.mipmap.tabbar_message_center_logo, R.menu.fragment_task);
                mAPSTS.hideDot(1);
                break;
            case 2:
                InitiateToolbarTabs("我的信息", R.mipmap.tabbar_profile_logo, R.menu.fragment_me);
                findViewById(R.id.action_logout).setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if(AVUser.getCurrentUser()==null) return;
                        AVUser.logOut();
                        UpdateUser();
                        mFirstFragment.UpdateUser();
                        mSecondFragment.UpdateUser();
                        mThirdFragment.UpdateUser();
                        mFourthFragment.UpdateUser();
                        showLogInWindow();
                    }
                });
                break;
            case 3:
                InitiateToolbarTabs("发现新任务", R.mipmap.tabbar_discover_logo, R.menu.fragment_activities);
                mAPSTS.hideDot(3);
                break;
        }
    }

    //显示三个fragment里面的登陆窗口
    private void showLogInWindow() {
        mSecondFragment.getView().findViewById(R.id.linearLayout_reg).setVisibility(View.VISIBLE);
        mThirdFragment.getView().findViewById(R.id.linearLayout_reg).setVisibility(View.VISIBLE);
        mFourthFragment.getView().findViewById(R.id.linearLayout_reg).setVisibility(View.VISIBLE);
    }
    //设置toolbar的内容。
    private void InitiateToolbarTabs(String title, int iconId, int menuId) {

        toolbar.setTitle(title);
        toolbar.setNavigationIcon(iconId);

        toolbar.getMenu().clear();
        toolbar.inflateMenu(menuId);
    }

    //处理toolbar对indexFragment的操作
    private void handleFragmentToolBarClick(){

        image_search_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateSearch.hideSearchBox(MainActivity.this, card_search, toolbar, view_search, listView, edit_text_search, line_divider);
                listContainer.setVisibility(View.GONE);
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                int menuItem = item.getItemId();
                switch (menuItem){
                    case R.id.action_location:
                        MapView mMapView = (MapView) findViewById(R.id.indexMap);
                        aMap = mMapView.getMap();
                        AMapLocation location = locationManager.getLocation();
                        if(location == null)
                        {
                            showError("定位失败，过会再试吧");
                            return false;
                        }
                        aMap.animateCamera(CameraUpdateFactory.changeLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        Toast.makeText(getApplicationContext(), "您的位置是: " + location.getAddress(), Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_search:
                        initiateSearch.showSearchBox(MainActivity.this, card_search, toolbar, view_search, listView, edit_text_search, line_divider);
                        break;
                    case R.id.image_search_back:
                        initiateSearch.hideSearchBox(MainActivity.this, card_search, toolbar, view_search, listView, edit_text_search, line_divider);
                        listContainer.setVisibility(View.GONE);
                        break;
                    case R.id.action_voice:
                        Toast.makeText(MainActivity.this, "语音功能暂未完善", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_refresh:
                        MapView mMapView1;
                        if(aMap ==null) {
                            mMapView1 = (MapView) findViewById(R.id.indexMap);
                            aMap = mMapView1.getMap();

                            aMap.setOnInfoWindowClickListener(new AMap.OnInfoWindowClickListener() {
                                @Override
                                public void onInfoWindowClick(Marker marker) {
                                    Intent intent =new Intent(MainActivity.this, TaskLandingActivity.class);
                                    Bundle bundle=new Bundle();
                                    bundle.putParcelable("xfbTask", (XfbTask) marker.getObject());
                                    intent.putExtras(bundle);
                                    MainActivity.this.startActivity(intent);
                                }
                            });
                        }
                        aMap.clear();
                        new ShowMarkerOnMapTask().execute();
                        break;
                    case R.id.action_top:
                        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
                        recyclerView.smoothScrollToPosition(0);
                        break;
                    case R.id.action_feedback:
                        baseActivity.feedBackAgent.startDefaultThreadActivity();
                        break;

                    case R.id.action_edit:
                        Intent intent = new Intent(MainActivity.this, UserInfoEditActivity.class);
                        startActivity(intent);
                    default:
                        return false;
                }
                return true;
            }
        });

        edit_text_search.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event){
                switch (keyCode){
                    case KeyEvent.KEYCODE_ENTER:
                        // 实现搜索框回车事件，这边在按下回车后会被调用两次，不知道出了什么幺蛾子。加个变量能解决这个问题，但是这样不够优雅
                        if(searchCount % 2 == 0) {
                            MapView mMapView = (MapView) findViewById(R.id.indexMap);
                            aMap = mMapView.getMap();

                            geocoderSearch = new GeocodeSearch(MainActivity.this);
                            geocoderSearch.setOnGeocodeSearchListener(MainActivity.this);
                            GeocodeQuery query = new GeocodeQuery(edit_text_search.getText().toString(), "025");
                            geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
                        }
                        searchCount++;
                }
                return false;
            }
        });
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == 1000) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(address.getLatLonPoint().getLatitude(), address.getLatLonPoint().getLongitude()), 15));

                String addressName = "已移动地图至:" + address.getFormatAddress();
                image_search_back.callOnClick();
                Toast.makeText(MainActivity.this, addressName, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "没有搜索结果，你输入的地点太偏啦", Toast.LENGTH_SHORT).show();
            }

        }
        else {
            Toast.makeText(MainActivity.this, "出错啦，你的网是不是断掉了？", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class ShowMarkerOnMapTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            taskData = Dbconnect.FetchNotAccpetedXfbTask();
            return String.valueOf(taskData.size());
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.isEmpty()|result.equals("0"))
            {
                Toast.makeText(baseActivity, "刷新任务失败", Toast.LENGTH_SHORT).show();
                super.onPostExecute(result);
                return;
            }
            Toast.makeText(MainActivity.this, "总共有" + result + "条未完成任务", Toast.LENGTH_SHORT).show();

            for (XfbTask task : taskData)
            {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(new LatLng(task.getSenderLat(), task.getSenderLng()));
                markerOptions.title(task.getDesc());

                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), XfbTask.getLogoOfTaskType(task.getTaskType()));
                Bitmap smallBitmap = bitmap.createScaledBitmap(bitmap, bitmap.getWidth(), bitmap.getHeight(), true);
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallBitmap));

                Marker marker = aMap.addMarker(markerOptions);
                marker.setObject(task);
            }

            super.onPostExecute(result);
        }
    }

    /**
     * 首页滑动Fragment管理器
     */
    public class FragmentAdapter extends FragmentStatePagerAdapter implements
            AdvancedPagerSlidingTabStrip.IconTabProvider,
            AdvancedPagerSlidingTabStrip.LayoutProvider,
            AdvancedPagerSlidingTabStrip.TipsProvider {

        public FragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position >= 0 && position < VIEW_CAPACITY) {
                switch (position) {
                    case VIEW_INDEX:
                        if (null == mFirstFragment)
                            mFirstFragment = IndexFragment.instance(baseActivity);
                        return mFirstFragment;

                    case VIEW_TASK:
                        if (null == mSecondFragment)
                            mSecondFragment = TaskFragment.instance(baseActivity);
                        return mSecondFragment;

                    case VIEW_MY:
                        if (null == mThirdFragment)
                            mThirdFragment = MeFragment.instance(baseActivity);
                        return mThirdFragment;

                    case VIEW_ACTIVITIES:
                        if (null == mFourthFragment)
                            mFourthFragment = ActivitiesFragment.instance(baseActivity);
                        return mFourthFragment;
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount() {
            return VIEW_CAPACITY;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (position >= 0 && position < VIEW_CAPACITY) {
                switch (position) {
                    case VIEW_INDEX:
                        return "首页";
                    case VIEW_TASK:
                        return "任务";
                    case VIEW_MY:
                        return "我的";
                    case VIEW_ACTIVITIES:
                        return "发现";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public float getPageWeight(int position) {
            if (position >= 0 && position < VIEW_CAPACITY) {
                switch (position) {
                    case VIEW_INDEX:
                        return 0.92f;
                    case VIEW_TASK:
                        return 1.0f;
                    case VIEW_MY:
                        return 1.0f;
                    case VIEW_ACTIVITIES:
                        return 0.92f;
                    default:
                        break;
                }
            }
            return 1.0f;
        }

        @Override
        public int[] getPageRule(int position) {
            if (position >= 0 && position < VIEW_CAPACITY) {
                switch (position) {
                    case VIEW_INDEX:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_LEFT};
                    case VIEW_TASK:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_LEFT};
                    case VIEW_MY:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_RIGHT};
                    case VIEW_ACTIVITIES:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_RIGHT};
                    default:
                        break;
                }
            }
            return new int[0];
        }

        @Override
        public Margins getPageMargins(int position) {
            if (position >= 0 && position < VIEW_CAPACITY)
            {
                switch (position)
                {
                    case VIEW_INDEX:
                        return new Margins(getResources().getDimensionPixelSize(R.dimen.home_bar_icon_margins), 0, 0, 0);
                    case VIEW_TASK:
                        return null;
                    case VIEW_MY:
                        return null;
                    case VIEW_ACTIVITIES:
                        return new Margins(0, 0, getResources().getDimensionPixelSize(R.dimen.home_bar_icon_margins), 0);
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public Integer getPageIcon(int index) {
            if (index >= 0 && index < VIEW_CAPACITY)
            {
                switch (index)
                {
                    case VIEW_INDEX:
                        return R.mipmap.tabbar_home;
                    case VIEW_TASK:
                        return R.mipmap.tabbar_message_center;
                    case VIEW_ACTIVITIES:
                        return R.mipmap.tabbar_discover;
                    case VIEW_MY:
                        return R.mipmap.tabbar_profile;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Integer getPageSelectIcon(int index) {
            if (index >= 0 && index < VIEW_CAPACITY) {
                switch (index) {
                    case VIEW_INDEX:
                        //我把“tabbar_home_selected”换成了“tabbar_home_highlighted”，selected可以删了。
                        return R.mipmap.tabbar_home_highlighted;
                    case VIEW_TASK:
                        return R.mipmap.tabbar_message_center_highlighted;
                    case VIEW_ACTIVITIES:
                        return R.mipmap.tabbar_discover_highlighted;
                    case VIEW_MY:
                        return R.mipmap.tabbar_profile_highlighted;
                    default:
                        break;
                }
            }
            return 0;
        }

        @Override
        public Rect getPageIconBounds(int position) {
            return new Rect(0, 0, mSize, mSize);
        }

        @Override
        public int[] getTipsRule(int position) {
            if (position >= 0 && position < VIEW_CAPACITY) {
                switch (position) {
                    case VIEW_INDEX:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_LEFT};
                    case VIEW_TASK:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_LEFT};
                    case VIEW_MY:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_RIGHT};
                    case VIEW_ACTIVITIES:
                        return new int[]{
                                RelativeLayout.ALIGN_PARENT_RIGHT};
                    default:
                        break;
                }
            }
            return new int[0];
        }

        @Override
        public Margins getTipsMargins(int position) {
            if (position >= 0 && position < VIEW_CAPACITY) {
                switch (position) {
                    case VIEW_INDEX:
                        return new Margins(4 * getResources().getDimensionPixelSize(R.dimen.psts_dot_m_right), 0, 0, 0);
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public Drawable getTipsDrawable(int position) {
            return null;
        }
    }

}
