package org.gis4.xfb.hurricanehelp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.activity.BaseActivity;
import org.gis4.xfb.hurricanehelp.activity.MainActivity;
import org.gis4.xfb.hurricanehelp.activity.RegistActivity;
import org.gis4.xfb.hurricanehelp.lbs.location.AmapLocationSource;

/**
 * 基本Fragment，用于同步登录状态和位置
 */
public class BaseFragment extends Fragment
{
    protected BaseFragment baseF;

    /**
     * 定位信息
     */
    protected AmapLocationSource locationSource;
    private AVUser currentUser;

    public String getUserId()
    {
        if (this.currentUser != null)
            return currentUser.getObjectId();
        return "";
    }

    public boolean hasLogined() {return this.currentUser != null;}

    private ProgressDialog prgd;
    /**
     * 显示请等待窗口
     */
    public void progressDialogShow()
    {
        prgd=ProgressDialog
                .show(this.baseF.getContext(),
                        "提示","请等待", true, false);
    }

    public void progressDialogDismiss()
    {
        if(prgd==null) return;
        prgd.dismiss();
    }

    /**
     * 显示错误窗口
     * @param msg 消息内容
     */
    public void showError(String msg)
    {
        showError(msg, this.baseF.getContext());
    }

    public void showError(String msg, Context c)
    {
        new AlertDialog.Builder(c)
                .setTitle("错误")
                .setMessage(msg)
                .setNegativeButton(android.R.string.ok,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i)
                            {
                                dialogInterface.dismiss();
                            }
                        }).show();
    }

    /**
     * 更新用户信息，登陆以后重新调用一次
     */
    public void UpdateUser() {this.currentUser=AVUser.getCurrentUser();}

    private EditText uName,uPass;
    /**
     * 初始化登陆界面
     * @return View
     */
    public View initLoginUi(final Fragment f, LayoutInflater inflater, final ViewGroup container,
                            final int viewForLogin)
    {
        final View view = inflater.inflate(viewForLogin, container, false);
        if (getUserId().isEmpty()) {
            uName = (EditText) view.findViewById(R.id.editText_login_userName);
            uPass = (EditText) view.findViewById(R.id.editText_login_userPassword);
            view.findViewById(R.id.button_i_need_register).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            baseF.showRegisterActivity();
                        }
                    }
            );
            view.findViewById(R.id.button_login).setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (uName == null | uPass == null) return;
                            if (getUserName().isEmpty() | getUserPass().isEmpty()) {
                                showError("请填写完整登陆信息！");
                                return;
                            }
                            progressDialogShow();
                            AVUser.logInInBackground(getUserName(), getUserPass(),
                                    new LogInCallback<AVUser>() {
                                        @Override
                                        public void done(AVUser avUser, AVException e) {
                                            baseF.progressDialogDismiss();
                                            if (avUser != null) {
                                                baseF.UpdateUser();
                                                Toast.makeText(baseF.getContext(), "登陆成功！", Toast.LENGTH_SHORT).show();
                                                hideLogInWindow();
                                            } else {
                                                String errDef;
                                                switch (e.getCode())
                                                {
                                                    case AVException.USER_DOESNOT_EXIST:
                                                        errDef="该用户不存在";
                                                        break;
                                                    case AVException.USERNAME_PASSWORD_MISMATCH:
                                                        errDef="用户密码输入错误";
                                                        break;
                                                    default:
                                                        errDef=e.getLocalizedMessage();
                                                        Log.e("Xfb_Cloud", e.getMessage(), e);
                                                        AVAnalytics.onEvent(getContext(),e.getMessage(),"Xfb_Cloud");
                                                }
                                                showError(errDef);
                                            }
                                        }
                                    }
                            );
                        }
                    }
            );
        }
        else {
            LinearLayout linearLayoutReg = (LinearLayout) view.findViewById(R.id.linearLayout_reg);
            linearLayoutReg.setVisibility(View.GONE);
        }

        return view;
    }

    private String getUserName() {return uName.getText().toString();}
    private String getUserPass() {return uPass.getText().toString();}

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        this.baseF = this;
        this.currentUser = AVUser.getCurrentUser();
        this.locationSource = new AmapLocationSource(this.getContext());
    }

    @Override
    public void onPause()
    {
        super.onPause();
        locationSource.stopLocation();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        locationSource.startLocation();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        locationSource.deactivate();
    }

    /**
     * 显示注册界面
     */
    public void showRegisterActivity()
    {
        startActivity(new Intent(this.baseF.getContext(), RegistActivity.class));
        this.baseF.getActivity().overridePendingTransition(R.anim.splash_slidein,
                R.anim.splash_slideout);
    }

    /**
     * 隐藏三个fragment里面的登陆窗口
     */
    private void hideLogInWindow() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.mSecondFragment.getView().findViewById(R.id.linearLayout_reg).setVisibility(View.GONE);
        mainActivity.mThirdFragment.getView().findViewById(R.id.linearLayout_reg).setVisibility(View.GONE);
        mainActivity.mFourthFragment.getView().findViewById(R.id.linearLayout_reg).setVisibility(View.GONE);
    }

}
