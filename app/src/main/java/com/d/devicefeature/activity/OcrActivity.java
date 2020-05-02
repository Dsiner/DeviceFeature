package com.d.devicefeature.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.d.devicefeature.R;
import com.d.lib.common.component.mvp.MvpBasePresenter;
import com.d.lib.common.component.mvp.MvpView;
import com.d.lib.common.component.mvp.app.BaseActivity;
import com.d.lib.common.util.UriUtil;
import com.d.lib.common.util.ViewHelper;
import com.d.lib.devicefeature.ocr.OcrAssetUtils;
import com.d.lib.devicefeature.ocr.OcrUtils;
import com.d.lib.taskscheduler.TaskScheduler;
import com.d.lib.taskscheduler.callback.Function;
import com.d.lib.taskscheduler.callback.Observer;
import com.d.lib.taskscheduler.callback.Task;
import com.d.lib.taskscheduler.schedule.Schedulers;

import java.io.File;

public class OcrActivity extends BaseActivity<MvpBasePresenter>
        implements MvpView, View.OnClickListener {
    private static final int REQUEST_PICK_IMAGE = 1001;

    ImageView iv_original;
    TextView tv_result;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_choose_image:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            "image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, REQUEST_PICK_IMAGE);
                } else {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, REQUEST_PICK_IMAGE);
                }
                break;

            case R.id.btn_recognition:
                final String path = (String) iv_original.getTag();
                if (TextUtils.isEmpty(path)) {
                    toast("Please choose image firstly!");
                    return;
                }
                tv_result.setText("Loading...");
                TaskScheduler.create(new Task<Bitmap>() {
                    @Override
                    public Bitmap run() {
                        Bitmap bmp = BitmapFactory.decodeFile(path);
                        return OcrUtils.convertGray(bmp);
                    }
                }).subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.mainThread())
                        .map(new Function<Bitmap, Bitmap>() {
                            @Override
                            public Bitmap apply(@NonNull Bitmap bitmap) throws Exception {
                                iv_original.setImageBitmap(bitmap);
                                return bitmap;
                            }
                        })
                        .observeOn(Schedulers.io())
                        .map(new Function<Bitmap, String>() {
                            @Override
                            public String apply(@NonNull Bitmap bitmap) throws Exception {
                                return OcrUtils.ocr(bitmap, "");
                            }
                        })
                        .observeOn(Schedulers.mainThread())
                        .subscribe(new Observer<String>() {
                            @Override
                            public void onNext(@NonNull String result) {
                                tv_result.setText(result);
                            }

                            @Override
                            public void onError(Throwable e) {
                                tv_result.setText(e.getMessage());
                            }
                        });
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_ocr;
    }

    @Override
    public MvpBasePresenter getPresenter() {
        return new MvpBasePresenter(getApplicationContext());
    }

    @Override
    protected MvpView getMvpView() {
        return this;
    }

    @Override
    protected void bindView() {
        super.bindView();
        iv_original = ViewHelper.findView(this, R.id.iv_original);
        tv_result = ViewHelper.findView(this, R.id.tv_result);

        ViewHelper.setOnClick(this, this, R.id.btn_choose_image,
                R.id.btn_recognition);
    }

    @Override
    protected void init() {
        copyTessData();
    }

    private void copyTessData() {
        TaskScheduler.create(new Task<Boolean>() {
            @Override
            public Boolean run() {
                File file = new File(OcrUtils.TESSBASE_PATH);
                File eng = new File(OcrUtils.TESSBASE_PATH + "/eng.traineddata");
                File chi_sim = new File(OcrUtils.TESSBASE_PATH + "/chi_sim.traineddata");
                return file.exists() && eng.exists() && chi_sim.exists();
            }
        }).subscribeOn(Schedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(@NonNull Boolean result) {
                        if (result) {
                            return;
                        }
                        OcrAssetUtils.copy(getApplicationContext());
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void toast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_PICK_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                toast("Failed to open picture!");
                return;
            }
            try {
                String path = UriUtil.getPath(this, data.getData());
                iv_original.setTag(path);
                iv_original.setImageBitmap(BitmapFactory.decodeFile(path));
                tv_result.setText("");
            } catch (Throwable e) {
                toast("Failed to read picture data!");
                e.printStackTrace();
            }
        }
    }
}
