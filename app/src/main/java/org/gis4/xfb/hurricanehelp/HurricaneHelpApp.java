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

import com.orm.androrm.DatabaseAdapter;
import com.orm.androrm.Model;

import org.gis4.xfb.hurricanehelp.LogQuickSearchData.LogQuickSearch;
import org.gis4.xfb.hurricanehelp.activity.SplashActivity;

import java.util.ArrayList;
import java.util.List;

import org.gis4.xfb.hurricanehelp.R;

public class HurricaneHelpApp extends Application
{
    private static HurricaneHelpApp appContext;

    @Override
    public void onCreate()
    {
        super.onCreate();
        AVOSCloud.initialize(this, "LGhLGudXgoF9oTXHTr1an03Y-gzGzoHsz", "z4Dou3Q1LNlmG176tECNBxQh");
        AVAnalytics.setAnalyticsEnabled(true);
        AVAnalytics.enableCrashReport(this, false);
        PushService.setDefaultPushCallback(this, SplashActivity.class);

        initializeDatabase();
    }

    public static Context context() {
        return appContext;
    }

    //actionbar搜索框
    private void initializeDatabase() {
        List<Class<? extends Model>> models = new ArrayList<>(0);
        models.add(LogQuickSearch.class);
        String dbName = this.getResources().getString(R.string.database_name);
        DatabaseAdapter.setDatabaseName(dbName);
        DatabaseAdapter adapter = new DatabaseAdapter(appContext);
        adapter.setModels(models);
    }

}
