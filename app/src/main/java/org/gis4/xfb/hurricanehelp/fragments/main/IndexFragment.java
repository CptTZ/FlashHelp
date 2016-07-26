package org.gis4.xfb.hurricanehelp.fragments.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.gis4.xfb.hurricanehelp.R;

public class IndexFragment extends Fragment
{
    public static IndexFragment instance()
    {
        IndexFragment view = new IndexFragment();
        return view;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_index, container, false);
    }


}
