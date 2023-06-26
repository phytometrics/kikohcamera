package com.science.app.plantcamera.activity;

import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.science.app.plantcamera.R;
import com.science.app.plantcamera.manager.CamManager;
import com.gyf.immersionbar.ImmersionBar;

import butterknife.ButterKnife;

/**
 * ExCamera页面基类
 *
 * @Author WangZiheng
 * @CreateDate 2020-12-18
 * @Organization Convergence Ltd.
 */
public abstract class ExCameraActivity extends AppCompatActivity {

    protected CamManager camManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(onBindLayoutId());
        ButterKnife.bind(this);
        camManager = onBindCamManager();
//        ImmersionBar.with(this)
//                .transparentBar()
//                .statusBarDarkFont(true)
//                .navigationBarDarkIcon(true)
//                .init();

        ImmersionBar.with(this)
                .statusBarDarkFont(false)
                .statusBarColor(R.color.black)
                .navigationBarColor(R.color.black)
                .fitsSystemWindows(true)
                .keyboardEnable(true)
                .init();

        init();
    }

    /**
     * 设置布局id
     *
     * @return 布局id
     */
    @LayoutRes
    protected abstract int onBindLayoutId();

    /**
     * 设置CamManager
     *
     * @return CamManager
     */
    protected abstract CamManager onBindCamManager();

    /**
     * 初始化
     */
    protected abstract void init();

    @Override
    protected void onStart() {
        super.onStart();
        camManager.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        camManager.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        camManager.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        camManager.onStop();
    }

    @Override
    protected void onDestroy() {
        camManager.onDestroy();
        super.onDestroy();
    }
}
