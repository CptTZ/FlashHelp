package org.gis4.xfb.hurricanehelp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;

import butterknife.BindView;
import butterknife.ButterKnife;

//TODO: Splash_Image从网络上加载
public class SplashActivity extends Activity
{
    @BindView(R.id.AppName_CHN)
    TextView textAppName;
    @BindView(R.id.AppName_ENG)
    TextView textAppNameEng;
    @BindView(R.id.BriefIntro)
    TextView briefIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fadein);
        Animation.AnimationListener animationListener = new Animation.AnimationListener()
        {
            @Override
            public void onAnimationStart(Animation animation)
            {
                //TODO: 初始化需要加载啥
            }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.overridePendingTransition(R.anim.splash_slidein,
                        R.anim.splash_slideout);
                SplashActivity.this.finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation)
            {
            }
        };

        fadeIn.setAnimationListener(animationListener);
        textAppName.startAnimation(fadeIn);
        textAppNameEng.startAnimation(fadeIn);
        briefIntro.startAnimation(fadeIn);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        // Splash不给主动退出
        return keyCode == KeyEvent.KEYCODE_BACK || super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        System.runFinalization();
    }

}
