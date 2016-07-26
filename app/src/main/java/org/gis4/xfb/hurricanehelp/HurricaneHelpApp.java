package org.gis4.xfb.hurricanehelp;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

public class HurricaneHelpApp extends Application
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        AVOSCloud.initialize(this, "LGhLGudXgoF9oTXHTr1an03Y-gzGzoHsz", "z4Dou3Q1LNlmG176tECNBxQh");
        AVAnalytics.setAnalyticsEnabled(true);
        AVAnalytics.enableCrashReport(this, false);

        //gittest 张宜弛大傻逼
    }

}
