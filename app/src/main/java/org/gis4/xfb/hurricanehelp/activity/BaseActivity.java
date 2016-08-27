package org.gis4.xfb.hurricanehelp.activity;

import android.content.res.TypedArray;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.avos.avoscloud.AVUser;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;
import org.gis4.xfb.hurricanehelp.location.LocationManager;

import java.util.ArrayList;

/**
 * Created by Tony on 2016/7/26.
 * Activity基类，存储必须变量啥的
 */
public class BaseActivity extends AppCompatActivity {
    protected LocationManager locationOld;
    protected AmapLocationSource locationSource;
    protected BaseActivity baseActivity;

    private static final int NUM_OF_ITEMS = 100;
    private static final int NUM_OF_ITEMS_FEW = 3;

    private AVUser currentUser;

    public String getUserId()
    {
        if (this.currentUser != null)
            return currentUser.getObjectId();
        return "";
    }

    public boolean hasLogined()    {return this.currentUser != null;}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.baseActivity = this;

        this.currentUser = AVUser.getCurrentUser();
    }

    public void showError(String msg)
    {
        showError(msg, this.baseActivity);
    }

    /**
     * 更新用户信息，登陆以后重新调用一次
     */
    public void UpdateUser() {this.currentUser=AVUser.getCurrentUser();}

    /**
     * 显示错误消息
     */
    public void showError(String msg, Context c) {
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

    protected int getActionBarSize() {
        TypedValue typedValue = new TypedValue();
        int[] textSizeAttr = new int[]{R.attr.actionBarSize};
        int indexOfAttrTextSize = 0;
        TypedArray a = obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = a.getDimensionPixelSize(indexOfAttrTextSize, -1);
        a.recycle();
        return actionBarSize;
    }

    protected int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
    }

}
