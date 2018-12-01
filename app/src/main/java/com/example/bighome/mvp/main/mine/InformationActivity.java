package com.example.bighome.mvp.main.mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bighome.R;
import com.example.bighome.mvp.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InformationActivity extends BaseActivity {

    private static android.support.v7.app.ActionBar actionBar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_child);

        initToolbar("我的资料",R.layout.layout_information);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
