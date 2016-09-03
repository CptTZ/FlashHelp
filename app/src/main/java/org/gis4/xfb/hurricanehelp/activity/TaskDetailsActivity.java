package org.gis4.xfb.hurricanehelp.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.Dbconnect;
import org.gis4.xfb.hurricanehelp.data.XfbTask;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.task_location_imageview)
    ImageView taskLocationImageview;

    @BindView(R.id.task_type)
    TextView taskType;

    @BindView(R.id.task_sender)
    TextView taskSender;

    @BindView(R.id.textview_time_start)
    TextView textviewTimeStart;

    @BindView(R.id.textview_time_end)
    TextView textviewTimeEnd;

    @BindView(R.id.textview_my_points)
    TextView textviewMyPoints;

    @BindView(R.id.task_desc)
    TextView taskDesc;

    //保存数据
    private XfbTask xfbTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);
        ButterKnife.bind(this);

        Bundle bundle = this.getIntent().getExtras();
        xfbTask = bundle.getParcelable("xfbTask");

        if(xfbTask.getTaskState() == 1) {
            toolbar.inflateMenu(R.menu.activity_task_details_menu);
            initialMenu(toolbar.getMenu());
        }

        taskType.setText(xfbTask.getTaskType());
        Dbconnect.UpdateSenderIdToUserName(xfbTask.getSenderId(), taskSender);

        textviewTimeStart.setText(getDateString(xfbTask.getStartTime()));
        textviewTimeEnd.setText(getDateString(xfbTask.getEndTime()));
        taskDesc.setText(xfbTask.getDesc());

        taskLocationImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(TaskDetailsActivity.this, TaskDetailsMapActivity.class);
                Bundle bundle=new Bundle();
                bundle.putParcelable("xfbTask", xfbTask);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initialMenu(Menu menu) {
        toolbar.setNavigationIcon(R.mipmap.ic_back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_ok:
                        try {
                            String oriSender = xfbTask.getSenderId();
                            AVGeoPoint oriLoc = xfbTask.getHappenGeoLocation();
                            XfbTask xfb = AVObject.createWithoutData(XfbTask.class, xfbTask.getObjectId());
                            // 实例化的时候会修改这些，有Bug先这样凑合
                            xfb.put(XfbTask.SENDERID, oriSender);
                            xfb.setHappenGeoLocation(oriLoc);
                            xfb.setTaskstate(XfbTask.State_Finished);
                            xfb.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(AVException e) {
                                    if(e==null) {
                                        Toast.makeText(getApplicationContext(), "订单已完成", Toast.LENGTH_SHORT).show();
                                        finish();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "网络问题，无法完成订单，请重试", Toast.LENGTH_SHORT).show();
                                        LogUtil.log.e("Xfb", e.getMessage(), e);
                                    }
                                }
                            });
                        } catch (AVException e) {
                            AVAnalytics.onEvent(getApplicationContext(), e.getMessage(), "Xfb_Cloud_TaskAlter");
                            Toast.makeText(getApplicationContext(), "系统错误，无法完成订单，请重试", Toast.LENGTH_SHORT).show();
                            return true;
                        }
                        break;
                    default:
                        return false;
                }
                return true;
            }
        });
    }

    private String getDateString(Date date) {
        return date.getYear() + "年" + date.getMonth() + "月" + date.getDay() + "日" + date.getHours() + "点" + date.getMinutes() + "分";
    }
}
