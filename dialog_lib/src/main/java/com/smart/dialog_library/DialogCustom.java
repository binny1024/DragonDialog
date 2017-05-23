package com.smart.dialog_library;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.smart.dialog_library.callback.OnDoubleBtnClickedListener;
import com.smart.dialog_library.callback.OnDownloadApkListener;
import com.smart.dialog_library.callback.OnSingleBtnClickedListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.smart.dialog_library.DragonConstant.ANIM_FADE_IN_OUT;
import static com.smart.dialog_library.DragonConstant.ANIM_HYPERSPACE_IN_OUT;
import static com.smart.dialog_library.DragonConstant.ANIM_PUSH_LEFT_IN_OUT;
import static com.smart.dialog_library.DragonConstant.ANIM_PUSH_UP_IN_OUT;
import static com.smart.dialog_library.DragonConstant.ANIM_SCALE_IN_ALPHA_OUT;
import static com.smart.dialog_library.DragonConstant.ANIM_SLIDE_LEFT_RIGHT;
import static com.smart.dialog_library.DragonConstant.ANIM_SLIDE_UP_DOWN;
import static com.smart.dialog_library.DragonConstant.downloadCachePath;
import static com.smart.dialog_library.R.id.linearLayout_title_msg;
import static com.smart.dialog_library.Utils.getAppName;

/**
 * Created by xu on 2017/3/3.
 */
public class DialogCustom extends Dialog {

    private final Resources res;
    private Activity mContext;
    private DialogCustom instance;
//    private LinearLayout linearLayoutDialog;
    private LinearLayout linearLayoutTitleMsg;
    private RelativeLayout rl_download;

    /*
    * 标题
     */
    private TextView titleTv;//消息标题文本
    private ColorStateList titleTextColor;//标题字体颜色
    private String titleStr;//从外界设置的title文本
    private int titleSize;//标题大小
    private View titleMsgLine;
    private Drawable titleDrawable;//标题的背景

    /*
    * 消息
    */
    private String messageStr="";//从外界设置的消息文本
    private TextView messageTv;//消息提示文本
    private int messageSize;
    private ColorStateList messageTextColor;
    private Drawable messageDrawable;//消息的背景
    /*
    * 按钮
    * */
    private View btnTopLine;//按钮顶部的分割线
    private Button rightBtn;
    private Button leftBtn;
    private String rightBtnStr, leftBtnStr;
    private ColorStateList rightBtnColor;
    private ColorStateList  leftBtnColor;
    private LinearLayout linearLayout_btn;
    private Drawable rightBtnDrawable;//右侧按钮的背景
    private Drawable leftBtnDrawable;//左侧按钮的背景

    /*
    * dialog的一些属性
    * */
    private boolean clickedAnimation;//是否开启动画，即视觉交互效果，开启后，体验更佳
    private int inOutAnimationStyle = 0x00;//动画的样式
    private long ANITIME = 100;//动画效果持续的时间

    private boolean outsideClickable  ;//设置对话框白色区域是否可以点击,默认可以点击
    private boolean cancelable = true;//返回键可用

    private boolean dialogScale;//是否开启缩放
    private Float scaleHeight = 0.3f;//相对于屏幕的宽度的缩放大小
    private Float scaleWidth = 0.75f;

    private Float offPosX;
    private Float offPosY;

    /*
    * 作为toast
    * */
    private Drawable toastDrawable;//整个对话框的背景
    private boolean notToast = true;//是否作为toast显示，默认为“不作为”,以是否缩放为主，其为次


    /*
    * 监听器
    * */
    private OnDoubleBtnClickedListener doubleBtnClickedListener;//左右侧按钮被点击了的监听器
    private OnSingleBtnClickedListener singleBtnClickedListener;//左侧按钮被点击了的监听器
    private OnDownloadApkListener downloadListener;



    /*
    * 下载
    * */
    private String savePath="";
    private boolean openDownloader;//是否打开下载，如果下载链接不为空打开下载
    private String downloadUr;

    private ProgressBar mProgress;
    private TextView updatePercentTextView;
    private TextView updateCurrentTextView;
    private TextView updateTotalTextView;

    private int apkLength;//软件包的大小
    private int apkCurrentDownload;
    private boolean interceptFlag;//下载背用户主动取消
    private Thread downLoadThread;

    private int progress;
    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    updatePercentTextView.setText("已完成 "+progress + "" + "%");
                    try {
                        int currentM, currentK, totalM, totalK;
                        currentM = apkCurrentDownload / 1024 / 1024;
                        currentK = apkCurrentDownload / 1024 - currentM * 1024;

                        totalM = apkLength / 1024 / 1024;
                        totalK = apkLength / 1024 - totalM * 1024;

                        updateCurrentTextView.setText( String.format("%.2f", Double.valueOf(currentM+"."+currentK))  + "MB/");
                        updateTotalTextView.setText( String.format("%.2f", Double.valueOf(totalM+"."+totalK)) + "MB");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    break;
                case DOWN_OVER:
                    rl_download.setVisibility(View.GONE);
                    downloadListener.result(instance);
                    instance.dismiss();
                    rightBtn.setVisibility(View.VISIBLE);
                    instance = new DialogCustom( mContext)
                            .setTitle("下载完成，是否安装？")
                            .setDoubleBtnTextLR("取消","確定")
                            .setOnDoubleBtnClickedListener(new OnDoubleBtnClickedListener() {
                                @Override
                                public void onLeftBtnClick(DialogCustom dialogCustom) {
                                    instance.dismiss();
                                }

                                @Override
                                public void onRightBtnClick(DialogCustom dialogCustom) {
                                    installApk();
                                    instance.dismiss();
                                }
                            });
                    instance.show();

                    break;
                default:
                    break;
            }
        }

        ;
    };

    private Runnable mdownApkRunnable = new Runnable() {
        @Override
        public void run() {
            try {

                File ApkFile = new File(savePath);
                boolean exists = ApkFile.exists();
                if (exists) {
                    ApkFile.delete();
                }
                FileOutputStream fos = new FileOutputStream(ApkFile,false);

                URL url = new URL(downloadUr);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                apkLength = conn.getContentLength();
                System.out.println();
                InputStream is = conn.getInputStream();
                apkCurrentDownload = 0;
                byte buf[] = new byte[1024];
                int length = -1 ;
                while((length = is.read(buf))!=-1){
                    apkCurrentDownload += length;
                    progress = (int) (((float) apkCurrentDownload / apkLength) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    fos.write(buf, 0, length);
                    if (apkCurrentDownload == apkLength) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        instance.dismiss();
                        break;
                    }
                    if(interceptFlag ){
                        ApkFile.delete();
                        instance.dismiss();
                        Looper.prepare();
                        Toast.makeText(mContext, "您取消了下载", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                        break;
                    }
                }
                fos.close();
                is.close();
            }catch (IOException e) {
                e.printStackTrace();
                /*
                * 给用户回调信息
                * */
                downloadListener.failure(e,instance);
            }
        }
    };



    public DialogCustom(Activity context) {
        super(context, R.style.MyTechDialog);
        mContext = context;
        res = mContext.getResources();
        instance = this;
        this.savePath = downloadCachePath;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog);

        initView();
        initDialog();
        initListener();
    }

    private void setInOutAnimation() {
        if (inOutAnimationStyle != 0x00) {
            Window dialogWindow = getWindow();
            dialogWindow.setWindowAnimations(inOutAnimationStyle);//设置对话框的进出效果
        }

    }
    public DialogCustom setOutsideClickable(boolean outsideClickable) {
        this.outsideClickable = outsideClickable;
        return instance;
    }
    public DialogCustom setForbiddenBackKey(boolean cancelable) {
        this.cancelable = cancelable;
        return instance;
    }

    /**
     * 初始化界面控件,并设置界面控件不可见，是否可见，要根据用户的配置值
     */
    private void initView() {
//        linearLayoutDialog = (LinearLayout) findViewById(R.id.linearLayout_dialog);
        linearLayoutTitleMsg = (LinearLayout) findViewById(linearLayout_title_msg);

        /*
        * 标题
        * */
        titleTv = (TextView) findViewById(R.id.title);
        titleTv.setVisibility(View.GONE);

        titleMsgLine = findViewById(R.id.title_msg_line);
        titleMsgLine.setVisibility(View.GONE);
        /*
        * 消息
        * */
        messageTv = (TextView) findViewById(R.id.message_tv);
        messageTv.setVisibility(View.GONE);

        /*
        * 下载控件的父布局
        * */
        rl_download = (RelativeLayout) findViewById(R.id.rl_download);
        rl_download.setVisibility(View.GONE);

        /*
        * 分割线
        * */
        btnTopLine = findViewById(R.id.btn_top_line);
        btnTopLine.setVisibility(View.GONE);

        linearLayout_btn = (LinearLayout) findViewById(R.id.ll_btn);
        linearLayout_btn.setVisibility(View.GONE);
        /*
        * 右侧按钮
        * */
        rightBtn = (Button) findViewById(R.id.btn_right);
        rightBtn.setVisibility(View.GONE);

        /*
        * 左侧按钮、
        * */
        leftBtn = (Button) findViewById(R.id.btn_left);
        leftBtn.setVisibility(View.GONE);
    }

    private void downloadSetting() {
        messageTv.setVisibility(View.GONE);
        titleTv.setText("正在下载…");
        rl_download.setVisibility(View.VISIBLE);
        leftBtn.setText("取消下载");
        rightBtn.setText("后台更新");
        downloadApk();
    }

    /**
     * 初始化界面控件的显示数据
     */
    private void initDialog() {
        setCanceledOnTouchOutside(outsideClickable);
        setCancelable(cancelable);//调用这个方法时，按对话框以外的地方不起作用。按返回键也不起作用
        setInOutAnimation();

        setTitleView();
        setMessageView();
        setBtnView();
        if (dialogScale) {//如果开启缩放,则以缩放为主
            notToast = true;
        }
        if (openDownloader) {
            setDownLoadDialog();
        }
        /*
        * 非吐司的情况下
        * 控制显示的宽高
        * */
        if (notToast) {
            Window dialogWindow = getWindow();
            WindowManager windowManager = mContext.getWindowManager();
            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            int screenWidth = dm.widthPixels;
            int screenHeight = dm.heightPixels;
            WindowManager.LayoutParams winlp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
            winlp.width = (int) (screenWidth * scaleWidth); // 宽度
            winlp.height = (int) (screenHeight * scaleHeight);// 高度

            if (offPosX != null) {
                winlp.x = (int) (screenWidth*offPosX);
            }
            if (offPosY != null) {
                winlp.y = (int) (screenHeight*offPosY);
            }

            dialogWindow.setAttributes(winlp);

        }
    }

    /**
     * 设置title文字
     */
    public DialogCustom setTitle(String title) {
        titleStr = title;
        return this;
    }

    /*
    * 设置title文字大小
    * */
    public DialogCustom setTitleSize(int titleSize){
        this.titleSize = titleSize;
        return this;
    }
    /*
    *设置title文字颜色
    * */
    public DialogCustom setTitleColor(int titleTextColor) {
        this.titleTextColor =  res.getColorStateList(titleTextColor);
        return this;
    }

    /**
     * drawableId :可以是drawable资源文件、color值、一张图片
     */
    public DialogCustom setTitleBackgroundResId( int drawableId){
        titleDrawable = res.getDrawable(drawableId);
        return this;
    }

    /**
     * 设置消息
     */
    public DialogCustom setMessage(String message) {
        if (message != null) {
            messageStr = message;
        }
        return this;
    }
    public DialogCustom setMessageTextSize(int messageSize){
        this.messageSize = messageSize;
        return this;
    }
    public DialogCustom setMessageTextColor(int messageTextColor){
        this.messageTextColor =  res.getColorStateList(messageTextColor);
        return this;
    }

    /*
    * 设置消息背景
    * */
    public DialogCustom setMessageBackgroundResId( int resId){
        messageDrawable = res.getDrawable(resId);
        return this;
    }

    /**
     * 设置按钮
     */
    public DialogCustom setDoubleBtnTextLR(String leftBtnStr,String rightBtnStr) {
        this.leftBtnStr = leftBtnStr;
        setSingleBtnTextR(rightBtnStr);
        return this;
    }
    public DialogCustom setSingleBtnTextR(String rightBtnStr) {
        this.rightBtnStr = rightBtnStr;
        return this;
    }
    public DialogCustom setLeftBtnTextColor(int colorId){
        this.leftBtnColor = res.getColorStateList(colorId);
        return this;
    }
    public DialogCustom setRightBtnTextColor(int colorId){
        this.rightBtnColor = res.getColorStateList(colorId);
        return this;
    }
    public DialogCustom setRightBtnBackgroungRes(int rightBtnDrawableId){
        rightBtnDrawable = res.getDrawable(rightBtnDrawableId);
        return this;
    }
    public DialogCustom setLeftBtnBackgroungRes(int leftBtnDrawableId){
        leftBtnDrawable = res.getDrawable(leftBtnDrawableId);
        return this;
    }

    public DialogCustom setDoubleBtnBackgroungRes(Integer leftBtnDrawableId,Integer rightBtnDrawableId) {
        if (leftBtnDrawableId!=null) {
            leftBtnDrawable = res.getDrawable(leftBtnDrawableId);
        }
        if (rightBtnDrawableId != null) {
            rightBtnDrawable = res.getDrawable(rightBtnDrawableId);
        }

        return this;
    }
    public DialogCustom setSingleBtnBackgroungRes(int leftBtnDrawableId) {
        rightBtnDrawable = res.getDrawable(leftBtnDrawableId);
        return this;
    }
    private void setTitleView() {

        if (!TextUtils.isEmpty(titleStr)) {
            titleTv.setVisibility(View.VISIBLE);
            titleTv.setText(titleStr);
            titleMsgLine.setVisibility(View.VISIBLE);
        }else {
            titleMsgLine.setVisibility(View.GONE);
        }

        /*
        * 设置title文字大小
        * */
        if (titleSize != 0) {
            titleTv.setTextSize(titleSize);
        }
        /*
        * 设置title字体颜色
        * */
        if (titleTextColor != null) {
            titleTv.setTextColor(titleTextColor);
        }
        /*
        * 设置title背景
        * */
        if (titleDrawable!=null) {
            titleTv.setBackground(titleDrawable);
        }
    }
    private void setMessageView() {

        /*
        * 如果没有消息内容
        * */
        if (TextUtils.isEmpty(messageStr)) {
            messageTv.setVisibility(View.GONE);//隐藏消息布局
            return;
        }else {
            messageTv.setText(messageStr);
            messageTv.setVisibility(View.VISIBLE);
            /*
            * 如果作为土司
            * */
            if (TextUtils.isEmpty(titleStr)&&TextUtils.isEmpty(leftBtnStr)&&TextUtils.isEmpty(rightBtnStr)&&!dialogScale) {
                messageTv.setGravity(Gravity.CENTER);
                messageTv.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                        , ViewGroup.LayoutParams.WRAP_CONTENT));//匹配父布局

            }else {
                messageTv.setGravity(Gravity.CENTER);
            }
        }
        if (messageSize != 0) {
            messageTv.setTextSize(messageSize);
        }
          /*
        * 设置消息背景
        * */
        if (messageDrawable!=null) {
            messageTv.setBackground(messageDrawable);
            btnTopLine.setVisibility(View.GONE);
            titleMsgLine.setVisibility(View.GONE);
            linearLayoutTitleMsg.setBackground(toastDrawable);
        }
        if (messageTextColor != null) {
            messageTv.setTextColor(messageTextColor);
        }
    }
    private void setBtnView() {
        /*
        * 如果没有设置左侧和右侧按钮的文字
        * 则视为不使用按钮
        * */
        if (TextUtils.isEmpty(leftBtnStr)&&TextUtils.isEmpty(rightBtnStr)) {
            linearLayout_btn.setVisibility(View.GONE);
            btnTopLine.setVisibility(View.GONE);
            /*
            * 这种情况下可能为toast，则将布局设置设置为弹窗布局
            * */
            if (toastDrawable == null) {
                linearLayoutTitleMsg.setBackground(res.getDrawable(R.drawable.toast_dialog_bg));
            }else {
                linearLayoutTitleMsg.setBackground(toastDrawable);
            }
            return;
        }else if (!TextUtils.isEmpty(leftBtnStr)&&!TextUtils.isEmpty(rightBtnStr)) {//两个都有
            linearLayout_btn.setVisibility(View.VISIBLE);
            btnTopLine.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.VISIBLE);
            rightBtn.setVisibility(View.VISIBLE);

            leftBtn.setText(leftBtnStr);
            if (leftBtnColor != null) {//If you set the color of the left button,
                leftBtn.setTextColor(leftBtnColor);
            }
            if (leftBtnDrawable!=null) {
                leftBtn.setBackground(leftBtnDrawable);
            }

            rightBtn.setText(rightBtnStr);
            if (rightBtnColor != null) {
                rightBtn.setTextColor(rightBtnColor);
            }
            if (rightBtnDrawable!=null) {
                rightBtn.setBackground(rightBtnDrawable);
            }
        }else if (TextUtils.isEmpty(leftBtnStr)&&!TextUtils.isEmpty(rightBtnStr)) {//单个按钮
            linearLayout_btn.setVisibility(View.VISIBLE);
            btnTopLine.setVisibility(View.VISIBLE);
            leftBtn.setVisibility(View.GONE);
            rightBtn.setVisibility(View.VISIBLE);

            rightBtn.setBackground( rightBtnDrawable = res.getDrawable(R.drawable.single_btn_bg));
            rightBtn.setGravity(Gravity.CENTER);//右侧按钮设置居中
            rightBtn.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));//匹配父布局

            rightBtn.setText(rightBtnStr);
            if (rightBtnColor != null) {
                rightBtn.setTextColor(rightBtnColor);
            }
            if (rightBtnDrawable!=null) {
                rightBtn.setBackground(rightBtnDrawable);
            }
        }

    }
    /*
    * 设置宽高的缩放范围
    * 默认情况下：宽度为屏幕的3/4 ；高度为屏幕的1/3
    * */
    public DialogCustom setDialogScale(Float scaleWidth,Float scaleHeight){
        dialogScale = true;
        if (scaleWidth != null) {
            this.scaleWidth = scaleWidth;
        }
        if (scaleHeight != null) {
            this.scaleHeight = scaleHeight;
        }
        return this;
    }

    public DialogCustom setDialogOffPos(Float offPosX,Float offPosY){
        if (offPosX != null) {
            this.offPosX = offPosX;
        }
        if (offPosY != null) {
            this.offPosY = offPosY;
        }
        return this;
    }
    public DialogCustom setToastDrawableId( int drawableId){
        toastDrawable = res.getDrawable(drawableId);
        return this;
    }

    /*
    *设置Toast
    * */
    public DialogCustom setToast(String messageStr,long timeDelay) {
        titleStr = "";
        leftBtnStr = "";
        rightBtnStr = "";
        notToast = false;//是toast,则要调整相应的布局
        if (TextUtils.isEmpty(messageStr)) {
            this.messageStr = "没有提示信息！";
        }else {
            this.messageStr = messageStr;
        }
        if (timeDelay<1000) {
            timeDelay = 1000;
        }

        new Handler().postDelayed(new Runnable(){
            public void run() {
                instance.dismiss();
            }
        }, timeDelay);

        return this;
    }



    public DialogCustom setDownloadBtnText(String cancleStr,String ascertainStr) {
        setDoubleBtnTextLR( cancleStr, ascertainStr);
        return this;
    }
  
    public DialogCustom setClickedAnimation(boolean clickedAnimation){
        this.clickedAnimation = clickedAnimation;
        return this;
    }
    public DialogCustom setInOutAnimationStyle(int  inOutAnimationStyle){
        switch (inOutAnimationStyle){
            case ANIM_SLIDE_UP_DOWN:
                this.inOutAnimationStyle = R.style.slide_up_down;
                break;
            case ANIM_FADE_IN_OUT:
                this.inOutAnimationStyle = R.style.fade_in_out;
                break;
            case ANIM_SCALE_IN_ALPHA_OUT:
                this.inOutAnimationStyle = R.style.scale_in_alpha_out;
                break;
            case ANIM_HYPERSPACE_IN_OUT:
                this.inOutAnimationStyle = R.style.hyperspace_in_out;
                break;
            case ANIM_PUSH_LEFT_IN_OUT:
                this.inOutAnimationStyle = R.style.push_left_in_out;
                break;
            case ANIM_PUSH_UP_IN_OUT:
                this.inOutAnimationStyle = R.style.push_up_in_out;
                break;
            case ANIM_SLIDE_LEFT_RIGHT:
                this.inOutAnimationStyle = R.style.slide_left_right;
                break;
            default:
                this.inOutAnimationStyle = inOutAnimationStyle;
                break;
        }

        return this;
    }
    public DialogCustom setInnerInOutAnimationStyle(int  inOutAnimationStyle){
        this.inOutAnimationStyle = inOutAnimationStyle;
        return this;
    }
    private void setDownLoadDialog() {
        mProgress = (ProgressBar) findViewById(R.id.progress);

        updatePercentTextView = (TextView) findViewById(R.id.updatePercentTextView);
        updateCurrentTextView = (TextView) findViewById(R.id.updateCurrentTextView);
        updateTotalTextView = (TextView) findViewById(R.id.updateTotalTextView);

        updatePercentTextView.setTextColor(res.getColor(R.color.black));
        updateCurrentTextView.setTextColor(res.getColor(R.color.black));
        updateTotalTextView.setTextColor(res.getColor(R.color.black));

    }
    private void matchParent(View view){
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }
    private void matchContent(View view){
        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }


    private void installApk() {
        File apkfile = new File(savePath);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        try {
            if (this!=null&&this.isShowing())
                this.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public DialogCustom setOnDownloadListener(OnDownloadApkListener downloadListener){
//        interceptFlag = true;
        this.downloadListener = downloadListener;
        this.doubleBtnClickedListener = null;
        this.singleBtnClickedListener = null;
        return this;
    }
    /**
     * 下载apk
     *
     * @param
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }


    public DialogCustom setDownloadUrl(String downloadUr,String apkName){

        if (TextUtils.isEmpty(downloadUr)) {
            setMessageTextSize(20).setToast("下载链接为空！",3000).show();
            return this;
        }
        this.downloadUr = downloadUr;
        if (TextUtils.isEmpty(apkName)) {
            savePath = savePath + getAppName(mContext);
        }else {
            savePath = savePath + apkName;
        }
        openDownloader = true;
        return this;
    }

    /*一个按钮时，右侧按钮的监听器*/
    public DialogCustom setOnSingleClicedkListener(OnSingleBtnClickedListener singleBtnClickedListener){
        this.singleBtnClickedListener = singleBtnClickedListener;
        this.doubleBtnClickedListener = null;
        this.downloadListener = null;
        return this;
    }
    /*
    * 两个按钮的监听器*/
    public DialogCustom setOnDoubleBtnClickedListener(OnDoubleBtnClickedListener doubleBtnClickedListener){
        this.doubleBtnClickedListener = doubleBtnClickedListener;
        this.singleBtnClickedListener = null;
        this.downloadListener = null;
        return this;
    }
    /**
     * 初始化界面的监听器
     */
    private void initListener() {
        //设置确定按钮被点击后，向外界提供监听
        rightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (doubleBtnClickedListener != null||singleBtnClickedListener!= null||downloadListener!=null) {
                    if (clickedAnimation) {
                        // 透明度变化
                        AlphaAnimation alphaAni = new AlphaAnimation(0.05f, 1.0f);
                        alphaAni.setDuration(ANITIME);    // 设置动画效果时间
                        v.startAnimation(alphaAni);        // 添加光效动画到VIew

                        alphaAni.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (singleBtnClickedListener!= null) {//执行单个按钮的对话框操作
                                    singleBtnClickedListener.onRightBtnClick(instance);
                                }else if (doubleBtnClickedListener != null) {//执行两个按钮的非下载操作
                                    doubleBtnClickedListener.onRightBtnClick(instance);
                                } else {//不执行回调，直接去下载
                                    if (rightBtn.getText().toString().equals("后台更新")) {
                                        dismiss();
                                    }else {
                                        downloadSetting();
                                    }
                                }
                            }
                            @Override
                            public void onAnimationRepeat(android.view.animation.Animation animation) {

                            }
                        });
                    }else {
                        if (singleBtnClickedListener!= null) {
                            singleBtnClickedListener.onRightBtnClick(instance);
                        }else if (doubleBtnClickedListener != null) {
                            doubleBtnClickedListener.onRightBtnClick(instance);
                        } else {
                            if (rightBtn.getText().toString().equals("后台更新")) {
                                dismiss();
                            }else {
                                downloadSetting();
                            }

                        }
                    }
                }
            }
        });
        //设置取消按钮被点击后，向外界提供监听接口
        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (doubleBtnClickedListener != null||downloadListener!=null) {
                    if (clickedAnimation) {
                        // 透明度变化
                        AlphaAnimation alphaAni = new AlphaAnimation(0.05f, 1.0f);
                        alphaAni.setDuration(ANITIME);      // 设置动画效果时间
                        v.startAnimation(alphaAni);        // 添加光效动画到VIew

                        alphaAni.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {
                            }
                            @Override
                            public void onAnimationEnd(Animation animation) {
                                if (doubleBtnClickedListener != null) {//左侧按钮事件
                                    doubleBtnClickedListener.onLeftBtnClick(instance);
                                }else if (downloadListener != null) {//有更新下载时的左边按钮事件和下载中的取消按钮
                                    instance.dismiss();
                                    interceptFlag =true;
                                    //取消下载，还是在后台下载
                                }

                            }
                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });
                    }else {
                        if (doubleBtnClickedListener != null) {//双按钮的左侧按钮事件
                            doubleBtnClickedListener.onLeftBtnClick(instance);
                        }else if (downloadListener != null) {//下载时的左边按钮事件
                            dismiss();
                        }
                    }
                }
            }
        });
    }

}
