package org.gis4.xfb.hurricanehelp.fragments.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFragment extends BaseFragment
{
    public static MeFragment instance()
    {
        return new MeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view;
        if (super.getUserId().isEmpty())
        {
            view = inflater.inflate(R.layout.fragment_me_reg, container, false);
            view.findViewById(R.id.button_i_need_register).setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    baseF.showRegisterActivity();
                }
            });
            super.hasLogined = false;
        } else
        {
            view = inflater.inflate(R.layout.fragment_me_norm, container, false);
            super.hasLogined = true;
        }

        ButterKnife.bind(this, view);

        return view;
    }

}
