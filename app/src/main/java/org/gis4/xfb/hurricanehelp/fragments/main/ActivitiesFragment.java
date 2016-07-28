package org.gis4.xfb.hurricanehelp.fragments.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActivitiesFragment extends BaseFragment
{
    public static ActivitiesFragment instance()
    {
        ActivitiesFragment view = new ActivitiesFragment();
        return view;
    }

    public ActivitiesFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

}
