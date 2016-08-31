package org.gis4.xfb.hurricanehelp.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;

import java.util.Collections;
import java.util.List;

/**
 * 获取后台数据的类
 * @author Tony
 */
public class Dbconnect
{
    /**
     * 获取任务所有数据
     * @deprecated 正常不应使用，开发中随意
     */
    public static List<XfbTask> FetchAllXfbTask()
    {
        AVQuery<XfbTask> query = AVQuery.getQuery(XfbTask.class);
        query.orderByDescending("updatedAt");
        query.limit(20);
        try {
            return query.find();
        } catch (AVException ex) {
            Log.e("Xfb_Cloud", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

}
