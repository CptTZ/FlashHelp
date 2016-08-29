package org.gis4.xfb.hurricanehelp.data;

import android.os.SystemClock;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Mod by zyc on 2016/8/29
 * Created by zc on 2016/8/19.
 */
public class testXfbTask
{
    /**
     * 保存数据到服务器
     * @deprecated 已经存过了，不要再用
     * @param sample 测试数据
     */
    public static void saveSampleTask(XfbTask[] sample)
    {
        for (XfbTask sp:sample)
        {
            try
            {
                sp.save();
            } catch (AVException e)
            {
                LogUtil.log.e("XFB_CLOUD",e.getMessage());
            }
        }
    }

    /**
     * 测试数据
     * @deprecated I'm angry! Naive!
     * @return 乱七八糟的测试数据
     */
    public static XfbTask[] taskSample(){
        XfbTask[] taskList = new XfbTask[50];

        //desc字段后加上一个随机数，以此判断是否真刷新了
        taskList[0] = new XfbTask(
                "", "帮忙去北区复印店拿顺丰快递。" + (int)(Math.random()*10), "", "拿快递",
                32.1049720000, 118.9142240000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 50);

        taskList[1] = new XfbTask(
                "", "东区校园促销活动需要帮手，在线等，急" + (int)(Math.random()*10), "", "帮干活",
                32.1072240000,118.9301580000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 60);

        taskList[2] = new XfbTask(
                "", "帮忙去正门拿快递，再寄给指定地点" + (int)(Math.random()*10), "", "拿快递",
                32.1000990000,118.9189140000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 70);

        taskList[3] = new XfbTask(
                "", "帮我操死张宜弛" + (int)(Math.random()*10), "", "帮干活",
                32.0981720000,118.9021340000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区超市内",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,17,30), new Date(2016,8,20,21,30), 80);

        taskList[4] = new XfbTask(
                "", "帮忙去北区复印店拿圆通快递。" + (int)(Math.random()*10), "", "拿快递",
                32.1155480000,118.9310590000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,16,30), new Date(2016,8,20,21,30), 90);

        taskList[5] = new XfbTask(
                "", "帮忙去北区复印店拿韵达快递。" + (int)(Math.random()*10), "", "其他",
                32.1178020000,118.9085710000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,9,30), new Date(2016,8,20,21,30), 120);

        taskList[6] = new XfbTask(
                "", "帮忙去北区复印店拿圆通快递。" + (int)(Math.random()*10), "", "拿快递",
                32.1216550000,118.9331190000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学老北区", "老北区34栋男生宿舍楼",
                new Date(2016,8,20,16,30), new Date(2016,8,20,21,30), 90);

        taskList[7] = new XfbTask(
                "", "帮忙去北区复印店拿韵达快递。" + (int)(Math.random()*10), "", "其他",
                32.1012800000,118.9462510000, 32.106385, 118.913559, 23.45,
                "南京市栖霞区文苑路1号南京师范大学", "老北区复印店，水果店旁边那个",
                "南京市栖霞区文苑路1号南京师范大学", "老北区31栋女生宿舍楼",
                new Date(2016,8,20,9,30), new Date(2016,8,20,21,30), 120);

        for(int n = 8; n < 50; n++) {
            taskList[n] = taskList[0];
        }

        //延迟一定时间，模拟真实情况,0.3~0.8秒
        SystemClock.sleep(((int)(Math.random()*6) + 1) * 100);

        return taskList;
    }
}
