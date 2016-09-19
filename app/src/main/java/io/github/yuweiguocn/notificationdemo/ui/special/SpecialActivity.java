package io.github.yuweiguocn.notificationdemo.ui.special;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import io.github.yuweiguocn.notificationdemo.R;
import io.github.yuweiguocn.notificationdemo.base.BaseActivity;

public class SpecialActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_special);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
    }

}
