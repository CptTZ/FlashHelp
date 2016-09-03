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
import org.gis4.xfb.hurricanehelp.activity.BaseActivity;
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

    private View meView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        meView = baseF.initLoginUi(this,inflater,container,R.layout.fragment_me_reg_and_norm);
        return meView;
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO: 2016-09-03 更新用户的信息（邮箱和用户名）
        if (!super.getUserId().isEmpty())
        {

        }
    }

    /**
     * 更新用户信息
     */
    private void UpdateUserData()
    {

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
