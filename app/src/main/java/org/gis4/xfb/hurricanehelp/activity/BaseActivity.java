package org.gis4.xfb.hurricanehelp.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;
import org.gis4.xfb.hurricanehelp.location.LocationManager;


/**
 * Created by Tony on 2016/7/26.
 * Activity基类，存储必须变量啥的
 */
public class BaseActivity extends AppCompatActivity
{
    protected LocationManager locationOld;
    protected AmapLocationSource locationSource;
    protected BaseActivity baseActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.baseActivity = this;

        //隐藏actionbar，用toolbar代替
        getSupportActionBar().hide();
    }

    public void showError(String msg)
    {
        showError(msg, this.baseActivity);
    }

    /**
     * 显示错误消息
     *
     * @param msg
     * @param c
     */
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
}
