package org.gis4.xfb.hurricanehelp.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
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

import com.rey.material.widget.Button;
import com.rey.material.widget.Slider;
import com.rey.material.widget.Spinner;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;
import com.yongchun.library.view.ImageSelectorActivity;

import org.gis4.xfb.hurricanehelp.R;
import org.gis4.xfb.hurricanehelp.data.pickedDate;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;

public class PublishActivity extends BaseActivity {

    private Toolbar toolbar;
    private Spinner spinner;
    private TextView textViewTimeStart;
    private TextView textViewTimeEnd;
    private TextView textviewPoints;
    private TextView textviewMyPoints;
    private Slider sliderPoints;

    private ImageView taskSendLocationImageview;
    private ImageView taskExecuteLocationImageview;

    private Button selectOrClearImages;
    private boolean hasSelectImage;
    private ImageView[] imageViewsArray;

    private boolean textViewTimeStartSelected, textViewTimeEndSelected;
    private pickedDate startDate, endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish);

        spinner = (Spinner) findViewById(R.id.task_type);
        sliderPoints = (Slider) findViewById(R.id.slider_points);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_publish_menu);

        textViewTimeStart =(TextView) findViewById(R.id.textview_time_start);
        textViewTimeEnd =(TextView) findViewById(R.id.textview_time_end);
        textviewMyPoints =(TextView) findViewById(R.id.textview_my_points);
        textviewPoints =(TextView) findViewById(R.id.textview_points);

        taskSendLocationImageview =(ImageView) findViewById(R.id.task_send_location_imageview);
        taskExecuteLocationImageview =(ImageView) findViewById(R.id.task_execute_location_imageview);

        selectOrClearImages =(Button) findViewById(R.id.select_or_clear_images);
        hasSelectImage =false;
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
                        Toast.makeText(PublishActivity.this, "提交", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_clear:
                        Toast.makeText(PublishActivity.this, "重置", Toast.LENGTH_SHORT).show();
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
                // DialogFragment to host SublimePicker
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                // Options
                textViewTimeStartSelected = true;
                textViewTimeEndSelected = false;
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                // Valid options
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

                // DialogFragment to host SublimePicker
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                // Options
                textViewTimeStartSelected = false;
                textViewTimeEndSelected = true;
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");

            }
        });
    }

    //初始化地点选择功能
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
            }
        });
    }

    private void initialImageSelect(){
        selectOrClearImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hasSelectImage){
                    for(int n = 0; n<imageViewsArray.length; n++){
                        imageViewsArray[n].setImageDrawable(getResources().getDrawable(R.mipmap.publish_activity_noimage));
                    }

                    Toast.makeText(PublishActivity.this, "清空已选", Toast.LENGTH_SHORT).show();
                    selectOrClearImages.setBackgroundDrawable(getResources().getDrawable(R.mipmap.publish_activity_addimage));
                    hasSelectImage = false;
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

            for(int n =0; n<imagePaths.size(); n++){
                Bitmap bitmap = getLoacalBitmap(imagePaths.get(n));

                //将图片的宽或者高压缩至300像素
                double newWidth = 0, newHeight = 0;
                if(bitmap.getHeight() > bitmap.getWidth()){
                    newWidth = 300;
                    newHeight = bitmap.getHeight() * newWidth / bitmap.getWidth();
                }
                else {
                    newHeight = 300;
                    newWidth = bitmap.getWidth() * newHeight / bitmap.getHeight();
                }
                Bitmap smallBitmap = zoomImage(bitmap, newWidth, newHeight);
                imageViewsArray[n].setImageBitmap(smallBitmap);
            }
        }

        if(resultCode == RESULT_OK && requestCode == ChooseLocationActivity.REQUEST_SEND_LOCATION){
            Toast.makeText(PublishActivity.this, data.getStringExtra("location"), Toast.LENGTH_SHORT).show();
        }

        if(resultCode == RESULT_OK && requestCode == ChooseLocationActivity.REQUEST_EXECUTE_LOCATION){
            Toast.makeText(PublishActivity.this, data.getStringExtra("location"), Toast.LENGTH_SHORT).show();
        }
    }

    //本地路径
    private Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);  ///把流转化为Bitmap图片

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    //压缩图片，防止内存溢出
    public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
        return bitmap;
    }

    //时间控件相关的方法
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
            }
            if(textViewTimeEndSelected){
                endDate = new pickedDate(
                        selectedDate.getStartDate().get(Calendar.YEAR),
                        selectedDate.getStartDate().get(Calendar.MONTH),
                        selectedDate.getStartDate().get(Calendar.DAY_OF_MONTH),
                        hourOfDay,minute);

                if(endDate.earlierThan(startDate)){
                    Toast.makeText(PublishActivity.this, "截至时间应迟于开始时间！", Toast.LENGTH_LONG).show();
                    return;
                }

                textViewTimeEnd.setText(endDate.toString());
            }

            Toast.makeText(PublishActivity.this, "选择成功", Toast.LENGTH_SHORT).show();
        }
    };

    //获取时间设置空间的参数
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

}
