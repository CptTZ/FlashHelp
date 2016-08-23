package org.gis4.xfb.hurricanehelp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.feedback.FeedbackAgent;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.location.AmapLocationSource;

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

    private boolean hasRunnedS, hasRunnedE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        hasRunnedS = false;
        hasRunnedE = false;

        FeedbackAgent agent = new FeedbackAgent(this);
        agent.sync();

        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.splash_fadein);
        Animation.AnimationListener animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (hasRunnedS) return;
                AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback()
                {
                    @Override
                    public void done(AVException e)
                    {
                        if (e == null) {
                            String installationId = AVInstallation.getCurrentInstallation().getInstallationId();
                            // 关联  installationId 到用户表等操作……
                        }
                        else {
                            Log.e("LeanCloud", "错误," + e.getLocalizedMessage());
                        }
                    }
                });
                hasRunnedS = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (hasRunnedE) return;
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.overridePendingTransition(R.anim.splash_slidein,
                        R.anim.splash_slideout);
                hasRunnedE = true;
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
