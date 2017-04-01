可以自定义风格，还可以用它实现下载可以自定义进入退出动画等

	使用方法在后面
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/1.jpg)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/nullxz.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/2.jpg)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/3.jpg)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/111.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/112.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/xiazai.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/xiazai2.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/xiazai3.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/toast.png)

	新版本的对话框封装了 下载功能 
	Gradle:api要求>=19
    compile 'com.smart.dialog_library:dialog_lib:1.1.3'


activity的代码：(有些老了，可以不看，建议下载运行demo)

	import android.content.Context;
	import android.os.Bundle;
	import android.support.v7.app.AppCompatActivity;
	import android.util.Log;
	import android.view.View;
	import android.widget.Button;

	import com.smart.dialog_library.DialogCustom;
	import com.smart.dialog_library.callback.OnDownloadApkListener;

	public class MainActivity extends AppCompatActivity {
    private DialogCustom doubleBtn;
    private DialogCustom singleBtn;
    private Context mContext;
    private String apkUrl = "http://hiao.com/android/bus/QingDaoBus.apk";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        final Button button = (Button) findViewById(R.id.self_dialog);
        final Button button1 = (Button) findViewById(R.id.self_dialog1);
        initDialog();
        initDialog1();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singleBtn.show();
                Log.i("TAG", "onClick: ");
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doubleBtn.show();
            }
        });
	}

	 private void initDialog1() {
        doubleBtn = new DialogCustom(this)
                  .setTitle("您确定要放弃支付么？")
                  .setTitleSize(0)//设置标题大小
                  .setTitleColor(R.color.colorAccent)
                  .setDoubleBtnText("取消","确定")
                  .setOutsideClickable(true)
                  .setMessage("温馨提醒:\n\n  尽快完成支付有助于后续提供服务\n温馨提醒:\n" )
                  .setDialogScale(0.75f,0.485f)//设置对话框大小
                  .setDialogOffPos(0.0f,0.5f)//设置相对于屏幕中心的偏移位置
                  .setLeftBtnTextColor(R.color.colorAccent)
                  .setRightBtnTextColor(R.color.colorPrimary)
                  .setClickedAnimation(true)//开启动画
                  .setInOutAnimationStyle(R.style.dialogWindowAnim)
                 .setSingleBtnText("ww")
                 .build();
    	}

	//注意:下面的功能主要为了实现apk的现在，所在.setDownloadUrl(apkUrl,"你的apk名字.apk");
	//这样，下载后的文件，对系统来说，是可识别的。
    private void initDialog() {
        singleBtn = new DialogCustom(this)
                .setTitle("您确定要下载么？")
                .setTitleBackGroundResId(R.drawable.title_bg)
                .setDownloadBtnText("取消","确定")
                .setMessage("更新提示信息\n跟新内容")
                .setDownloadUrl(apkUrl,"")
                .setClickedAnimation(true)
                .setOnDownloadListener(new OnDownloadApkListener() {
                    @Override
                    public void result(String result) {
						//完成后的操作，只是提供一个完成后的接口，目前意义不大，每为以后封装文件下载用
                    }
                    @Override
                    public void failure(Exception e) {
						//下载失败的回调
                    }
                }).build();
    	}
	}

布局文件

	<?xml version="1.0" encoding="utf-8"?>
	<LinearLayout
	    xmlns:android="http://schemas.android.com/apk/res/android"
	    android:layout_width="match_parent"
	    android:layout_height="match_parent"
	    android:paddingBottom="@dimen/activity_vertical_margin"
	    android:paddingLeft="@dimen/activity_horizontal_margin"
	    android:paddingRight="@dimen/activity_horizontal_margin"
	    android:paddingTop="@dimen/activity_vertical_margin"
	    >
	
	    <Button
	        android:id="@+id/self_dialog"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_centerInParent="true"
	        android:text="自定义Dialog"/>
	    <Button
	        android:id="@+id/self_dialog1"
	        android:layout_width="wrap_content"
	        android:layout_height="wrap_content"
	        android:layout_centerInParent="true"
	        android:text="自定义Dialog1"/>
	</LinearLayout>

可以自定义风格：在drawable下定义一个xml文件 title_bg.xml

	<?xml version="1.0" encoding="utf-8"?>
	<shape xmlns:android="http://schemas.android.com/apk/res/android">
	
	    <solid android:color="#C7C7C7" />
	    <stroke
	        android:width="0.8dp"
	        android:color="#ffffff" />
	    <!-- 圆角 -->
	    <corners
	        android:topLeftRadius="10dp"
	        android:topRightRadius="10dp"
	        />
	
	</shape>


进入和退出的动画
1、新建anim资源目录，并在该目录下新建两个文件

	slide_up_in.xml

	<?xml version="1.0" encoding="utf-8"?>
	<set android:interpolator="@android:anim/decelerate_interpolator"
		xmlns:android="http://schemas.android.com/apk/res/android">
		<translate android:duration="2000"
			android:fromYDelta="100.0%p" android:toYDelta="0.0" />
	</set>
	
	
	slide_down_out.xml
	<?xml version="1.0" encoding="utf-8"?>
	<set android:interpolator="@android:anim/accelerate_interpolator"
		xmlns:android="http://schemas.android.com/apk/res/android">
		<translate android:duration="2000"
			android:fromYDelta="0.0" android:toYDelta="100.0%p" />
	</set>
	


2、在style.xml文件中

	<style name="dialogWindowAnim" parent="android:Animation" mce_bogus="1">
        <item name="android:windowEnterAnimation">@anim/slide_up_in</item>
        <item name="android:windowExitAnimation">@anim/slide_down_out</item>
    </style>
清单文件

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	注意6.0以上的设备需要动态获取运行时权限。
