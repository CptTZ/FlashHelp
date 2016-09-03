package org.gis4.xfb.hurricanehelp.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
        query.setLimit(50);
        try {
            return query.find();
        } catch (AVException ex) {
            Log.e("Xfb_Cloud", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    /**
     * 获取和用户有关的XfbTask数据，排序按照新旧来
     * @param userId 用户ID
     * @return 和用户有关的XfbTask数据
     */
    public static List<XfbTask> FetchUserRelatedXfbTask(String userId)
    {
        if(userId==null||userId.isEmpty()) return Collections.emptyList();

        //TODO: 这里只包含我接的或者我完成的，不包括我发的
        //AVQuery<XfbTask> queryUserSender = AVQuery.getQuery(XfbTask.class);
        AVQuery<XfbTask> queryUserHelper = AVQuery.getQuery(XfbTask.class);
        AVQuery<XfbTask> queryNotAccepted = AVQuery.getQuery(XfbTask.class);
        //queryUserSender.whereEqualTo(XfbTask.SENDERID, userId);
        queryUserHelper.whereEqualTo(XfbTask.HELPERID, userId);
        queryNotAccepted.whereNotEqualTo(XfbTask.TASKSTATE, XfbTask.State_NotAccepted);

        AVQuery<XfbTask> queryFinal =AVQuery.or(Arrays.asList(queryNotAccepted, queryUserHelper));

        try {
            return queryFinal.find();
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
    public static List<XfbTask> FetchCloseUnacceptedXfbTask(double lat, double lng, int limit)
    {
        if(limit < 20) limit = 20;
        AVGeoPoint geoPoint = new AVGeoPoint(lat,lng);
        AVQuery<XfbTask> queryNear = AVQuery.getQuery(XfbTask.class);
        queryNear.whereNear(XfbTask.HAPPENGEOLOCATION, geoPoint);
        AVQuery<XfbTask> queryUnaccepted = AVQuery.getQuery(XfbTask.class);
        queryUnaccepted.whereEqualTo(XfbTask.TASKSTATE, XfbTask.State_NotAccepted);

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
