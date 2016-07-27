package org.gis4.xfb.hurricanehelp.activity;

import android.os.Bundle;
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
}
