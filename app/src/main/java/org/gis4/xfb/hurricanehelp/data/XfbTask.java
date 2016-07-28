package org.gis4.xfb.hurricanehelp.data;


import java.util.Date;

/**
 * 旋风帮，基础任务
 */
public class XfbTask
{
    private String title, desc, senderId;
    private double senderLat, senderLng, happenLat, happenLng, dur;
    private Date takePlaceTime, sendTime;

    public double getHappenLat()
    {
        return happenLat;
    }

    public void setHappenLat(double happenLat)
    {
        this.happenLat = happenLat;
    }

    public double getHappenLng()
    {
        return happenLng;
    }

    public void setHappenLng(double happenLng)
    {
        this.happenLng = happenLng;
    }

    public double getSenderLat()

    {
        return senderLat;
    }

    public double getSenderLng()
    {
        return senderLng;
    }

    public void setTaskDetailInfo(String desc)
    {
        this.desc = desc;
    }

    public void setTaskTakePlaceTime(Date takePlaceTime)
    {
        this.takePlaceTime = takePlaceTime;
    }

    public void setTaskDurance(double dur)
    {
        this.dur = dur;
    }

    public String getTaskDetailInfo()
    {
        return desc;
    }

    public double getTaskDurance()
    {
        return dur;
    }

    public Date getTaskTakePlaceTime()
    {
        return takePlaceTime;
    }

    public XfbTask(String title, String sender, double senderLat, double senderLng)
    {
        this.senderLat = senderLat;
        this.senderLng = senderLng;
        this.title = title;
        this.senderId = sender;
        this.sendTime = new Date(System.currentTimeMillis());
    }

}
