package org.gis4.xfb.hurricanehelp.fragments.main;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import butterknife.ButterKnife;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class TaskFragment extends BaseFragment
{
    public static TaskFragment instance() {
        return new TaskFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        //// TODO: 2016-8-19 先把登陆验证停掉
//        if (super.getUserId().isEmpty())
//        {
//            view = inflater.inflate(R.layout.fragment_me_reg, container, false);
//            super.hasLogined = false;
//            view.findViewById(R.id.button_i_need_register).setOnClickListener(new View.OnClickListener()
//            {
//                @Override
//                public void onClick(View view)
//                {
//                    baseF.showError("FUCK");
//                }
//            });
//        } else
//        {
//            view = inflater.inflate(R.layout.fragment_task, container, false);
//            super.hasLogined = true;
//        }

        view = inflater.inflate(R.layout.fragment_task, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}
