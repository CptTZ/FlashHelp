package org.gis4.xfb.hurricanehelp.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskFragment extends BaseFragment
{
    public static TaskFragment instance()
    {
        return new TaskFragment();
    }

    @OnClick(R.id.button_i_need_register)
    public void Show()
    {
        super.showError("FUCK");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view;
        if (super.getUserId().isEmpty())
        {
            view = inflater.inflate(R.layout.fragment_me_reg, container, false);
            super.hasLogined = false;
        } else
        {
            view = inflater.inflate(R.layout.fragment_task, container, false);
            super.hasLogined = true;
        }

        ButterKnife.bind(this, view);

        return view;
    }

}
