package org.gis4.xfb.hurricanehelp.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chi on 2016-8-19.
 * 暂时模拟一个获取后台数据的类在这儿，方便我调试
 */
public class Dbconnect {

    //TODO: 晚上回来弄获取数据的
    //接单的经纬度坐标
    public static XfbTask[] getTaskList(double lat, double lng){
        return testXfbTask.taskSample();
    }
}
