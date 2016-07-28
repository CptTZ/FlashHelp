package org.gis4.xfb.hurricanehelp.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;

import com.avos.avoscloud.AVUser;

import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;

/**
 * 基本Fragment，用于同步登录状态和位置
 */
public class BaseFragment extends Fragment
{
    protected BaseFragment baseF;

    /**
     * 定位信息
     */
    protected AmapLocationSource locationSource;
    private AVUser currentUser;

    public String getUserId()
    {
        if (this.currentUser != null)
            return currentUser.getObjectId();
        return "";
    }

    public void showError(String msg)
    {
        showError(msg, this.baseF.getContext());
    }

    public void showError(String msg, Context c)
    {
        new AlertDialog.Builder(c)
                .setTitle("错误")
                .setMessage(msg)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        }).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.baseF = this;
        this.currentUser = AVUser.getCurrentUser();
        this.locationSource = new AmapLocationSource(this.getContext());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        locationSource.stopLocation();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        locationSource.startLocation();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        locationSource.deactivate();
    }

}
