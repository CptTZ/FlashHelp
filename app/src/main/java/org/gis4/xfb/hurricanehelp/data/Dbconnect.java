package org.gis4.xfb.hurricanehelp.data;

import android.util.Log;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVCloudQueryResult;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CloudQueryCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.LogUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 获取后台数据的类（均不在后台，所以请保证该类在后台线程中运行）
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
     * userId->UserName
     * @param userId ID
     * @param text TextView
     */
    public static void UpdateSenderIdToUserName(String userId, final TextView text)
    {
        if(userId==null||userId.isEmpty()) return;
        //String sql = "SELECT username FROM `_User` WHERE objectId = '" + userId + "'";
        AVQuery<AVUser> userQuery = new AVQuery<>("_User");
        userQuery.whereEqualTo("objectId", userId);
        userQuery.setLimit(1);

        /*AVQuery.doCloudQueryInBackground(sql, new CloudQueryCallback<AVCloudQueryResult>()
        {
            @Override
            public void done(AVCloudQueryResult a, AVException e) {
                if(e==null) {
                    text.setText(a.getResults().get(0).getString("username"));
                } else {
                    Log.e("Xfb_Cloud", e.getMessage(), e);
                }
            }
        });*/
        userQuery.findInBackground(new FindCallback<AVUser>() {
            @Override
            public void done(List<AVUser> list, AVException e) {
                if(e==null) {
                    text.setText(list.get(0).getUsername());
                } else {
                    Log.e("Xfb_Cloud", e.getMessage(), e);
                }
            }
        });
    }

    /**
     * 获取所有状态为未接收的任务
     * @return 未完成任务
     */
    public static List<XfbTask> FetchNotAccpetedXfbTask()
    {
        AVQuery<XfbTask> query = AVQuery.getQuery(XfbTask.class);
        query.orderByDescending("updatedAt");
        query.whereEqualTo(XfbTask.TASKSTATE, XfbTask.State_NotAccepted);
        try {
            return query.find();
        } catch (AVException ex) {
            Log.e("Xfb_Cloud", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

    /**
     * 获取用户发送的XfbTask的所有数量,并更新到TextView
     * @param userId 用户ID
     * @param text TextView
     */
    public static void UpdateMeSentAllXfbTaskCount(String userId, final TextView text)
    {
        if(userId==null||userId.isEmpty()) return;
        String sql = "SELECT count(*) FROM XfbTask WHERE " + XfbTask.SENDERID + " = '" + userId + "'";
        try {
            AVQuery.doCloudQueryInBackground(sql, new CloudQueryCallback<AVCloudQueryResult>()
            {
                @Override
                public void done(AVCloudQueryResult a, AVException e)
                {
                    if(e==null) { text.setText(String.valueOf(a.getCount())); }
                    else { Log.e("Xfb_Cloud", e.getMessage(), e); }
                }
            });
        } catch (Exception ex) {
            Log.e("Xfb_Cloud", ex.getMessage(), ex);
        }
    }

    /**
     * 获取用户发送的XfbTask数据，排序按照新旧来
     * @param userId 用户ID
     * @return 用户发送的fbTask数据
     */
    public static List<XfbTask> FetchMeSentAllXfbTask(String userId)
    {
        if(userId==null||userId.isEmpty()) return Collections.emptyList();

        AVQuery<XfbTask> queryMe = AVQuery.getQuery(XfbTask.class);
        queryMe.whereEqualTo(XfbTask.SENDERID, userId);
        queryMe.orderByDescending("updatedAt");
        try {
            return queryMe.find();
        } catch (Exception ex) {
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

        //这里只包含我接的或者我完成的，不包括我发的
        //AVQuery<XfbTask> queryUserSender = AVQuery.getQuery(XfbTask.class);
        AVQuery<XfbTask> queryUserHelper = AVQuery.getQuery(XfbTask.class);
        AVQuery<XfbTask> queryNotAccepted = AVQuery.getQuery(XfbTask.class);
        //queryUserSender.whereEqualTo(XfbTask.SENDERID, userId);
        queryUserHelper.whereEqualTo(XfbTask.HELPERID, userId);
        queryNotAccepted.whereNotEqualTo(XfbTask.TASKSTATE, XfbTask.State_NotAccepted);

        AVQuery<XfbTask> queryFinal = AVQuery.and(Arrays.asList(queryNotAccepted, queryUserHelper));
        queryFinal.orderByDescending("updatedAt");

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
     * @param dist 距离公里（至少1公里）
     * @return 用户附近的Task
     */
    public static List<XfbTask> FetchCloseUnacceptedXfbTask(double lat, double lng, double dist)
    {
        if(dist < 1) dist = 1;
        AVGeoPoint geoPoint = new AVGeoPoint(lat,lng);
        AVQuery<XfbTask> queryNear = AVQuery.getQuery(XfbTask.class);
        queryNear.whereWithinKilometers(XfbTask.HAPPENGEOLOCATION, geoPoint, dist);
        AVQuery<XfbTask> queryUnaccepted = AVQuery.getQuery(XfbTask.class);
        queryUnaccepted.whereEqualTo(XfbTask.TASKSTATE, XfbTask.State_NotAccepted);

        AVQuery<XfbTask> query = AVQuery.and(Arrays.asList(queryNear, queryUnaccepted));

        try {
            return query.find();
        } catch (AVException ex) {
            Log.e("Xfb_Cloud", ex.getMessage(), ex);
            return Collections.emptyList();
        }
    }

}
