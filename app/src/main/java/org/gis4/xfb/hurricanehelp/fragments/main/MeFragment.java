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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.BaseActivity;
import org.gis4.xfb.hurricanehelp.activity.MyPublishActivity;
import org.gis4.xfb.hurricanehelp.data.Dbconnect;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFragment extends BaseFragment
{
    public static MeFragment instance(BaseActivity b)
    {
        MeFragment m = new MeFragment();
        m.SetBaseActivity(b);
        return m;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.meView = baseF.initLoginUi(this,inflater,container,R.layout.fragment_me_reg_and_norm);
        return super.meView;
    }

    @Override
    public void onStart() {
        super.onStart();
        UpdateUserData();

        LinearLayout alreadyPublish = (LinearLayout) getActivity().findViewById(R.id.already_publish);

        alreadyPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyPublishActivity.class);
                startActivity(intent);
            }
        });
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
