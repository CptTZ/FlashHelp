package org.gis4.xfb.hurricanehelp.fragments.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private EditText loginUserName;
    private EditText loginUserPass;
    private Button buttonLogin;
    private Button buttonRegister;

    private String getEditTextUsername() {return loginUserName.getText().toString();}
    private String getEditTextPassword() {return loginUserPass.getText().toString();}

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        if (super.getUserId().isEmpty()) {
            view = inflater.inflate(R.layout.fragment_me_reg, container, false);

            loginUserName = (EditText) view.findViewById(R.id.editText_login_userName);
            loginUserPass = (EditText) view.findViewById(R.id.editText_login_userPassword);

            buttonLogin = (Button) view.findViewById(R.id.button_login);
            buttonRegister = (Button) view.findViewById(R.id.button_i_need_register);

            buttonLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getEditTextUsername().isEmpty()||getEditTextPassword().isEmpty()) {
                        baseF.showError("请填写完整登陆信息！");
                        return;
                    }
                    baseF.progressDialogShow();
                    // 下面是LeanCloud的登陆过程
                    AVUser.logInInBackground(
                            getEditTextUsername(), getEditTextPassword(),
                            new LogInCallback<AVUser>() {
                                @Override
                                public void done(AVUser avUser, AVException e) {
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
            });

            buttonRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    baseF.showRegisterActivity();
                }
            });
        }
        else {
            view = inflater.inflate(R.layout.fragment_me_norm, container, false);
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        //TODO!!: 这样的Hack估计以后会遇到问题
        if (super.getUserId().isEmpty())
        {

        }
    }
}
