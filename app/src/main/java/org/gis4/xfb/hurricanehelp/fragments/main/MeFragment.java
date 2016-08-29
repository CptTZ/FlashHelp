package org.gis4.xfb.hurricanehelp.fragments.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFragment extends BaseFragment
{
    public static MeFragment instance()
    {
        return new MeFragment();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        if (super.getUserId().isEmpty()) {
            return baseF.initLoginUi(this,inflater,container,R.layout.fragment_me_norm);
        }
        return inflater.inflate(R.layout.fragment_me_norm, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO!!: 这样的Hack估计以后会遇到问题
        if (super.getUserId().isEmpty())
        {

        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        AVAnalytics.onFragmentStart("MyAccount-Frag");
    }

    @Override
    public void onPause()
    {
        super.onPause();
        AVAnalytics.onFragmentEnd("MyAccount-Frag");
    }
}
