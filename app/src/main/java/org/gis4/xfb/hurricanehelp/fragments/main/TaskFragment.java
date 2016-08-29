package org.gis4.xfb.hurricanehelp.fragments.main;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class TaskFragment extends BaseFragment
{
    public static TaskFragment instance() {
        return new TaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (super.getUserId().isEmpty())
        {
            return baseF.initLoginUi(this,inflater,container,R.layout.fragment_task);
        }
        // 已登陆的界面
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

}
