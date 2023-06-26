package com.science.app.plantcamera.activity.camera;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.science.app.plantcamera.R;
import com.science.app.plantcamera.activity.ExCameraActivity;
import com.science.app.plantcamera.manager.CamManager;
import com.science.app.plantcamera.manager.WifiMicroCamManager;
import com.convergence.excamera.sdk.wifi.core.WifiCameraView;
import com.science.app.plantcamera.view.config.WifiMicroConfigLayout;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * 显微相机WiFi连接页面
 *
 * @Author WangZiheng
 * @CreateDate 2020-11-17
 * @Organization Convergence Ltd.
 */
public class WifiMicroCamActivity extends ExCameraActivity {

    @BindView(R.id.iv_close_activity_wifi_micro_camera)
    ImageView ivCloseActivityWifiMicroCamera;
    @BindView(R.id.iv_take_photo_activity_wifi_micro_camera)
    ImageView ivTakePhotoActivityWifiMicroCamera;
    @BindView(R.id.iv_record_activity_wifi_micro_camera)
    ImageView ivRecordActivityWifiMicroCamera;
    @BindView(R.id.iv_resolution_activity_wifi_micro_camera)
    ImageView ivResolutionActivityWifiMicroCamera;
    @BindView(R.id.view_wifi_camera_activity_wifi_micro_camera)
    WifiCameraView viewWifiCameraActivityWifiMicroCamera;
    @BindView(R.id.tv_fps_activity_wifi_micro_camera)
    TextView tvFpsActivityWifiMicroCamera;
    @BindView(R.id.tv_record_time_activity_wifi_micro_camera)
    TextView tvRecordTimeActivityWifiMicroCamera;
    @BindView(R.id.item_config_activity_wifi_micro_camera)
    WifiMicroConfigLayout itemConfigActivityWifiMicroCamera;

    @Override
    protected int onBindLayoutId() {
        return R.layout.activity_wifi_micro_camera;
    }

    @Override
    protected CamManager onBindCamManager() {
        return new WifiMicroCamManager.Builder(this, viewWifiCameraActivityWifiMicroCamera)
                .bindConfigLayout(itemConfigActivityWifiMicroCamera)
                .bindRecordTimeText(tvRecordTimeActivityWifiMicroCamera)
                .bindFpsText(tvFpsActivityWifiMicroCamera)
                .build();
    }

    @Override
    protected void init() {

    }

    @OnClick({R.id.iv_close_activity_wifi_micro_camera, R.id.iv_take_photo_activity_wifi_micro_camera, R.id.iv_record_activity_wifi_micro_camera, R.id.iv_resolution_activity_wifi_micro_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_activity_wifi_micro_camera:
                finish();
                break;
            case R.id.iv_take_photo_activity_wifi_micro_camera:
                camManager.takePhoto();
                break;
            case R.id.iv_record_activity_wifi_micro_camera:
                if (camManager.isRecording()) {
                    camManager.stopRecord();
                } else {
                    camManager.startRecord();
                }
                break;
            case R.id.iv_resolution_activity_wifi_micro_camera:
                camManager.showResolutionSelection();
                break;
            default:
                break;
        }
    }

    @OnLongClick({R.id.iv_take_photo_activity_wifi_micro_camera, R.id.iv_record_activity_wifi_micro_camera})
    public void onViewLongClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_take_photo_activity_wifi_micro_camera:
                camManager.startStackAvg();
                break;
            case R.id.iv_record_activity_wifi_micro_camera:
                if (camManager.isTLRecording()) {
                    camManager.stopTLRecord();
                } else {
                    camManager.startTLRecord(15);
                }
                break;
            default:
                break;
        }
    }
}
