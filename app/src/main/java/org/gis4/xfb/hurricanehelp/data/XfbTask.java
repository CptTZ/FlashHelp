package org.gis4.xfb.hurricanehelp.data;


import android.graphics.Bitmap;

import java.util.Date;

/**
 * 旋风帮，基础任务
 */
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

    public XfbTask() {
    }

    public XfbTask(String title, String sender, double senderLat, double senderLng) {
//        this.senderLat = senderLat;
//        this.senderLng = senderLng;
//        this.title = title;
//        this.senderId = sender;
//        this.sendTime = new Date(System.currentTimeMillis());
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
