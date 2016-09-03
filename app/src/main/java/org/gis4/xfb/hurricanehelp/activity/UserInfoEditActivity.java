package org.gis4.xfb.hurricanehelp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.rey.material.widget.CheckBox;
import com.rey.material.widget.EditText;
import com.yongchun.library.view.ImageSelectorActivity;

import org.gis4.xfb.hurricanehelp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserInfoEditActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.checkbox_boy)
    CheckBox checkBoxBoy;

    @BindView(R.id.checkbox_girl)
    CheckBox checkBoxGirl;

    @BindView(R.id.avatar)
    ImageView avatar;

    @BindView(R.id.editText_introduce)
    MaterialEditText editTextIntroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.activity_edit_user_info_menu);
        editTextIntroduce.setText(AVUser.getCurrentUser().getString("sign"));

        initialMenu(toolbar.getMenu());
        if(AVUser.getCurrentUser().getInt("sex")==0) checkBoxBoy.setChecked(true);
        else checkBoxGirl.setChecked(true);

        checkBoxBoy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) checkBoxGirl.setChecked(false);
            }
        });

        checkBoxGirl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) checkBoxBoy.setChecked(false);
            }
        });

        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxSelectNum = 1;
                int mode = ImageSelectorActivity.MODE_SINGLE;
                boolean isShow = false;
                boolean isPreview = false;
                boolean isCrop = true;

                ImageSelectorActivity.start(UserInfoEditActivity.this, maxSelectNum, mode, isShow,isPreview,isCrop);
            }
        });
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
                switch (item.getItemId()) {
                    case R.id.action_save:

                        int sex = checkBoxBoy.isChecked() ? 0 : 1;

                        String introduce = editTextIntroduce.getText().toString();
                        avatar.setDrawingCacheEnabled(true);

                        //头像图片
                        Bitmap avatarPic = avatar.getDrawingCache();
                        avatar.setDrawingCacheEnabled(false);

                        AVUser.getCurrentUser().put("sign", introduce);
                        AVUser.getCurrentUser().put("sex", sex);
                        AVUser.getCurrentUser().saveInBackground( new SaveCallback() {
                            @Override
                            public void done(AVException e)
                            {
                                if(e==null) {
                                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    AVAnalytics.onEvent(getApplicationContext(), e.getMessage(), "Xfb_Cloud");
                                    LogUtil.log.e("Xfb", e.getMessage(),e);
                                    Toast.makeText(getApplicationContext(), "保存失败请重试", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } );
                        break;
                    default:
                        return false;
                }

                return true;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE) {
            ArrayList<String> imagePaths = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);

            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 4;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePaths.get(0), opts);

            avatar.setImageBitmap(bitmap);
        }
    }
}
