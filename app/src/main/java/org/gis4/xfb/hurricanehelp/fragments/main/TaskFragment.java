package org.gis4.xfb.hurricanehelp.fragments.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.avos.avoscloud.AVAnalytics;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

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
}
