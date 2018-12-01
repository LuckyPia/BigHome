package com.example.bighome.mvp.main.homepage;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatImageButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.bighome.R;
import com.example.bighome.data.BannerDetailData;
import com.example.bighome.glide.GlideLoader;
import com.example.bighome.mvp.main.MainActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomePageFragment extends Fragment implements HomePageContract.View {

    private HomePageContract.Presenter presenter;
    private boolean isFirstLoad = true;
    private MainActivity mainActivity;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.nsv)
    NestedScrollView nestedScrollView;
    @BindView(R.id.tv_title)
    AppCompatTextView tvTitle;
    @BindView(R.id.imagebutton_scan)
    AppCompatImageButton imageButtonScan;

    private Unbinder unbinder;


    public static HomePageFragment newInstance() {
        return new HomePageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_page, container, false);
        unbinder=ButterKnife.bind(this, view);

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
                    tvTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                    tvTitle.setShadowLayer(1, 1, 1, ContextCompat.getColor(getActivity(), R.color.black));
                    imageButtonScan.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.white)));
                } else {
                    swipeRefreshLayout.setEnabled(false);
                    tvTitle.setTextColor(ContextCompat.getColor(getActivity(), R.color.black));
                    tvTitle.setShadowLayer(0, 0, 0, ContextCompat.getColor(getActivity(), R.color.white));
                    imageButtonScan.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getActivity(), R.color.black)));
                }
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == 1) {
                    swipeRefreshLayout.setEnabled(false);
                } else {
                    swipeRefreshLayout.setEnabled(true);
                }
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=(MainActivity) getActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            presenter.getBanner();
        }
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (banner != null) {
            banner.stopAutoPlay();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void showBanner(List<BannerDetailData> list) {
        List<String> urls = new ArrayList<>();
        for (BannerDetailData item : list) {
            urls.add(item.getImagePath());
        }
        if (banner == null) {
            Log.d("banner", "空");
        } else {
            banner.setImages(urls);
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
            banner.setImageLoader(new GlideLoader());
            banner.setBannerAnimation(Transformer.ZoomOutSlide);
            banner.isAutoPlay(true);
            banner.setDelayTime(7800);
            banner.start();
        }
    }

    @Override
    public void hideBanner() {
        banner.setVisibility(View.GONE);
    }

    @Override
    public void initViews(View view) {

    }

    @Override
    public void setPresenter(HomePageContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
