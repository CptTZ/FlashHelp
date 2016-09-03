package org.gis4.xfb.hurricanehelp.fragments.main;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.BaseActivity;
import org.gis4.xfb.hurricanehelp.data.Dbconnect;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.data.testXfbTask;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * “发现”界面
 */
public class ActivitiesFragment extends BaseFragment
{
    private RecyclerView mRecyclerView;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RecyclerAdapter mAdapter;
    private List<XfbTask> taskData;

    public static ActivitiesFragment instance(BaseActivity b) {
        ActivitiesFragment view = new ActivitiesFragment();
        view.SetBaseActivity(b);
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return baseF.initLoginUi(this,inflater,container,R.layout.fragment_activities);
    }

    @Override
    public void onStart() {
        super.onStart();
        initSwipeRefresh();
    }

    /**
     * 初始化下拉刷新，不涉及数据
     */
    private void initSwipeRefresh()
    {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) getActivity().findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setOnRefreshListener(new WaveSwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                new RefreshXfbTask().execute();
            }
        });

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveRGBColor(14, 135, 234);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AVAnalytics.onFragmentEnd("Activities-Frag");
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AVAnalytics.onFragmentStart("Activities-Frag");
    }

    /**
     * 后台异步更新数据
     */
    private class RefreshXfbTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            AMapLocation loc = baseA.getCurrentLocation();
            // 未获取到用户位置时拒绝刷新
            if(loc == null) { return ""; }
            taskData = Dbconnect.FetchCloseUnacceptedXfbTask(loc.getLatitude(),loc.getLongitude(),2);
            return String.valueOf(taskData.size());
        }

        @Override
        protected void onPostExecute(String result) {
            mWaveSwipeRefreshLayout.setRefreshing(false);
            if(result.isEmpty()|result.equals("0"))
            {
                Toast.makeText(getActivity(), "刷新任务失败", Toast.LENGTH_SHORT).show();
                super.onPostExecute(result);
                return;
            }
            Toast.makeText(getActivity(), "在您附近找到" + result + "条任务", Toast.LENGTH_SHORT).show();
            setData();
            super.onPostExecute(result);
        }
    }

    private void setData() {
        if(taskData==null) return;
        mAdapter = new RecyclerAdapter(getActivity(), taskData, baseA.getCurrentLocation());
        SlideInRightAnimationAdapter slideAdapter = new SlideInRightAnimationAdapter(mAdapter);
        slideAdapter.setFirstOnly(true);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator(.5f));
        mRecyclerView.setAdapter(slideAdapter);
    }
}
