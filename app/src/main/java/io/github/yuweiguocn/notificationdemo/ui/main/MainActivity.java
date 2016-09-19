package io.github.yuweiguocn.notificationdemo.ui.main;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;

import io.github.yuweiguocn.notificationdemo.R;
import io.github.yuweiguocn.notificationdemo.base.BaseActivity;
import io.github.yuweiguocn.notificationdemo.databinding.ActivityMainBinding;
import io.github.yuweiguocn.notificationdemo.ui.about.AboutActivity;

public class MainActivity extends BaseActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(binding.toolbar.toolbar);
        binding.setHandler(new MainClickHandlers(this));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        startActivity(new Intent(this, AboutActivity.class));
        return true;
    }

}
