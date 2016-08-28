package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.MainActivity;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.SlideInRightAnimationAdapter;
import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

/**
 * A simple {@link Fragment} subclass.
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

    public ActivitiesFragment() {
    }

    private EditText loginUserName;
    private EditText loginUserPass;

    private String getEditTextUsername() {return loginUserName.getText().toString();}
    private String getEditTextPassword() {return loginUserPass.getText().toString();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        if (super.getUserId().isEmpty())
        {
            // 引导登陆或者注册
            view = inflater.inflate(R.layout.fragment_me_reg, container, false);

            loginUserName = (EditText) view.findViewById(R.id.editText_login_userName);
            loginUserPass = (EditText) view.findViewById(R.id.editText_login_userPassword);

            Button buttonLogin = (Button) view.findViewById(R.id.button_login);
            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getEditTextUsername().isEmpty()||getEditTextPassword().isEmpty())
                    {
                        baseF.showError("请填写完整登陆信息！");
                        return;
                    }
                    baseF.progressDialogShow();
                    // 下面是LeanCloud的登陆过程
                    AVUser.logInInBackground(
                            getEditTextUsername(), getEditTextPassword(),
                            new LogInCallback<AVUser>()
                            {
                                @Override
                                public void done(AVUser avUser, AVException e)
                                {
                                    baseF.progressDialogDismiss();
                                    if (avUser != null) {
                                        baseF.UpdateUser();
                                        Toast.makeText(baseF.getContext(),"登陆成功！",Toast.LENGTH_SHORT).show();
                                        //TODO: 更新一下我的界面为已经登陆
                                    } else {
                                        //TODO: 根据e判断是用户名密码还是其它问题
                                        showError("登陆失败，请确认用户名密码，或者网络情况");
                                    }
                                }
                            }
                    );
                }
            });

            Button buttonRegister = (Button) view.findViewById(R.id.button_i_need_register);
            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseF.showRegisterActivity();
                }
            });
        }
        else
        {
            // 已登陆的界面
            view = inflater.inflate(R.layout.fragment_activities, container, false);
        }

        ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO!!: 这样的Hack估计以后会遇到问题
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
                new Task().execute();
            }
        });

        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveRGBColor(14, 135, 234);

        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new SlideInLeftAnimator());
    }

    //// TODO: 2016-8-21 没有考虑到请求超时的情况，可以在请求的代码中解决。
    private class Task extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            taskData = Arrays.asList(XfbTask.taskSample());
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
