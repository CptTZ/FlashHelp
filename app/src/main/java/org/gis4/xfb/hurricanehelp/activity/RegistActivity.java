package org.gis4.xfb.hurricanehelp.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.sns.SNS;
import com.avos.sns.SNSBase;
import com.avos.sns.SNSCallback;
import com.avos.sns.SNSException;
import com.avos.sns.SNSType;

import org.gis4.xfb.hurricanehelp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistActivity extends BaseActivity
{
    /*
    protected SNSCallback myCallback;
    private SNSType loginType;

    @OnClick(R.id.button_reg_with_weibo)
    public void RegWB(View v)
    {
        try
        {
            SNS.setupPlatform(SNSType.AVOSCloudSNSSinaWeibo, "https://leancloud.cn/1.1/sns/goto/4upfku4dx2h57ib0");
        } catch (AVException e)
        {
            e.printStackTrace();
            return;
        }
        loginType = SNSType.AVOSCloudSNSSinaWeibo;
        SNS.loginWithCallback(this, SNSType.AVOSCloudSNSSinaWeibo, myCallback);
    }

    @OnClick(R.id.button_reg_with_qq)
    public void RegQQ(View v)
    {
        try
        {
            SNS.setupPlatform(this, SNSType.AVOSCloudSNSQQ, "1105407778", "",
                    "https://leancloud.cn/1.1/sns/goto/158n3i1bngnrhmj3");
        } catch (AVException e)
        {
            e.printStackTrace();
            return;
        }
        loginType = SNSType.AVOSCloudSNSQQ;
        SNS.loginWithCallback(this, SNSType.AVOSCloudSNSQQ, myCallback);
    }*/

    @OnClick(R.id.proceed_to_reg)
    public void RegNorm(View v)
    {
        if (userPassword.getText().toString()
                .equals(userPasswordAgain.getText().toString()))
        {
            if (!userPassword.getText().toString().isEmpty())
            {
                if (!userName.getText().toString().isEmpty())
                {
                    if (!userEmail.getText().toString().isEmpty())
                    {
                        progressDialogShow();
                        register();
                    } else
                    {
                        showError(getString(R.string.error_register_email_address_null));
                    }
                } else
                {
                    showError(getString(R.string.error_register_user_name_null));
                }
            } else
            {
                showError(getString(R.string.error_register_password_null));
            }
        } else
        {
            showError(getString(R.string.error_register_password_not_equals));
        }
    }

    @OnClick(R.id.login_from_reg)
    public void onClickLoginFromReg(View v)
    {
        this.finish();
    }

    @BindView(R.id.editText_register_email)
    EditText userEmail;
    @BindView(R.id.editText_register_userName)
    EditText userName;
    @BindView(R.id.editText_register_userPassword)
    EditText userPassword;
    @BindView(R.id.editText_register_userPassword_again)
    EditText userPasswordAgain;

    private ProgressDialog progressDialog;
    private RegistActivity regAct;
    private AlertDialog alertDiag;

    public void register()
    {
       SignUpCallback signUpCallback = new SignUpCallback()
       {
           public void done(AVException e)
           {
               progressDialogDismiss();
               if (e == null)
               {
                   showRegisterSuccess();
                   alertDiag.dismiss();
                   regAct.finish();
               } else
               {
                   switch (e.getCode())
                   {
                       case 202:
                           showError(getString(R.string.error_register_user_name_repeat));
                           break;
                       case 203:
                           showError(getString(R.string.error_register_email_repeat));
                           break;
                       default:
                           showError(getString(R.string.network_error));
                           break;
                   }
               }
           }
       };

       AVUser user = new AVUser();
       user.setUsername(userName.getText().toString());
       user.setPassword(userPassword.getText().toString());
       user.setEmail(userEmail.getText().toString());
       user.signUpInBackground(signUpCallback);
    }

    private void showRegisterSuccess()
    {
        alertDiag = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_message_title))
                .setMessage(getString(R.string.success_register_success))
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog,
                                                int which)
                            {
                                dialog.dismiss();
                            }
                        }).show();
  }

  private void progressDialogDismiss()
  {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    private void progressDialogShow()
    {
        progressDialog = ProgressDialog
                .show(this,
                        getResources().getText(
                                R.string.dialog_message_title),
                        getResources().getText(
                                R.string.dialog_text_wait), true, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        regAct = this;

        /*myCallback = new SNSCallback()
        {
            @Override
            public void done(SNSBase object, SNSException e)
            {
                if (e == null)
                {
                    Toast.makeText(RegistActivity.this, "Login OK", Toast.LENGTH_SHORT).show();
                }
            }
        };*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (loginType == null) return;
//        SNS.onActivityResult(requestCode, resultCode, data, loginType);
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
