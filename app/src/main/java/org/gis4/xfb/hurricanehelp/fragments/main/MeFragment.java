package org.gis4.xfb.hurricanehelp.fragments.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MeFragment extends BaseFragment
{
    public static MeFragment instance()
    {
        return new MeFragment();
    }

    @BindView(R.id.editText_login_userName)
    EditText loginUserName;
    @BindView(R.id.editText_login_userPassword)
    EditText loginUserPass;

    @OnClick(R.id.button_i_need_register)
    public void iNeedRegClick(View v)
    {
        baseF.showRegisterActivity();
    }
    @OnClick(R.id.button_login)
    public void loginClick(View v)
    {
        if(getEditTextUsername().isEmpty()||getEditTextPassword().isEmpty())
        {
            baseF.showError("请填写完整登陆信息！");
            return;
        }
        baseF.progressDialogShow();
        // 下面是LeanCloud的登陆过程
        AVUser.logInInBackground(
                getEditTextUsername(), getEditTextPassword(),
                new LogInCallback<AVUser>()
                {
                    @Override
                    public void done(AVUser avUser, AVException e)
                    {
                        baseF.progressDialogDismiss();
                        if (avUser != null) {
                            baseF.UpdateUser();
                            Toast.makeText(baseF.getContext(),"登陆成功！",Toast.LENGTH_SHORT).show();
                            //TODO: 更新一下我的界面为已经登陆
                        } else {
                            //TODO: 根据e判断是用户名密码还是其它问题
                            showError("登陆失败，请确认用户名密码，或者网络情况");
                        }
                    }
                }
        );
    }

    private String getEditTextUsername() {return loginUserName.getText().toString();}
    private String getEditTextPassword() {return loginUserPass.getText().toString();}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view;
        if (super.getUserId().isEmpty())
        {
            view = inflater.inflate(R.layout.fragment_me_reg, container, false);
        } else
        {
            view = inflater.inflate(R.layout.fragment_me_norm, container, false);
        }

        ButterKnife.bind(this, view);
        return view;
    }


}
