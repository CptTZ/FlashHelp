package org.gis4.xfb.hurricanehelp.data;


import android.graphics.Bitmap;
import android.os.SystemClock;

import org.gis4.xfb.hurricanehelp.R;

import java.util.Date;
import java.util.HashMap;

/**
 * 旋风帮，基础任务
 */
//// TODO: 2016-8-18 这个类里面还要记录发单人，就是发这个单的用户id，别忘了。
public class XfbTask
{
    private String title, desc, senderId, taskType;

    //送达地点坐标，执行地点坐标
    private double senderLat, senderLng, happenLat, happenLng, dur;

    //送达地点名称，送达地点描述，前者是由地图根据经纬度生成的，后者是用户自己手输的。
    private String senderLocation, senderLocationDescription;
    //执行地点名称，执行地点描述，前者是由地图根据经纬度生成的，后者是用户自己手输的。
    private String happenLocation, happenLocationDescription;

    //任务开始时间，截至时间
    private Date startTime, endTime;

    //奖励积分
    private int rewardPoint;

    //图片，我把本地路径和bitmap图片都保存了。
    private String[] imagePaths;
    private Bitmap[] lowQualityBitmaps;
    private Bitmap[] highQualityBitmaps;

    public XfbTask() { }

    public XfbTask(String title, String desc, String senderId, String taskType,
                   double senderLat, double senderLng, double happenLat, double happenLng, double dur,
                   String senderLocation, String senderLocationDescription,
                   String happenLocation, String happenLocationDescription,
                   Date startTime, Date endTime, int rewardPoint) {
        this.title=title;
        this.desc=desc;
        this.senderId=senderId;
        this.taskType=taskType;
        this.senderLat=senderLat;
        this.senderLng=senderLng;
        this.happenLat=happenLat;
        this.happenLng=happenLng;
        this.dur=dur;
        this.senderLocation=senderLocation;
        this.senderLocationDescription=senderLocationDescription;
        this.happenLocation=happenLocation;
        this.happenLocationDescription=happenLocationDescription;
        this.startTime=startTime;
        this.endTime=endTime;
        this.rewardPoint=rewardPoint;
    }

    public static XfbTask[] taskSample(){
        XfbTask[] taskList = new XfbTask[8];

        //desc字段后加上一个随机数，以此判断是否真刷新了
        taskList[0] = new XfbTask(
                "", "帮忙去北区复印店拿顺丰快递。" + (int)(Math.random()*10), "", "拿快递",
                32.1049720000, 118.9142240000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 50);

        taskList[1] = new XfbTask(
                "", "东区校园促销活动需要帮手，在线等，急" + (int)(Math.random()*10), "", "帮干活",
                32.1072240000,118.9301580000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 60);

        taskList[2] = new XfbTask(
                "", "帮忙去正门拿快递，再寄给指定地点" + (int)(Math.random()*10), "", "拿快递",
                32.1000990000,118.9189140000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 70);

        taskList[3] = new XfbTask(
                "", "帮我操死张宜弛" + (int)(Math.random()*10), "", "帮干活",
                32.0981720000,118.9021340000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区超市内",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 80);

        taskList[4] = new XfbTask(
                "", "帮忙去北区复印店拿圆通快递。" + (int)(Math.random()*10), "", "拿快递",
                32.1155480000,118.9310590000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,16,30), new Date(2016,8,20,21,30), 90);

        taskList[5] = new XfbTask(
                "", "帮忙去北区复印店拿韵达快递。" + (int)(Math.random()*10), "", "其他",
                32.1178020000,118.9085710000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,9,30), new Date(2016,8,20,21,30), 120);

        taskList[6] = new XfbTask(
                "", "帮忙去北区复印店拿圆通快递。" + (int)(Math.random()*10), "", "拿快递",
                32.1216550000,118.9331190000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,16,30), new Date(2016,8,20,21,30), 90);

        taskList[7] = new XfbTask(
                "", "帮忙去北区复印店拿韵达快递。" + (int)(Math.random()*10), "", "其他",
                32.1012800000,118.9462510000, 234.45, 345.45, 23.45,
                "南京市栖霞区文苑路1号南京师范大学", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学", "老北区31栋女生宿舍楼",
                new Date(2016,8,20,9,30), new Date(2016,8,20,21,30), 120);

        //延迟一定时间，模拟真实情况,3~8秒
        SystemClock.sleep(((int)(Math.random()*6) + 1) * 1000);

        return taskList;
    }

    //// TODO: 2016-8-21 这边没处理好，能用，不优雅
    //根据task_type字段获得对应的图片资源
    public static HashMap<String, Integer> imageList(){
        HashMap<String, Integer> imageList = new HashMap<String, Integer>();
        imageList.put("拿快递", R.mipmap.task_type_kuaidi);
        imageList.put("帮干活", R.mipmap.task_type_ganhuo);
        imageList.put("其他", R.mipmap.task_type_qita);

        return imageList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public double getSenderLat() {
        return senderLat;
    }

    public void setSenderLat(double senderLat) {
        this.senderLat = senderLat;
    }

    public double getSenderLng() {
        return senderLng;
    }

    public void setSenderLng(double senderLng) {
        this.senderLng = senderLng;
    }

    public double getHappenLat() {
        return happenLat;
    }

    public void setHappenLat(double happenLat) {
        this.happenLat = happenLat;
    }

    public double getHappenLng() {
        return happenLng;
    }

    public void setHappenLng(double happenLng) {
        this.happenLng = happenLng;
    }

    public double getDur() {
        return dur;
    }

    public void setDur(double dur) {
        this.dur = dur;
    }

    public String getSenderLocation() {
        return senderLocation;
    }

    public void setSenderLocation(String senderLocation) {
        this.senderLocation = senderLocation;
    }

    public String getSenderLocationDescription() {
        return senderLocationDescription;
    }

    public void setSenderLocationDescription(String senderLocationDescription) {
        this.senderLocationDescription = senderLocationDescription;
    }

    public String getHappenLocation() {
        return happenLocation;
    }

    public void setHappenLocation(String happenLocation) {
        this.happenLocation = happenLocation;
    }

    public String getHappenLocationDescription() {
        return happenLocationDescription;
    }

    public void setHappenLocationDescription(String happenLocationDescription) {
        this.happenLocationDescription = happenLocationDescription;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getRewardPoint() {
        return rewardPoint;
    }

    public void setRewardPoint(int rewardPoint) {
        this.rewardPoint = rewardPoint;
    }

    public String[] getImagePaths() {
        return imagePaths;
    }

    public void setImagePaths(String[] imagePaths) {
        this.imagePaths = imagePaths;
    }

    public Bitmap[] getLowQualityBitmaps() {
        return lowQualityBitmaps;
    }

    public void setLowQualityBitmaps(Bitmap[] lowQualityBitmaps) {
        this.lowQualityBitmaps = lowQualityBitmaps;
    }

    public Bitmap[] getHighQualityBitmaps() {
        return highQualityBitmaps;
    }

    public void setHighQualityBitmaps(Bitmap[] highQualityBitmaps) {
        this.highQualityBitmaps = highQualityBitmaps;
    }
}
