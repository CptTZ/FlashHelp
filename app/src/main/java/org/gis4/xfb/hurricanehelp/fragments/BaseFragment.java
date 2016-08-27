package org.gis4.xfb.hurricanehelp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.RegistActivity;
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

    public boolean hasLogined() {return this.currentUser != null;}

    private ProgressDialog prgd;
    /**
     * 显示请等待窗口
     */
    public void progressDialogShow()
    {
        prgd=ProgressDialog
                .show(this.baseF.getContext(),
                        "提示","请等待", true, false);
    }

    public void progressDialogDismiss()
    {
        if(prgd==null) return;
        prgd.dismiss();
    }

    /**
     * 显示错误窗口
     * @param msg 消息内容
     */
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

    /**
     * 更新用户信息，登陆以后重新调用一次
     */
    public void UpdateUser() {this.currentUser=AVUser.getCurrentUser();}

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

    /**
     * 显示注册界面
     */
    public void showRegisterActivity()
    {
        startActivity(new Intent(this.baseF.getContext(), RegistActivity.class));
        this.baseF.getActivity().overridePendingTransition(R.anim.splash_slidein,
                R.anim.splash_slideout);
    }

}
