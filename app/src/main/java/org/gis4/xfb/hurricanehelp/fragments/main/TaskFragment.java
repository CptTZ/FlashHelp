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

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVUser;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.BaseActivity;
import org.gis4.xfb.hurricanehelp.activity.MainActivity;
import org.gis4.xfb.hurricanehelp.data.Dbconnect;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class TaskFragment extends BaseFragment
{
    private RecyclerView mRecyclerView;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RecyclerAdapter mAdapter;
    private List<XfbTask> taskData;

    public static TaskFragment instance(BaseActivity b) {
        TaskFragment t = new TaskFragment();
        t.SetBaseActivity(b);
        return t;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return baseF.initLoginUi(this,inflater,container,R.layout.fragment_task);
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
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) getActivity().findViewById(R.id.task_swipe);
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

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView_task);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AVAnalytics.onFragmentStart("Task-Frag");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AVAnalytics.onFragmentEnd("Task-Frag");
    }

    /**
     * 后台异步更新数据
     */
    private class RefreshXfbTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            if(AVUser.getCurrentUser()==null) return "";
            if(baseA.getCurrentLocation() == null) {
                return "";
            }
            taskData = Dbconnect.FetchUserRelatedXfbTask(AVUser.getCurrentUser().getObjectId());
            return String.valueOf(taskData.size());
        }

        @Override
        protected void onPostExecute(String result) {
            mWaveSwipeRefreshLayout.setRefreshing(false);
            if(result.isEmpty()|result.equals("0")) {
                Toast.makeText(getActivity(), "刷新我处理的任务失败", Toast.LENGTH_SHORT).show();
                super.onPostExecute(result);
                return;
            }
            Toast.makeText(getActivity(), "共刷新" + result + "条我处理的任务", Toast.LENGTH_SHORT).show();
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
