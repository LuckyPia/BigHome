package com.example.bighome.mvp.main;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bighome.R;
import com.example.bighome.data.source.remote.BannerDataRemoteSource;
import com.example.bighome.data.source.BannerDataRepository;
import com.example.bighome.mvp.BaseActivity;
import com.example.bighome.mvp.main.homepage.HomePageFragment;
import com.example.bighome.mvp.main.homepage.HomePagePresenter;
import com.example.bighome.mvp.main.mine.InformationActivity;
import com.example.bighome.mvp.main.mine.MineFragment;
import com.example.bighome.mvp.main.mine.SettingActivity;
import com.example.bighome.mvp.main.neighbor.NeighborFragment;
import com.example.bighome.util.TintUtil;
import com.hjq.toast.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import eu.long1.spacetablayout.SpaceTabLayout;

public class MainActivity extends BaseActivity {
    //add the fragments you want to display in a List
    List<Fragment> fragmentList = new ArrayList<>();
    private long startTime = 0;

    //@BindView(R.id.bottom_navigation_view)
    //BottomNavigationView bottomNavigationView;
    @BindView(R.id.spaceTabLayout)
    SpaceTabLayout spaceTabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private HomePageFragment homePageFragment;
    private NeighborFragment neighborFragment;
    private MineFragment mineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        Intent intent=getIntent();
        if(!intent.getStringExtra("username").isEmpty()){
            //Toast.makeText(this,intent.getStringExtra("username")+",欢迎您!",Toast.LENGTH_SHORT).show();
            ToastUtils.show(intent.getStringExtra("username")+",欢迎您!");
            intent.removeExtra("username");
        }

        initFragments(savedInstanceState);

        fragmentList.add(homePageFragment);
        fragmentList.add(neighborFragment);
        fragmentList.add(mineFragment);


        //we need the savedInstanceState to get the position
        spaceTabLayout.initialize(viewPager, getSupportFragmentManager(),
                fragmentList, savedInstanceState);

        initSpaceTabLayout();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        spaceTabLayout.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    private void initSpaceTabLayout(){
        spaceTabLayout.setTabOneIcon(
                TintUtil.tintBitmap(
                        BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_home_white_24dp),
                        ContextCompat.getColor(this,R.color.white),this.getResources()));
        spaceTabLayout.setTabTwoIcon(TintUtil.tintBitmap(
                BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_people_white_24dp),
                ContextCompat.getColor(this,R.color.white),this.getResources()));
        spaceTabLayout.setTabThreeIcon(TintUtil.tintBitmap(
                BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_account_circle_white_24dp),
                ContextCompat.getColor(this,R.color.white),this.getResources()));

        spaceTabLayout.setTabOneText("首页");
        spaceTabLayout.setTabTwoText("邻里");
        spaceTabLayout.setTabThreeText("我的");
    }

    private void initFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            homePageFragment=(HomePageFragment) fragmentManager.getFragment(savedInstanceState, HomePageFragment.class.getSimpleName());
        }else{
            homePageFragment = HomePageFragment.newInstance();
        }
        neighborFragment = NeighborFragment.newInstance();
        mineFragment = MineFragment.newInstance();
        new HomePagePresenter(homePageFragment,BannerDataRepository.getInstance(BannerDataRemoteSource.getInstance()));

    }

    /**
     * 主界面不需要支持滑动返回，重写该方法永久禁用当前界面的滑动返回功能
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return false;
    }

    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - startTime) >= 2000) {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            startTime = currentTime;
        } else {
            //mSwipeBackHelper.backward();
            removeALLActivity();
        }
    }

    public void settingSwipeBack() {
        mSwipeBackHelper.forward(SettingActivity.class);
    }
    public void informationSwipeBack() {
        mSwipeBackHelper.forward(InformationActivity.class);
    }

}
