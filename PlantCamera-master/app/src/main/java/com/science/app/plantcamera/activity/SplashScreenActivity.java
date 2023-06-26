package com.science.app.plantcamera.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.permissionx.guolindev.PermissionX;
import com.science.app.plantcamera.R;
import com.science.app.plantcamera.activity.camera.UsbMicroCamActivity;

// SplashScreenActivity
// ロゴをGIFアニメーション表示
public class SplashScreenActivity extends AppCompatActivity {

    private static final int SPLASH_SCREEN_TIME_OUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // GIFアニメーション設定
        ImageView splashScreenGif = findViewById(R.id.splash_screen_gif);
        DrawableImageViewTarget target = new DrawableImageViewTarget(splashScreenGif);
        DrawableImageViewTarget into = Glide
            .with(this)
            .load(R.raw.motionlogo)
            .listener(new RequestListener<Drawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {
                    return false;
                }
                @Override
                public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                    // ループ回数1 = ループさせない
                    ((GifDrawable)drawable).setLoopCount(1);
                    return false;
                }
            })
            .into(target);

        new Handler().postDelayed(() -> {
            runOnUiThread(() -> {
                // Permissionチェック後にカメラ画面表示
                PermissionX.init(this)
                    .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request((allGranted, grantedList, deniedList) -> {
                        Intent mainActivityIntent = new Intent(SplashScreenActivity.this, UsbMicroCamActivity.class);
                        startActivity(mainActivityIntent);
                        finish();
                    }
                );
            });
        }, SPLASH_SCREEN_TIME_OUT);
    }
}