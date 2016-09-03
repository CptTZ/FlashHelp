package org.gis4.xfb.hurricanehelp.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.Dbconnect;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.fragments.main.RecyclerAdapter;

import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class MyPublishActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RecyclerAdapter mAdapter;
    private List<XfbTask> taskData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);

        initSwipeRefresh();
    }

    /**
     * 初始化下拉刷新，不涉及数据
     */
    private void initSwipeRefresh()
    {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.task_swipe);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView_task);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
    }

    /**
     * 后台异步更新数据
     */
    private class RefreshXfbTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            if(AVUser.getCurrentUser() == null) return "";
            //if(getCurrentLocation() == null)  return "";
            taskData = Dbconnect.FetchMeSentAllXfbTask(AVUser.getCurrentUser().getObjectId());
            return String.valueOf(taskData.size());
        }

        @Override
        protected void onPostExecute(String result) {
            mWaveSwipeRefreshLayout.setRefreshing(false);
            if(result.isEmpty()) {
                Toast.makeText(MyPublishActivity.this, "刷新我处理的任务失败", Toast.LENGTH_SHORT).show();
                super.onPostExecute(result);
                return;
            }
            Toast.makeText(MyPublishActivity.this, "共刷新" + result + "条我处理的任务", Toast.LENGTH_SHORT).show();
            setData();
            super.onPostExecute(result);
        }
    }

    private void setData() {
        if(taskData==null) return;
        mAdapter = new RecyclerAdapter(this, taskData, baseActivity.getCurrentLocation());
        SlideInRightAnimationAdapter slideAdapter = new SlideInRightAnimationAdapter(mAdapter);
        slideAdapter.setFirstOnly(true);
        slideAdapter.setDuration(500);
        slideAdapter.setInterpolator(new OvershootInterpolator(.5f));
        mRecyclerView.setAdapter(slideAdapter);
    }
}
