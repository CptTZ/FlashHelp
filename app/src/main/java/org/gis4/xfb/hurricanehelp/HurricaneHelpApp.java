package org.gis4.xfb.hurricanehelp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import org.gis4.xfb.hurricanehelp.activity.SplashActivity;

import java.util.ArrayList;
import java.util.List;

import org.gis4.xfb.hurricanehelp.R;

public class HurricaneHelpApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        AVOSCloud.initialize(this, "LGhLGudXgoF9oTXHTr1an03Y-gzGzoHsz", "z4Dou3Q1LNlmG176tECNBxQh");
        AVAnalytics.setAnalyticsEnabled(true);
        AVAnalytics.enableCrashReport(this, false);
        PushService.setDefaultPushCallback(this, SplashActivity.class);
    }

}
