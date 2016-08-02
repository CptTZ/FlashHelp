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
import android.widget.Toast;

import org.gis4.xfb.hurricanehelp.R;

public class FadanActivity extends BaseActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fadan);

        toolbar=(Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.activity_fadan_menu);
        initialMenu(toolbar.getMenu());
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

}
