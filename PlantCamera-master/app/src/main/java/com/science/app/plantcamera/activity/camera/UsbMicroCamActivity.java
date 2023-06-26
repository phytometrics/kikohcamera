package com.science.app.plantcamera.activity.camera;

import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.convergence.excamera.sdk.common.OutputUtil;
import com.convergence.excamera.sdk.common.callback.OnCameraPhotographListener;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.RequestCallback;
import com.science.app.plantcamera.IApp;
import com.science.app.plantcamera.PreferenceRepository;
import com.science.app.plantcamera.R;
import com.science.app.plantcamera.activity.ExCameraActivity;
import com.science.app.plantcamera.manager.CamManager;
import com.science.app.plantcamera.manager.UsbMicroCamManager;
import com.convergence.excamera.sdk.usb.core.UsbCameraView;
import com.science.app.plantcamera.view.config.UsbMicroConfigLayout;
import com.science.app.plantcamera.view.dialog.InputPrefixDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

/**
 * 显微相机USB连接页面
 *
 * @Author WangZiheng
 * @CreateDate 2020-11-06
 * @Organization Convergence Ltd.
 */
public class UsbMicroCamActivity extends ExCameraActivity
    implements InputPrefixDialog.InputPrefixDialogListener, OnCameraPhotographListener
{

    @BindView(R.id.iv_close_activity_usb_micro_camera)
    ImageView ivCloseActivityUsbMicroCamera;
    @BindView(R.id.iv_take_photo_activity_usb_micro_camera)
    ImageView ivTakePhotoActivityUsbMicroCamera;
    @BindView(R.id.iv_record_activity_usb_micro_camera)
    ImageView ivRecordActivityUsbMicroCamera;
    @BindView(R.id.iv_resolution_activity_usb_micro_camera)
    ImageView ivResolutionActivityUsbMicroCamera;
    @BindView(R.id.iv_config_activity_usb_micro_camera)
    ImageView ivConfigActivityUsbMicroCamera;
    @BindView(R.id.view_usb_camera_activity_usb_micro_camera)
    UsbCameraView viewUsbCameraActivityUsbMicroCamera;
    @BindView(R.id.tv_fps_activity_usb_micro_camera)
    TextView tvFpsActivityUsbMicroCamera;
    @BindView(R.id.tv_record_time_activity_usb_micro_camera)
    TextView tvRecordTimeActivityUsbMicroCamera;
    @BindView(R.id.tv_prefix_activity_usb_micro_camera)
    TextView tvPrefixActivityUsbMicroCamera;
    @BindView(R.id.tv_count_activity_usb_micro_camera)
    TextView tvCountActivityUsbMicroCamera;
    @BindView(R.id.item_config_activity_usb_micro_camera)
    UsbMicroConfigLayout itemConfigActivityUsbMicroCamera;
    @BindView(R.id.layout_config_activity_usb_micro_camera)
    LinearLayout layoutConfigActivityUsbMicroCamera;

    // アプリ設定Repository
    private PreferenceRepository preferenceRepository;
    // 画像ファイル保存中フラグ
    private boolean isSaving = false;
    // 撮影回数のフォーマット
    private final String formatCount = "%06d";

    @Override
    protected int onBindLayoutId() {
        return R.layout.activity_usb_micro_camera;
    }

    @Override
    protected CamManager onBindCamManager() {
        return new UsbMicroCamManager.Builder(this, viewUsbCameraActivityUsbMicroCamera)
                .bindConfigLayout(itemConfigActivityUsbMicroCamera)
                .bindRecordTimeText(tvRecordTimeActivityUsbMicroCamera)
                .bindFpsText(tvFpsActivityUsbMicroCamera)
                .build();
    }

    @Override
    protected void init() {
        preferenceRepository = new PreferenceRepository(this);
        preferenceRepository.load();
        setPrefixView();
        setCountView();
    }

    @OnClick({
        R.id.iv_close_activity_usb_micro_camera, R.id.iv_take_photo_activity_usb_micro_camera,
        R.id.iv_record_activity_usb_micro_camera, R.id.iv_resolution_activity_usb_micro_camera,
        R.id.iv_config_activity_usb_micro_camera, R.id.iv_prefix_activity_usb_micro_camera,
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close_activity_usb_micro_camera:
                finish();
                break;
            case R.id.iv_take_photo_activity_usb_micro_camera:
                checkPermission(callbackTakePhoto);
                break;
            case R.id.iv_record_activity_usb_micro_camera:
                checkPermission(callbackRecord);
                break;
            case R.id.iv_resolution_activity_usb_micro_camera:
                camManager.showResolutionSelection();
                break;
            case R.id.iv_config_activity_usb_micro_camera:
                int visible = View.INVISIBLE;
                if (layoutConfigActivityUsbMicroCamera.getVisibility() == View.VISIBLE) {
                    visible = View.INVISIBLE;
                } else {
                    visible = View.VISIBLE;
                }
                layoutConfigActivityUsbMicroCamera.setVisibility(visible);
                break;
            case R.id.iv_prefix_activity_usb_micro_camera:
                InputPrefixDialog dialog = new InputPrefixDialog();
                Bundle args = new Bundle();args.putString("FILE_PREFIX", preferenceRepository.getFilePrefix());
                dialog.setArguments(args);
                dialog.show(getSupportFragmentManager(), "prefix");
                break;
            default:
                break;
        }
    }

    @OnLongClick({R.id.iv_take_photo_activity_usb_micro_camera, R.id.iv_record_activity_usb_micro_camera})
    public void onViewLongClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_take_photo_activity_usb_micro_camera:
                camManager.startStackAvg();
                break;
            case R.id.iv_record_activity_usb_micro_camera:
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

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        // 指定キーイベントでカメラ撮影
        switch (e.getKeyCode()) {
            case KeyEvent.KEYCODE_2:
            case KeyEvent.KEYCODE_NUMPAD_2:
                takePhoto();
                break;
            default:
                break;
        }
        return super.dispatchKeyEvent(e);
    }

    // InputPrefixDialog.InputPrefixDialogListener
    // OKボタンタップ
    @Override
    public void onDialogPositiveClick(String prefix) {
        preferenceRepository.setFilePrefix(prefix);
        preferenceRepository.save();
        setPrefixView();
    }

    // Cancelボタンタップ
    @Override
    public void onDialogNegativeClick() {

    }

    // OnCameraPhotographListener
    @Override
    public void onTakePhotoStart() {

    }

    @Override
    public void onTakePhotoDone() {

    }

    @Override
    public void onTakePhotoSuccess(String path) {
        // 画像ファイル保存成功
        isSaving = false;
        preferenceRepository.incrementPhotoCount();
        preferenceRepository.save();
        setCountView();
        IApp.showToast(getString(R.string.message_photo_file_save_complete));
    }

    @Override
    public void onTakePhotoFail() {
        // 画像ファイル保存失敗
        isSaving = false;
        IApp.showToast(getString(R.string.message_error_photo_file_save));
    }

    // 録画用のPermissionチェックCallback
    RequestCallback callbackRecord = (allGranted, grantedList, deniedList) -> {
        // 全てのPermission許諾チェック
        if (allGranted) {
            // プレビューチェック
            if (!camManager.isPreviewing()) {
                return;
            }
            // 録画開始/停止
            if (camManager.isRecording()) {
                camManager.stopRecord();
            } else {
                camManager.startRecord();
            }
        } else {
            IApp.showLongToast(getString(R.string.permission_denied_write_storage));
        }
    };

    // 撮影用のPermissionチェックCallback
    RequestCallback callbackTakePhoto = (allGranted, grantedList, deniedList) -> {
        // 全てのPermission許諾チェック
        if (allGranted) {
            // プレビューチェック
            if (!camManager.isPreviewing()) {
                return;
            }
            // 撮影して画像ファイルへ保存
            takePhoto();
        } else {
            IApp.showLongToast(getString(R.string.permission_denied_write_storage));
        }
    };

    // Permissionチェック
    void checkPermission(RequestCallback callback) {
        PermissionX.init(this)
            .permissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .request(callback);
    }

    // 撮影して画像ファイルへ保存
    void takePhoto() {
        // 保存中チェック
        if (isSaving) {
            return;
        }
        isSaving = true;
        String path = makePhotoFilePath();
        camManager.takePhoto(path);
    }

    // 写真のファイル名生成
    // yyyymmdd_hhmmss_prefix_000001.png
    // prefixが未設定はファイルに含めない
    private String makePhotoFilePath() {
        String path = OutputUtil.getDefaultRootPath() + File.separator;
        Date date = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN);
        String timestamp = df.format(date);
        String filename = "";
        String prefix = preferenceRepository.getFilePrefix();
        if (!prefix.isEmpty()) {
            filename += "_" + prefix;
        }
        String count = "_" + String.format(Locale.JAPAN, formatCount, preferenceRepository.getPhotoCount());
        String ext = ".png";
        return path + timestamp + filename + count + ext;
    }

    // Prefixを画面に設定
    private void setPrefixView() {
        tvPrefixActivityUsbMicroCamera.setText(preferenceRepository.getFilePrefix());
    }

    // 撮影回数を画面に設定
    private void setCountView() {
        String count = String.format(Locale.JAPAN, formatCount, preferenceRepository.getPhotoCount());
        tvCountActivityUsbMicroCamera.setText(count);
    }
}
