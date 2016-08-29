package org.gis4.xfb.hurricanehelp.data;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;

import java.util.Collections;
import java.util.List;

/**
 * 获取后台数据的类，因为为网络请求需要时间
 * @author Tony
 */
public class DbConnect
{
    /**
     * 获取任务所有数据
     * @deprecated 正常不应使用，开发中随意
     */
    public class FetchAllXfbDataTask extends AsyncTask<Void, Void, List<XfbTask>>
    {
        @Override
        protected List<XfbTask> doInBackground(Void... params)
        {
            AVQuery<XfbTask> query = AVQuery.getQuery(XfbTask.class);
            query.orderByDescending("updatedAt");
            query.limit(20);
            try {
                return query.find();
            } catch (AVException exception) {
                Log.e("XFB_Cloud", exception.getMessage(), exception);
                return Collections.emptyList();
            }
        }
    }

}
