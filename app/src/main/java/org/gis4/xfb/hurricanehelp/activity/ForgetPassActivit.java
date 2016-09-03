package org.gis4.xfb.hurricanehelp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;

import org.gis4.xfb.hurricanehelp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPassActivit extends BaseActivity
{
    private ForgetPassActivit fpAc;
    @BindView(R.id.editText_forget_password_email)
    EditText emailText;

    @OnClick(R.id.button_find_password)
    public void onClickFindPass(View v)
    {
        if(AVUser.getCurrentUser() != null) showError("你已成功登陆，无需再次登陆！");
        String email = emailText.getText().toString();
        if(email==null) {
            showError("邮件地址必填！");
            return;
        }
        RequestPasswordResetCallback resetPass = new RequestPasswordResetCallback()
        {
            @Override
            public void done(AVException e)
            {
                if(e == null)
                {
                    Toast.makeText(fpAc,"已发送邮件，请按照邮件内容操作",Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    showError("发送错误，请确认地址是否正确");
                    AVAnalytics.onEvent(fpAc, e.getMessage(), "Xfb_Cloud");
                }
            }
        };
        AVUser.requestPasswordResetInBackground(email, resetPass);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.fpAc=this;
        setContentView(R.layout.activity_forget_pass);
        ButterKnife.bind(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        AVAnalytics.onPause(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        AVAnalytics.onResume(this);
    }
}
