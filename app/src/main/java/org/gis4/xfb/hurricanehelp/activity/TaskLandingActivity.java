package org.gis4.xfb.hurricanehelp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;

import org.gis4.xfb.hurricanehelp.R;

public class TaskLandingActivity extends SlidingUpBaseActivity<ObservableScrollView> implements ObservableScrollViewCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_landing);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_task_landing;
    }

    @Override
    protected ObservableScrollView createScrollable() {
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scroll);
        scrollView.setScrollViewCallbacks(this);
        return scrollView;
    }
}
