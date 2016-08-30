package org.gis4.xfb.hurricanehelp.data;


import android.graphics.Bitmap;
import android.os.SystemClock;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import org.gis4.xfb.hurricanehelp.R;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

/**
 * 旋风帮，基础任务
 * 引入Serializable接口，这样XfbTask可以作为参数在activity间传递
 * 为了用LeanCloud的StorageEngine，需要继承AVObject
 */
@AVClassName("XfbTask")
public class XfbTask extends AVObject implements Serializable
{
    public static final Creator CREATOR = AVObjectCreator.instance;

    //送达地点名称，送达地点描述，前者是由地图根据经纬度生成的，后者是用户自己手输的。
    public static final String SENDERLOCATION = "senderLocation";
    public static final String SENDERLOCATIONDESCRIPTION = "senderLocationDescription";
    //执行地点名称，执行地点描述，前者是由地图根据经纬度生成的，后者是用户自己手输的。
    public static final String HAPPENLOCATION = "happenLocation";
    public static final String HAPPENLOCATIONDESCRIPTION = "happenLocationDescription";

    //任务标题，详细描述，发送人ID，任务类型
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String SENDERID = "senderId";
    public static final String TASKTYPE = "taskType";

    //送达地点坐标，执行地点坐标，预计执行时间
    public static final String SENDERLAT = "senderLat";
    public static final String SENDERLNG = "senderLng";
    public static final String HAPPENLAT = "happenLat";
    public static final String HAPPENLNG = "happenLng";
    public static final String DUR = "dur";

    //任务开始时间，截至时间，奖励积分
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String REWARDPOINT = "rewardPoint";

    //一张图片（目前只可以一个）
    public static final String DESCIMAGE = "descImage";

    //图片路径，我把本地路径和bitmap图片都保存了。
    private String[] imagePaths;
    private Bitmap[] lowQualityBitmaps;
    private Bitmap[] highQualityBitmaps;

    public XfbTask() { }

    public XfbTask(String title, String desc, String senderId, String taskType,
                   double senderLat, double senderLng, double happenLat, double happenLng, double dur,
                   String senderLocation, String senderLocationDescription,
                   String happenLocation, String happenLocationDescription,
                   Date startTime, Date endTime, int rewardPoint) {
        put(TITLE, title);
        put(DESC, desc);
        put(SENDERID, senderId);
        put(TASKTYPE, taskType);
        put(SENDERLAT, senderLat);
        put(SENDERLNG, senderLng);
        put(HAPPENLAT, happenLat);
        put(HAPPENLNG, happenLng);
        put(DUR, dur);
        put(SENDERLOCATION, senderLocation);
        put(SENDERLOCATIONDESCRIPTION, senderLocationDescription);
        put(HAPPENLOCATION, happenLocation);
        put(HAPPENLOCATIONDESCRIPTION, happenLocationDescription);
        put(STARTTIME, startTime);
        put(ENDTIME, endTime);
        put(REWARDPOINT, rewardPoint);
    }

    public static int getLogoOfTaskType(String taskType) {
        HashMap<String, Integer> imageList = new HashMap<String, Integer>();
        imageList.put("拿快递", R.mipmap.task_type_kuaidi);
        imageList.put("帮干活", R.mipmap.task_type_ganhuo);
        imageList.put("其他", R.mipmap.task_type_qita);
        imageList.put("送达", R.mipmap.task_send);
        imageList.put("执行", R.mipmap.task_happen);

        try {
            return imageList.get(taskType);
        }
        catch (Exception e) {
            return imageList.get("拿快递");
        }

    }

    public String getTitle(){ return getString(TITLE); }
    public String getDesc(){ return getString(DESC); }
    public String getSenderId(){ return getString(SENDERID); }
    public String getTaskType(){ return getString(TASKTYPE); }
    public double getSenderLat(){ return getDouble(SENDERLAT); }
    public double getSenderLng(){ return getDouble(SENDERLNG); }
    public double getHappenLat(){ return getDouble(HAPPENLAT); }
    public double getHappenLng(){ return getDouble(HAPPENLNG); }
    public double getDur(){ return getDouble(DUR); }
    public String getSenderLocation(){ return getString(SENDERLOCATION); }
    public String getSenderLocationDescription(){ return getString(SENDERLOCATIONDESCRIPTION); }
    public String getHappenLocation(){ return getString(HAPPENLOCATION); }
    public String getHappenLocationDescription(){ return getString(HAPPENLOCATIONDESCRIPTION); }
    public Date getStartTime(){ return getDate(STARTTIME); }
    public Date getEndTime(){ return getDate(ENDTIME); }
    public int getRewardPoint(){ return getInt(REWARDPOINT); }
    public void setTitle(String d) { put(TITLE, d); }
    public void setDesc(String d) { put(DESC, d); }
    public void setSenderId(String d) { put(SENDERID, d); }
    public void setTaskType(String d) { put(TASKTYPE, d); }
    public void setSenderLat(double d) { put(SENDERLAT, d); }
    public void setSenderLng(double d) { put(SENDERLNG, d); }
    public void setHappenLat(double d) { put(HAPPENLAT, d); }
    public void setHappenLng(double d) { put(HAPPENLNG, d); }
    public void setDur(double d) { put(DUR, d); }
    public void setSenderLocation(String d) { put(SENDERLOCATION, d); }
    public void setSenderLocationDescription(String d) { put(SENDERLOCATIONDESCRIPTION, d); }
    public void setHappenLocation(String d) { put(HAPPENLOCATION, d); }
    public void setHappenLocationDescription(String d) { put(HAPPENLOCATIONDESCRIPTION, d); }
    public void setStartTime(Date d) { put(STARTTIME, d); }
    public void setEndTime(Date d) { put(ENDTIME, d); }
    public void setRewardPoint(int d) { put(REWARDPOINT, d); }

    public String[] getImagePaths() {
        return imagePaths;
    }
    public void setImagePaths(String[] imagePaths) {
        this.imagePaths = imagePaths;
    }
    public Bitmap[] getLowQualityBitmaps() {
        return lowQualityBitmaps;
    }
    public void setLowQualityBitmaps(Bitmap[] lowQualityBitmaps) { this.lowQualityBitmaps = lowQualityBitmaps; }
    public Bitmap[] getHighQualityBitmaps() {
        return highQualityBitmaps;
    }
    public void setHighQualityBitmaps(Bitmap[] highQualityBitmaps) { this.highQualityBitmaps = highQualityBitmaps; }
}
