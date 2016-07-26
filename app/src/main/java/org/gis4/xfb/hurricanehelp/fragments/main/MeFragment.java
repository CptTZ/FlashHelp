package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gis4.xfb.hurricanehelp.R;

public class MeFragment extends Fragment
{

    public static MeFragment instance()
    {
        MeFragment view = new MeFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_me, container, false);
    }

}
