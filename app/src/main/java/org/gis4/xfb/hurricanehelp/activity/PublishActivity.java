package org.gis4.xfb.hurricanehelp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.LogUtil;
import com.avos.avoscloud.SaveCallback;
import com.rey.material.widget.Button;
import com.rey.material.widget.Slider;
import com.rey.material.widget.Spinner;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.yongchun.library.view.ImageSelectorActivity;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.XfbTask;
import org.gis4.xfb.hurricanehelp.data.pickedDate;
import org.gis4.xfb.hurricanehelp.widget.SublimePickerFragment;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 发布新的XFB任务
 * @author zc
 */
public class PublishActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.task_type)
    Spinner spinner;

    @BindView(R.id.textview_time_start)
    TextView textViewTimeStart;

    @BindView(R.id.textview_time_end)
    TextView textViewTimeEnd;

    @BindView(R.id.textview_points)
    TextView textviewPoints;

    @BindView(R.id.textview_my_points)
    TextView textviewMyPoints;

    @BindView(R.id.slider_points)
    Slider sliderPoints;

    @BindView(R.id.task_send_location_imageview)
    ImageView taskSendLocationImageview;

    @BindView(R.id.task_execute_location_imageview)
    ImageView taskExecuteLocationImageview;

    @BindView(R.id.task_desc)
    EditText taskDesc;

    @BindView(R.id.edittext_send_location_discription)
    EditText edittextSend;

    @BindView(R.id.edittext_execute_location_discription)
    EditText edittextExecute;

    @BindView(R.id.select_or_clear_images)
    Button selectOrClearImages;


    private ImageView[] imageViewsArray;
    private boolean hasSelectImage;
    private boolean textViewTimeStartSelected, textViewTimeEndSelected;
    private pickedDate startDate, endDate;

    //保存数据
    private XfbTask xfbTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);
        ButterKnife.bind(this);

        toolbar.inflateMenu(R.menu.activity_publish_menu);
        xfbTask = new XfbTask();

        selectOrClearImages =(Button) findViewById(R.id.select_or_clear_images);
        hasSelectImage = false;
        imageViewsArray = new ImageView[5];
        imageViewsArray[0] =(ImageView) findViewById(R.id.publish_imageview1);
        imageViewsArray[1] =(ImageView) findViewById(R.id.publish_imageview2);
        imageViewsArray[2] =(ImageView) findViewById(R.id.publish_imageview3);
        imageViewsArray[3] =(ImageView) findViewById(R.id.publish_imageview4);
        imageViewsArray[4] =(ImageView) findViewById(R.id.publish_imageview5);

        initialMenu(toolbar.getMenu());
        initialSpinner();
        initialActivity();
        initialLocationSelect();
        initialUserInfo();
        initialImageSelect();
    }

    private void initialSpinner() {
        String[] taskSype = getResources().getStringArray(R.array.task_sype);

        ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,R.layout.row_spn, taskSype);
        adapter.setDropDownViewResource(R.layout.row_spn_dropdown);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(Spinner parent, View view, int position, long id) {
                xfbTask.setTaskType((String) spinner.getSelectedItem());
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
                switch (item.getItemId()){
                    case R.id.action_save:
                        xfbTask.setDesc(taskDesc.getText().toString());
                        xfbTask.setSenderlocationManualDesc(edittextSend.getText().toString());
                        xfbTask.setHappenLocationManualDesc(edittextExecute.getText().toString());

                        if(xfbTask.getSenderLat() == 0) {
                            Toast.makeText(PublishActivity.this, "请选择任务送达地点！", Toast.LENGTH_SHORT).show();
                        }
                        else if(xfbTask.getHappenLat() == 0) {
                            Toast.makeText(PublishActivity.this, "请选择任务执行地点！", Toast.LENGTH_SHORT).show();
                        }
                        else if(xfbTask.getStartTime() == null) {
                            Toast.makeText(PublishActivity.this, "请填写任务开始时间！", Toast.LENGTH_SHORT).show();
                        }
                        else if(xfbTask.getEndTime() == null) {
                            Toast.makeText(PublishActivity.this, "请填写任务截至时间！", Toast.LENGTH_SHORT).show();
                        }
                        else if(xfbTask.getDesc() == null || xfbTask.getDesc().equals("")) {
                            Toast.makeText(PublishActivity.this, "请填写任务描述！", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            xfbTask.saveInBackground(new SaveCallback()
                            {
                                @Override
                                public void done(AVException e)
                                {
                                    if(e == null)
                                    {
                                        Toast.makeText(PublishActivity.this, "提交成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                        return;
                                    }
                                    LogUtil.log.e("Xfb_Cloud", e.getMessage(), e);
                                    AVAnalytics.onEvent(getApplicationContext(), e.getMessage(), "Xfb_Cloud");
                                    Toast.makeText(PublishActivity.this, "提交失败,请检查网络连接", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        break;
                    case R.id.action_clear:
                        Toast.makeText(PublishActivity.this, "您已重置,尚未提交", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });

        for(int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);
            SpannableString spanString = new SpannableString(menu.getItem(i).getTitle().toString());
            spanString.setSpan(new ForegroundColorSpan(Color.WHITE), 0, spanString.length(), 0); //fix the color to white
            item.setTitle(spanString);
        }
    }

    private void initialActivity(){
        textViewTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                textViewTimeStartSelected = true;
                textViewTimeEndSelected = false;
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });

        textViewTimeEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDate == null){
                    Toast.makeText(PublishActivity.this, "请先选择开始时间", Toast.LENGTH_SHORT).show();
                    return;
                }

                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                textViewTimeStartSelected = false;
                textViewTimeEndSelected = true;
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });
    }

    //初始化地点选择功能
    //TODO: 2016-8-18 记录所选点的位置以及缩放级别，使得第二次及之后打开选点界面时，地图显示在上一次所选的地方
    private void initialLocationSelect(){
        taskSendLocationImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(PublishActivity.this,ChooseLocationActivity.class);
                Bundle bundle=new Bundle();

                bundle.putString("title", "选择任务送达地点");
                intent.putExtras(bundle);

                startActivityForResult(intent, ChooseLocationActivity.REQUEST_SEND_LOCATION);
            }
        });

        taskExecuteLocationImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(PublishActivity.this,ChooseLocationActivity.class);
                Bundle bundle=new Bundle();

                bundle.putString("title", "选择任务执行地点");
                intent.putExtras(bundle);

                startActivityForResult(intent, ChooseLocationActivity.REQUEST_EXECUTE_LOCATION);
            }
        });
    }

    //初始化界面的用户信息，例如用户积分值。
    private void initialUserInfo() {
        int points = 80;

        textviewPoints.setText("（当前可用积分值：" + String.valueOf(points) + "）");
        sliderPoints.setValueRange(0, points, true);
        sliderPoints.setValue(0, true);

        sliderPoints.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                textviewMyPoints.setText(String.valueOf(newValue));
                xfbTask.setRewardPoint(newValue);
            }
        });
    }

    private void initialImageSelect(){
        selectOrClearImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasSelectImage){
                    for (ImageView anImageViewsArray : imageViewsArray)
                    {
                        anImageViewsArray.setImageDrawable(getResources().getDrawable(R.mipmap.publish_activity_noimage));
                    }

                    Toast.makeText(PublishActivity.this, "清空已选", Toast.LENGTH_SHORT).show();
                    selectOrClearImages.setBackgroundDrawable(getResources().getDrawable(R.mipmap.publish_activity_addimage));
                    hasSelectImage = false;

                    xfbTask.setImagePaths(null);
                    xfbTask.setLowQualityBitmaps(null);
                    xfbTask.setHighQualityBitmaps(null);
                }
                else {
                    //设置选择图片的参数
                    int maxSelectNum = imageViewsArray.length;
                    int mode = ImageSelectorActivity.MODE_MULTIPLE;
                    boolean isShow = false;
                    boolean isPreview = true;
                    boolean isCrop = false;

                    ImageSelectorActivity.start(PublishActivity.this, maxSelectNum, mode, isShow,isPreview,isCrop);
                }
            }
        });
    }

    //处理别的页面返回的参数
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == ImageSelectorActivity.REQUEST_IMAGE){
            //更改添加图片的按钮为删除图片按钮
            selectOrClearImages.setBackgroundDrawable(getResources().getDrawable(R.mipmap.publish_activity_clearallselected));
            hasSelectImage = true;

            ArrayList<String> imagePaths = (ArrayList<String>) data.getSerializableExtra(ImageSelectorActivity.REQUEST_OUTPUT);
            Bitmap[] lowQualityBitmaps = new Bitmap[imagePaths.size()];
            Bitmap[] highQualityBitmaps = new Bitmap[imagePaths.size()];

            for(int n =0; n<imagePaths.size(); n++){
                Bitmap bitmap = getLoacalBitmap(imagePaths.get(n));

                //将图片的宽或者高压缩至150像素
                double newWidth = 0, newHeight = 0;
                if(bitmap.getHeight() > bitmap.getWidth()){
                    newWidth = 150;
                    newHeight = bitmap.getHeight() * newWidth / bitmap.getWidth();
                }
                else {
                    newHeight = 150;
                    newWidth = bitmap.getWidth() * newHeight / bitmap.getHeight();
                }
                Bitmap smallBitmap = compressImage(bitmap, newWidth, newHeight);
                imageViewsArray[n].setImageBitmap(smallBitmap);
                lowQualityBitmaps[n] = smallBitmap;
                highQualityBitmaps[n] = bitmap;
            }

            String[] temp = new String[imagePaths.size()];
            xfbTask.setImagePaths(imagePaths.toArray(temp));
            xfbTask.setLowQualityBitmaps(lowQualityBitmaps);
            xfbTask.setHighQualityBitmaps(highQualityBitmaps);
        }

        if(resultCode == RESULT_OK && requestCode == ChooseLocationActivity.REQUEST_SEND_LOCATION){
            double locationLatitude = data.getDoubleExtra("locationLatitude", 0);
            double locationLongitude = data.getDoubleExtra("locationLongitude", 0);
            String locationText = data.getStringExtra("locationText");

            byte[] b = data.getByteArrayExtra("mapPic");
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            taskSendLocationImageview.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

            xfbTask.setSenderLat(locationLatitude);
            xfbTask.setSenderLng(locationLongitude);
            xfbTask.setSenderLocationAutoDesc(locationText);
        }

        if(resultCode == RESULT_OK && requestCode == ChooseLocationActivity.REQUEST_EXECUTE_LOCATION){
            double locationLatitude = data.getDoubleExtra("locationLatitude", 0);
            double locationLongitude = data.getDoubleExtra("locationLongitude", 0);
            String locationText = data.getStringExtra("locationText");

            byte[] b = data.getByteArrayExtra("mapPic");
            Bitmap bitmap = BitmapFactory.decodeByteArray(b, 0, b.length);
            taskExecuteLocationImageview.setImageDrawable(new BitmapDrawable(getResources(), bitmap));

            xfbTask.setHappenLat(locationLatitude);
            xfbTask.setHappenLng(locationLongitude);
            xfbTask.setHappenLocationAutoDesc(locationText);
        }
    }

    //本地路径
    private Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            LogUtil.log.e("Xfb_Main", e.getMessage(), e);
            return null;
        }
    }

    /**
     * 压缩图片，防止内存溢出（这算啥压缩？？？）
     */
    public static Bitmap compressImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
    }

    /**
     * 时间控件相关的方法
     */
    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
            Toast.makeText(PublishActivity.this, "选择已取消", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {
            if(textViewTimeStartSelected){
                startDate = new pickedDate(
                        selectedDate.getStartDate().get(Calendar.YEAR),
                        selectedDate.getStartDate().get(Calendar.MONTH),
                        selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH),
                        hourOfDay,minute);
                textViewTimeStart.setText(startDate.toString());
                xfbTask.setStartTime(startDate.getDate());
            }
            if(textViewTimeEndSelected){
                endDate = new pickedDate(
                        selectedDate.getStartDate().get(Calendar.YEAR),
                        selectedDate.getStartDate().get(Calendar.MONTH),
                        selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH),
                        hourOfDay,minute);

                //比较时间的先后
//                if(endDate.earlierThan(startDate)){
//                    Toast.makeText(PublishActivity.this, "截至时间应迟于开始时间！", Toast.LENGTH_LONG).show();
//                    return;
//                }

                textViewTimeEnd.setText(endDate.toString());
                xfbTask.setEndTime(endDate.getDate());
            }

            Toast.makeText(PublishActivity.this, "选择成功", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 获取时间设置控件的参数
     * @return 参数
     */
    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = 0;

        displayOptions |= SublimeOptions.ACTIVATE_DATE_PICKER;
        displayOptions |= SublimeOptions.ACTIVATE_TIME_PICKER;

        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
        options.setDisplayOptions(displayOptions);
        options.setCanPickDateRange(false);

        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();

        if(textViewTimeStartSelected && startDate !=null){
            startCal.set(startDate.getYear(), startDate.getMonth(), startDate.getDay());
            endCal.set(startDate.getYear(), startDate.getMonth(), startDate.getDay());
            options.setDateParams(startCal, endCal);
        }
        if(textViewTimeEndSelected && endDate !=null){
            startCal.set(endDate.getYear(), endDate.getMonth(), endDate.getDay());
            endCal.set(endDate.getYear(), endDate.getMonth(), endDate.getDay());
            options.setDateParams(startCal, endCal);
        }

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(true, options);
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
