package org.gis4.xfb.hurricanehelp;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import org.gis4.xfb.hurricanehelp.activity.SplashActivity;

import org.gis4.xfb.hurricanehelp.data.XfbTask;

public class HurricaneHelpApp extends Application
{
    private static HurricaneHelpApp appContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        HurricaneHelpApp.appContext = this;
        // 注册用户自定义类，以便数据上传，需要在Init之前用
        AVObject.registerSubclass(XfbTask.class);

        AVOSCloud.initialize(this, "LGhLGudXgoF9oTXHTr1an03Y-gzGzoHsz", "z4Dou3Q1LNlmG176tECNBxQh");
        AVAnalytics.setAnalyticsEnabled(true);
        //AVAnalytics.enableCrashReport(this, true);
        PushService.setDefaultPushCallback(this, SplashActivity.class);
    }

    public static Context context() {
        return appContext;
    }

}
