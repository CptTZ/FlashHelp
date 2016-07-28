package org.gis4.xfb.hurricanehelp.activity;

import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.lhh.apst.library.AdvancedPagerSlidingTabStrip;
import com.lhh.apst.library.Margins;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.main.*;
import org.gis4.xfb.hurricanehelp.location.LocationManager;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener
{
    private static final int VIEW_INDEX = 0;
    private static final int VIEW_TASK = 1;
    private static final int VIEW_MY = 2;
    private static final int VIEW_ACTIVITIES = 3;
    private static final int VIEW_CAPACITY = 4;
    private int mSize = 0;

    private IndexFragment mFirstFragment = null;
    private TaskFragment mSecondFragment = null;
    private MeFragment mThirdFragment = null;
    private ActivitiesFragment mFourthFragment = null;

    @BindView(R.id.tabs)
    AdvancedPagerSlidingTabStrip mAPSTS;
    @BindView(R.id.vp_main)
    APSTSViewPager mVP;

    @OnClick(R.id.ivCenterBtn)
    public void onClick(View v)
    {
        String text;
        if (super.locationOld.getLocation() == null)
            text = "无有效位置，无法发布！";
        else
            text = "发布活动中，您的位置：" + super.locationOld.getLocation().getAddress();

        Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        super.locationOld = new LocationManager(this.getApplicationContext());
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        InitTab();
    }

    private void InitTab()
    {
        Random rnd = new Random();
        mSize = getResources().getDimensionPixelSize(R.dimen.xfb_tab_size);
        mVP.setOffscreenPageLimit(VIEW_CAPACITY);
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager());
        mVP.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        mAPSTS.setViewPager(mVP);
        mAPSTS.setOnPageChangeListener(this);
        mVP.setCurrentItem(VIEW_INDEX);
        mAPSTS.showDot(VIEW_TASK, String.valueOf((411 * rnd.nextInt(50) % 50)));
        mAPSTS.showDot(VIEW_ACTIVITIES, String.valueOf((411 * rnd.nextInt(50) % 30)));
    }

    private long mExitTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            // 1.5s内再次按下即退出
            if ((System.currentTimeMillis() - mExitTime) > 1500)
            {
                Toast.makeText(this, "再按一次返回键退出程序", Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else
            {
                super.locationOld.DestoryLocation();
                System.runFinalization();
                System.exit(0);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        super.locationOld.DestoryLocation();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        super.locationOld.StopUpateLocation();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {

    }

    @Override
    public void onPageSelected(int position)
    {

    }

    @Override
    public void onPageScrollStateChanged(int state)
    {

    }

    public class FragmentAdapter extends FragmentStatePagerAdapter implements
            AdvancedPagerSlidingTabStrip.IconTabProvider,
            AdvancedPagerSlidingTabStrip.LayoutProvider,
            AdvancedPagerSlidingTabStrip.TipsProvider
    {

        public FragmentAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            if (position >= 0 && position < VIEW_CAPACITY)
            {
                switch (position)
                {
                    case VIEW_INDEX:
                        if (null == mFirstFragment)
                            mFirstFragment = IndexFragment.instance();
                        return mFirstFragment;

                    case VIEW_TASK:
                        if (null == mSecondFragment)
                            mSecondFragment = TaskFragment.instance();
                        return mSecondFragment;

                    case VIEW_MY:
                        if (null == mThirdFragment)
                            mThirdFragment = MeFragment.instance();
                        return mThirdFragment;

                    case VIEW_ACTIVITIES:
                        if (null == mFourthFragment)
                            mFourthFragment = ActivitiesFragment.instance();
                        return mFourthFragment;
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public int getCount()
        {
            return VIEW_CAPACITY;
        }

        @Override
        public CharSequence getPageTitle(int position)
        {
            if (position >= 0 && position < VIEW_CAPACITY)
            {
                switch (position)
                {
                    case VIEW_INDEX:
                        return "首页";
                    case VIEW_TASK:
                        return "任务";
                    case VIEW_MY:
                        return "我的";
                    case VIEW_ACTIVITIES:
                        return "活动";
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public float getPageWeight(int position)
        {
            if (position >= 0 && position < VIEW_CAPACITY)
            {
                switch (position)
                {
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
        public int[] getPageRule(int position)
        {
            if (position >= 0 && position < VIEW_CAPACITY)
            {
                switch (position)
                {
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
        public Margins getPageMargins(int position)
        {
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
        public Integer getPageIcon(int index)
        {
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
        public Integer getPageSelectIcon(int index)
        {
            if (index >= 0 && index < VIEW_CAPACITY)
            {
                switch (index)
                {
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
        public Rect getPageIconBounds(int position)
        {
            return new Rect(0, 0, mSize, mSize);
        }

        @Override
        public int[] getTipsRule(int position)
        {
            if (position >= 0 && position < VIEW_CAPACITY)
            {
                switch (position)
                {
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
        public Margins getTipsMargins(int position)
        {
            if (position >= 0 && position < VIEW_CAPACITY)
            {
                switch (position)
                {
                    case VIEW_INDEX:
                        return new Margins(4 * getResources().getDimensionPixelSize(R.dimen.psts_dot_m_right), 0, 0, 0);
                    default:
                        break;
                }
            }
            return null;
        }

        @Override
        public Drawable getTipsDrawable(int position)
        {
            return null;
        }
    }
}