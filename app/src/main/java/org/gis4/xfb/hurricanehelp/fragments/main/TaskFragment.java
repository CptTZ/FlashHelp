package org.gis4.xfb.hurricanehelp.fragments.main;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gis4.xfb.hurricanehelp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaskFragment extends Fragment
{
    public static TaskFragment instance()
    {
        TaskFragment view = new TaskFragment();
        return view;
    }

    public TaskFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_task, container, false);
    }

}
