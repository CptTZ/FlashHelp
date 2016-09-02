package org.gis4.xfb.hurricanehelp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;

import org.gis4.xfb.hurricanehelp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

//TODO: Splash_Image从网络上加载
public class SplashActivity extends Activity {
    @BindView(R.id.AppName_CHN)
    TextView textAppName;
    @BindView(R.id.AppName_ENG)
    TextView textAppNameEng;
    @BindView(R.id.BriefIntro)
    TextView briefIntro;

    private boolean hasRunnedStart, hasRunnedEnd;

    /**
     * 初始化Push模块
     */
    private void initPush()
    {
        PushService.setDefaultPushCallback(this, SplashActivity.class);
        PushService.subscribe(this, "publicEvent", SplashActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        hasRunnedStart = false;
        hasRunnedEnd = false;

        // 保存installId
        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    if(AVUser.getCurrentUser() != null) {
                        AVInstallation.getCurrentInstallation().put("user", AVUser.getCurrentUser());
                    }
                }
                else {
                    AVAnalytics.onEvent(getApplicationContext(), e.getMessage(), "Xfb_Cloud_Init");
                }
            }
        });

        initPush();
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fadein);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (hasRunnedStart) return;
                hasRunnedStart = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (hasRunnedEnd) return;
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.overridePendingTransition(R.anim.splash_slidein,
                        R.anim.splash_slideout);
                hasRunnedEnd = true;
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        };

        fadeIn.setAnimationListener(animationListener);
        textAppName.startAnimation(fadeIn);
        textAppNameEng.startAnimation(fadeIn);
        briefIntro.startAnimation(fadeIn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Splash不给主动退出
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.runFinalization();
    }

}
