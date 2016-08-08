package org.gis4.xfb.hurricanehelp.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import org.gis4.xfb.hurricanehelp.R;

public class FadanActivity extends BaseActivity {

    private Toolbar toolbar;
    private Spinner spinner;
    private EditText editTextReceiveLocationDiscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fadan);

        spinner = (Spinner) findViewById(R.id.task_sype);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_fadan_menu);

        initialMenu(toolbar.getMenu());
        initialSpinner();
        initialActivity();
    }

    private void initialSpinner() {
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this,
                R.array.task_sype, R.layout.spinner_item);

        //adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
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
                        Toast.makeText(FadanActivity.this, "提交", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_clear:
                        Toast.makeText(FadanActivity.this, "重置", Toast.LENGTH_SHORT).show();
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

    }

}
