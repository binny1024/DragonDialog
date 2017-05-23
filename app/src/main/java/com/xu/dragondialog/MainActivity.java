package com.xu.dragondialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.smart.dialog_library.DialogCustom;
import com.smart.dialog_library.DragonConstant;
import com.smart.dialog_library.callback.OnDoubleBtnClickedListener;
import com.smart.dialog_library.callback.OnDownloadApkListener;
import com.smart.dialog_library.callback.OnSingleBtnClickedListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.btn_double)
    Button btnDouble;
    @BindView(R.id.btn_single)
    Button btnSingle;
    @BindView(R.id.btn_download)
    Button btnDownload;
    @BindView(R.id.btn_toast)
    Button btnToast;

    private final String msg="设置了背景色设置了背景色设置了背景色设置了\"+\"\\n\"+设置了背景色设置了背景色设置了背景色设置了\"+\"\\n\"+设置了背景色设置了背景色设置了背景色设置了\"+\"\\n\"+设置了背景色设置了背景色设置了背景色设置了\"+\"\\n\"+设置了背景色设置了背景色设置了背景色设置了\"+\"\\n\"+设置了背景色设置了背景色设置了背景色设置了\"+\"\\n\"+设置了背景色设置了背景色设置了背景色设置了\"+\"\\n\"+设置了背景色设置了背景色设置了背景色设置了"+"\n"+"背景色设置了背景色设置了背景色设置了背景色设置了背景色设置了背景色设置了背景色设置了背景色设置了背景色设置了背景色";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_double)
    public void onBtnDoubleClicked() {
        new DialogCustom(this).setDoubleBtnTextLR("取消", "确定")
                .setDialogScale(0.5f,0.9f)
                .setTitle("设置了背景色")
                .setTitleBackgroundResId(R.drawable.title_bg)
                .setMessage(msg)
                .setInOutAnimationStyle(DragonConstant.ANIM_SCALE_IN_ALPHA_OUT)
                .setMessageBackgroundResId(R.drawable.msg_bg)
                .setOnDoubleBtnClickedListener(new OnDoubleBtnClickedListener() {
                    @Override
                    public void onLeftBtnClick(DialogCustom dialogCustom) {
                        dialogCustom.dismiss();
                    }

                    @Override
                    public void onRightBtnClick(DialogCustom dialogCustom) {
                        dialogCustom.dismiss();
                    }
                }).show();
    }

    @OnClick(R.id.btn_single)
    public void onBtnSingleClicked() {
        new DialogCustom(this).setTitle("默认背景色")
                .setMessage(msg)
                .setSingleBtnTextR("one")
                .setInOutAnimationStyle(DragonConstant.ANIM_FADE_IN_OUT)
                .setOnSingleClicedkListener(new OnSingleBtnClickedListener() {
                    @Override
                    public void onRightBtnClick(DialogCustom dialogCustom) {
                        dialogCustom.dismiss();
                    }
                })
                .show();
    }

    @OnClick(R.id.btn_download)
    public void onBtnDownloadClicked() {
        String apkUrl = "http://hiao.com/android/bus/QingDaoBus.apk";
        new DialogCustom(this)
                .setTitle("您确定要下载么？")
                .setTitleBackgroundResId(R.mipmap.ic_launcher)
                .setDownloadBtnText("取消", "确定")
                .setInOutAnimationStyle(R.style.slide_left_right)
                .setLeftBtnTextColor(R.color.white)
                .setDoubleBtnBackgroungRes(R.drawable.left_btn_bg, R.drawable.right_btn_bg)
                .setMessage(msg)
                .setDownloadUrl(apkUrl, "")
                .setClickedAnimation(true)
                .setOnDownloadListener(new OnDownloadApkListener() {
                    @Override
                    public void result(DialogCustom dialogCustom) {
                        dialogCustom.dismiss();
                    }

                    @Override
                    public void failure(Exception e, DialogCustom dialogCustom) {
                        dialogCustom.dismiss();
                    }
                }).show();
    }

    @OnClick(R.id.btn_toast)
    public void onViewClicked() {
        new DialogCustom(this)
                .setToast(msg, 2000)
                .setInOutAnimationStyle(DragonConstant.ANIM_FADE_IN_OUT)
//                .setToastDrawableId(R.drawable.dialog_bg)
//                .setDialogScale(0.2f, 0.5f)
//                .setDialogOffPos(null, 0.2f)
                .show();
    }


}
