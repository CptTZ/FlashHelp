package org.gis4.xfb.hurricanehelp.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 获取后台数据的类（均不在后台，所以请保证该类在后台线程中运行
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
        try {
            return query.find();
        } catch (AVException ex) {
            Log.e("Xfb_Cloud", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    /**
     * 获取用户位置附近的limit条未完成的XfbTask数据，排序按照距离来
     * @param lat 纬度
     * @param lng 精度
     * @param limit 返回数据条数（至少20）
     * @return 用户附近的Task
     */
    public static List<XfbTask> FetchCloseXfbTask(double lat, double lng, int limit)
    {
        if(limit < 20) limit = 20;
        AVGeoPoint geoPoint = new AVGeoPoint(lat,lng);
        AVQuery<XfbTask> queryNear = AVQuery.getQuery(XfbTask.class);
        queryNear.whereNear(XfbTask.HAPPENGEOLOCATION, geoPoint);
        AVQuery<XfbTask> queryUnaccepted = AVQuery.getQuery(XfbTask.class);
        queryUnaccepted.whereEqualTo(XfbTask.TASKSTATE, 0);

        AVQuery<XfbTask> query = AVQuery.and(Arrays.asList(queryNear, queryUnaccepted));
        query.setLimit(limit);

        try {
            return query.find();
        } catch (AVException ex) {
            Log.e("Xfb_Cloud", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

}
