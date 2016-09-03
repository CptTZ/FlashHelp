package org.gis4.xfb.hurricanehelp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.XfbTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TaskDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

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
                        //// TODO: 2016-09-03 在这里确认完成
                        break;
                }
                return false;
            }
        });
    }
}
