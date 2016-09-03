package org.gis4.xfb.hurricanehelp.data;

import android.graphics.Bitmap;
import android.os.SystemClock;

import com.avos.avoscloud.AVACL;
import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVSaveOption;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;

import org.gis4.xfb.hurricanehelp.R;

import java.io.Serializable;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

/**
 * 旋风帮，基础任务
 * 为了用LeanCloud的StorageEngine，需要继承AVObject，它实现了Parceable，比Serializable更好
 */
@AVClassName(XfbTask.TaskClass)
public class XfbTask extends AVObject
{
    public static final Creator CREATOR = AVObjectCreator.instance;
    public static final String TaskClass = "XfbTask";

    private AVGeoPoint senderGeoLocation;

    //送达地点名称，送达地点描述，前者是由地图根据经纬度生成的，后者是用户自己手输的。
    public static final String SENDERLOCATIONAUTODESC = "senderLocationAutoDesc";
    public static final String SENDERLOCATIONMANUALDESC = "senderLocationManualDesc";
    //执行地点名称，执行地点描述，前者是由地图根据经纬度生成的，后者是用户自己手输的。
    public static final String HAPPENLOCATIONAUTODESC = "happenLocationAutoDesc";
    public static final String HAPPENLOCATIONMANUALDESC = "happenLocationManualDesc";

    //任务标题，详细描述，发送人ID，接单人ID，任务类型，完成情况
    public static final String TITLE = "title";
    public static final String DESC = "desc";
    public static final String SENDERID = "senderId";
    public static final String HELPERID = "helperId";
    public static final String TASKTYPE = "taskType";
    public static final String TASKSTATE = "taskState";

    //送达地点坐标，执行地点坐标，预计执行时间
    public static final String SENDERLAT = "senderLat";
    public static final String SENDERLNG = "senderLng";
    public static final String HAPPENGEOLOCATION = "happenGeoLocation";
    public static final String DUR = "dur";

    //任务开始时间，截至时间，奖励积分
    public static final String STARTTIME = "startTime";
    public static final String ENDTIME = "endTime";
    public static final String REWARDPOINT = "rewardPoint";

    //5张图片
    public static final String DESCIMAGE = "descImage";

    private String[] imagePaths;
    private Bitmap[] lowQualityBitmaps;
    private Bitmap[] highQualityBitmaps;

    public XfbTask() {
        put(SENDERID, AVUser.getCurrentUser().getObjectId());
        put(TASKSTATE, State_NotAccepted);
        put(HAPPENGEOLOCATION, new AVGeoPoint(0,0));
    }

    /**
     * 任务状态，0: 刚发没人接；1: 执行中；2: 已完成
     */
    public static final int State_NotAccepted=0, State_Processing=1, State_Finished=2;

    public XfbTask(String title, String desc, String taskType,
                   double senderLat, double senderLng, double happenLat, double happenLng, double dur,
                   String senderLocation, String senderLocationDescription,
                   String happenLocation, String happenLocationDescription,
                   Date startTime, Date endTime, int rewardPoint) {
        put(TITLE, title);
        put(DESC, desc);
        put(SENDERID, AVUser.getCurrentUser().getObjectId());
        put(TASKTYPE, taskType);
        senderGeoLocation = new AVGeoPoint(senderLat,senderLng);
        AVGeoPoint happen = new AVGeoPoint(happenLat,happenLng);
        put(SENDERLAT, senderLat);
        put(SENDERLNG, senderLng);
        put(HAPPENGEOLOCATION, happen);
        put(DUR, dur);
        put(SENDERLOCATIONAUTODESC, senderLocation);
        put(SENDERLOCATIONMANUALDESC, senderLocationDescription);
        put(HAPPENLOCATIONAUTODESC, happenLocation);
        put(HAPPENLOCATIONMANUALDESC, happenLocationDescription);
        put(STARTTIME, startTime);
        put(ENDTIME, endTime);
        put(REWARDPOINT, rewardPoint);
        put(TASKSTATE, 0);
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
            return imageList.get("其他");
        }
    }

    public static int getLogoOfTaskType(int taskState) {
        if(taskState == 1) return R.mipmap.task_state_ing;
        if(taskState == 2) return R.mipmap.task_state_ok;

        return R.mipmap.task_state_ok;
    }

    /**
     * Task权限设置，只让当前用户修改
     */
    private void aclSaveSet()
    {
        AVACL acl = new AVACL();
        acl.setPublicReadAccess(true);
        acl.setWriteAccess(AVUser.getCurrentUser(), true);
        this.setACL(acl);
    }

    @Override
    public void save() throws AVException
    {
        aclSaveSet();
        super.save();
    }

    @Override
    public void saveInBackground()
    {
        aclSaveSet();
        super.saveInBackground();
    }

    @Override
    public void saveInBackground(AVSaveOption o)
    {
        aclSaveSet();
        super.saveInBackground(o);
    }

    @Override
    public void saveInBackground(SaveCallback c)
    {
        aclSaveSet();
        super.saveInBackground(c);
    }

    @Override
    public void saveInBackground(AVSaveOption o, SaveCallback c)
    {
        aclSaveSet();
        super.saveInBackground(o, c);
    }

    public String getTitle(){ return getString(TITLE); }
    public String getDesc(){ return getString(DESC); }
    public String getSenderId(){ return getString(SENDERID); }
    public String getHelperId(){ return getString(HELPERID); }
    public String getTaskType(){ return getString(TASKTYPE); }
    public AVGeoPoint getSenderGeoLocation(){ return this.senderGeoLocation; }
    public AVGeoPoint getHappenGeoLocation(){ return getAVGeoPoint(HAPPENGEOLOCATION); }
    public String getSenderLocationManualDesc(){ return getString(SENDERLOCATIONMANUALDESC); }
    public String getSenderLocationAutoDesc(){ return getString(SENDERLOCATIONAUTODESC); }
    public String getHappenLocation(){ return getString(HAPPENLOCATIONMANUALDESC); }
    public String getHappenLocationDescription(){ return getString(HAPPENLOCATIONAUTODESC); }
    public double getDur(){ return getDouble(DUR); }
    public double getSenderLat(){ return getDouble(SENDERLAT); }
    public double getSenderLng(){ return getDouble(SENDERLNG); }
    public double getHappenLat(){ return getAVGeoPoint(HAPPENGEOLOCATION).getLatitude(); }
    public double getHappenLng(){ return getAVGeoPoint(HAPPENGEOLOCATION).getLongitude(); }
    public Date getStartTime(){ return getDate(STARTTIME); }
    public Date getEndTime(){ return getDate(ENDTIME); }
    public int getRewardPoint(){ return getInt(REWARDPOINT); }
    public AVFile getDescImage(int i){ return getAVFile(DESCIMAGE + String.valueOf(i)); }
    public int getTaskState() { return getInt(TASKSTATE); }
    public void setTitle(String d) { put(TITLE, d); }
    public void setDesc(String d) { put(DESC, d); }
    public void setHelperId(String d) { put(HELPERID, d); }
    public void setTaskType(String d) { put(TASKTYPE, d); }
    public void setSenderGeoLocation(AVGeoPoint d) { this.senderGeoLocation=d; }
    public void setHappenGeoLocation(AVGeoPoint d) { put(HAPPENGEOLOCATION, d); }
    public void setSenderLat(double d) { put(SENDERLAT,d); }
    public void setSenderLng(double d) { put(SENDERLNG,d); }
    public void setHappenLat(double d)
    {
        AVGeoPoint old = getAVGeoPoint(HAPPENGEOLOCATION);
        if(old != null)
        {
            old.setLatitude(d);
            put(HAPPENGEOLOCATION, old);
            return;
        }
        AVGeoPoint tmpGeo = new AVGeoPoint();
        tmpGeo.setLatitude(d);
        put(HAPPENGEOLOCATION, tmpGeo);
    }
    public void setHappenLng(double d)
    {
        AVGeoPoint old = getAVGeoPoint(HAPPENGEOLOCATION);
        if(old != null)
        {
            old.setLongitude(d);
            put(HAPPENGEOLOCATION, old);
            return;
        }
        AVGeoPoint tmpGeo = new AVGeoPoint();
        tmpGeo.setLongitude(d);
        put(HAPPENGEOLOCATION, tmpGeo);
    }
    public void setDur(double d) { put(DUR, d); }
    public void setSenderlocationManualDesc(String d) { put(SENDERLOCATIONMANUALDESC, d); }
    public void setSenderLocationAutoDesc(String d) { put(SENDERLOCATIONAUTODESC, d); }
    public void setHappenLocationManualDesc(String d) { put(HAPPENLOCATIONMANUALDESC, d); }
    public void setHappenLocationAutoDesc(String d) { put(HAPPENLOCATIONAUTODESC, d); }
    public void setStartTime(Date d) { put(STARTTIME, d); }
    public void setEndTime(Date d) { put(ENDTIME, d); }
    public void setRewardPoint(int d) { put(REWARDPOINT, d); }
    public void setDescImage(AVFile d, int i) { put(DESCIMAGE + String.valueOf(i), d); }
    public void setTaskstate(int d) { put(TASKSTATE, d); }

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
