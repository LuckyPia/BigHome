package com.example.bighome.mvp.main.mine;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.bighome.R;
import com.example.bighome.mvp.main.MainActivity;
import com.example.bighome.util.SettingsUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

public class MineFragment extends Fragment implements View.OnClickListener {

    private MainActivity mainActivity;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_banner)
    ImageView ivBanner;
    @BindView(R.id.civ_portrait)
    CircleImageView civPortrait;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.image_button_setting)
    AppCompatImageButton btnSetting;

    private Unbinder unbinder;

    public static MineFragment newInstance(){
        return new MineFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder=ButterKnife.bind(this,view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        //设置样式刷新显示的位置
        swipeRefreshLayout.setProgressViewOffset(true, -20, 100);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (verticalOffset >= 0) {
                    swipeRefreshLayout.setEnabled(true);
                    tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    tvTitle.setShadowLayer(1, 1, 1, ContextCompat.getColor(getContext(), R.color.black));
                    btnSetting.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.white)));
                } else {
                    swipeRefreshLayout.setEnabled(false);
                    tvTitle.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                    tvTitle.setShadowLayer(0, 0, 0, ContextCompat.getColor(getContext(), R.color.white));
                    btnSetting.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.black)));

                }
            }
        });
        btnSetting.setOnClickListener(this);
        civPortrait.setOnClickListener(this);
        tvUserName.setOnClickListener(this);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String userName = sp.getString(SettingsUtil.USERNAME, "");
        if (!userName.equals("")) {
            tvUserName.setText(userName+",欢迎您!");
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=(MainActivity) getActivity();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.image_button_setting:
                //startActivity(new Intent(getContext(),SettingActivity.class));
                mainActivity.settingSwipeBack();
                break;

            case R.id.tv_username:
            case R.id.civ_portrait:
                //startActivity(new Intent(getContext(),InformationActivity.class));
                mainActivity.informationSwipeBack();
                break;
                default:
                    break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
