package org.gis4.xfb.hurricanehelp.fragments.main;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.data.testXfbTask;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import java.util.Arrays;
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

    public static ActivitiesFragment instance() {
        ActivitiesFragment view = new ActivitiesFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (super.getUserId().isEmpty())
        {
            return baseF.initLoginUi(this,inflater,container,R.layout.fragment_activities);
        }
        // 已登陆的界面
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO/BUG: 这样的Hack，如果是登录后改布局，会遇到问题。
        if (!super.getUserId().isEmpty())
        {
            initSwipeRefresh();
        }
    }

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

    /**
     * 后台异步更新数据
     */
    private class RefreshXfbTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            XfbTask[] sample=testXfbTask.taskSample();
            taskData = Arrays.asList(sample);
            testXfbTask.saveSampleTask(sample);
            return String.valueOf(taskData.size());
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getActivity(), "共刷新" + result + "条记录", Toast.LENGTH_SHORT).show();
            setData();
            mWaveSwipeRefreshLayout.setRefreshing(false);
            super.onPostExecute(result);
        }
    }

    private void setData() {
        mAdapter = new RecyclerAdapter(getActivity(), taskData);
        SlideInRightAnimationAdapter slideAdapter = new SlideInRightAnimationAdapter(mAdapter);
        slideAdapter.setFirstOnly(true);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator(.5f));
        mRecyclerView.setAdapter(slideAdapter);
    }
}
