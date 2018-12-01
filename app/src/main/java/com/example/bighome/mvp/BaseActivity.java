package com.example.bighome.mvp;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.bighome.R;
import com.example.bighome.app.MyApp;
import com.example.bighome.util.StatuUtil;

import butterknife.ButterKnife;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

public abstract class BaseActivity extends AppCompatActivity implements BGASwipeBackHelper.Delegate {

    private MyApp app;
    private BaseActivity baseContext;
    protected BGASwipeBackHelper mSwipeBackHelper;

    TextView tvTitle;
    android.support.v7.widget.Toolbar mToolbar;
    protected static android.support.v7.app.ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 在 super.onCreate(savedInstanceState) 之前调用该方法
        initSwipeBackFinish();
        super.onCreate(savedInstanceState);
        StatuUtil.setDarkStatusIcon(true,getWindow());

        if (app == null) {
            // 得到Application对象
            app = (MyApp) getApplication();
        }

        baseContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
    }

    protected void initToolbar(String title,@LayoutRes int layoutId){
        FrameLayout frameLayout=ButterKnife.findById(this,R.id.frame_layout);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View settingLayout = layoutInflater.inflate(layoutId, null);
        frameLayout.addView(settingLayout);
        mToolbar=ButterKnife.findById(this,R.id.toolbar);
        tvTitle=ButterKnife.findById(this,R.id.tv_title);
        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setTitle("");
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvTitle.setText(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mSwipeBackHelper.backward();
                //this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {

    }

    @Override
    public void onSwipeBackLayoutCancel() {

    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    // 添加Activity方法
    public void addActivity() {
        app.addActivity(baseContext);// 调用myApplication的添加Activity方法
    }
    //销毁当个Activity方法
    public void removeActivity() {
        app.removeActivity(baseContext);// 调用myApplication的销毁单个Activity方法
    }
    //销毁所有Activity方法
    public void removeALLActivity() {
        app.removeALLActivity();// 调用myApplication的销毁所有Activity方法
    }
}
