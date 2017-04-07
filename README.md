
案例展示

![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/11.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/22.png)

一个按钮的使用图示

![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/111.png)

两个按钮的使用图示

![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/112.png)

下载

![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/xiazai.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/xiazai2.png)
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/xiazai3.png)

空下载链接

![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/nullxz.png)

toast

![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/toast.png)



缩放演示
![](https://github.com/xubinbin1024/DragonDialog/blob/master/gif/scale.png)


可以自定义风格，还可以用它实现下载可以自定义进入退出动画等,可定制大小和位置

    新版本的对话框封装了 下载功能
	Gradle:api要求>=19
    compile 'com.smart.dialog_library:dialog_lib:1.1.5'

两个按钮

    new DialogCustom(this).setDoubleBtnTextLR("取消", "确定")
                    .setTitle("设置了背景色")
                    .setTitleBackgroundResId(R.drawable.title_bg)
                    .setMessage("设置了背景色")
                    .setMessageBackgroundResId(R.drawable.msg_bg)
                    .setOnDoubleBtnClickedListener(new OnDoubleBtnClickedListener() {
                        @Override
                        public void onLeftBtnClick(DialogCustom dialogCustom) {
                             //取消按钮的事件
                             new DialogCustom(MainActivity.this)
                                     .setToast("取消", 2000)
                                     .setToastDrawableId(R.drawable.dialog_bg)
                                     .show();
                            dialogCustom.dismiss();
                        }
                        @Override
                        public void onRightBtnClick(DialogCustom dialogCustom) {
                          //确定按钮的事件
                           new DialogCustom(MainActivity.this)
                                  .setToast("确定确定确定确定确定确定确定确定确定确定确定确定", 2000)
                                  .setDialogScale(0.5f,0.5f)//开启缩放，则以缩放为主，若没有开启，则包裹内容
                                  .setToastDrawableId(R.drawable.dialog_bg)
                                  .show();
                            dialogCustom.dismiss();
                        }
                    }).show();

一个按钮

     new DialogCustom(this).setTitle("默认背景色")
                    .setMessage("默认背景色")
                    .setSingleBtnTextR("one")
                    .setOnSingleClicedkListener(new OnSingleBtnClickedListener() {
                        @Override
                        public void onRightBtnClick(DialogCustom dialogCustom) {
                            dialogCustom.dismiss();
                        }
                    })
                    .show();
Toast

        new DialogCustom(this)
                .setToast("发撒好方法放假还是发生了疯狂就爱疯了",2000)
                .setToastDrawableId(R.drawable.dialog_bg)
                .setDialogOffPos(null,0.2f)
                .show();

一、可以自定义背景建议使用drawbale绘制背景 在drawable下定义一个xml文件

1、右侧按钮的背景 right_btn_bg.xml

    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android">
        <!--背景色-->
        <solid android:color="#FFC000" />
        <!-- 圆角 -->
        <corners
            android:bottomRightRadius="20dp"
         />
    </shape>
2、左侧按钮的背景 left_btn_bg.xml

    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android">
        <!--背景色-->
        <solid android:color="#C0C0C0" />
        <!-- 圆角 -->
        <corners
            android:bottomLeftRadius="20dp"
        />
    </shape>
3、Toast弹窗的背景 dialog_bg.xml(就是整个对话框的背景)

    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android">
        <solid android:color="#80FFC0" />
        <stroke
            android:width="0.8dp"
            android:color="#80FFC0" />
        <!-- 圆角 -->
        <corners
            android:topLeftRadius="10dp"
            android:topRightRadius="10dp"
            android:bottomLeftRadius="20dp"
            android:bottomRightRadius="20dp"/>
    </shape>
4、消息的背景色 msg.xml

    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android">
        <solid android:color="#80FFC0" />
        <stroke
         android:width="0.8dp"
         ndroid:color="#80FFC0" />
    </shape>

5、标题的背景 title_bg.xml

    <?xml version="1.0" encoding="utf-8"?>
    <shape xmlns:android="http://schemas.android.com/apk/res/android">
        <solid android:color="#FF8080" />
        <stroke
            android:width="1dp"
            android:color="#656565" />
        <!-- 圆角 -->
        <corners
            android:topLeftRadius="10dp"
            android:topRightRadius="10dp"
            />
    </shape>

二、进入和退出的动画


1、使用内置的动画效果

	ANIM_SLIDE_UP_DOWN <!--上下交错效果-->
	ANIM_FADE_IN_OUT <!--淡入淡出效果-->
	ANIM_SCALE_IN_ALPHA_OUT  <!--放大淡出效果-->
	ANIM_HYPERSPACE_IN_OUT  <!--压缩变小淡出效果-->
	ANIM_PUSH_LEFT_IN_OUT <!--右往左推出效果-->
	ANIM_PUSH_UP_IN_OUT  <!--下往上推出效果-->
	ANIM_SLIDE_LEFT_RIGHT <!--左右交错效果-->

2、新建anim资源目录，并在该目录下新建两个文件

2.1 slide_up_in.xml

	<?xml version="1.0" encoding="utf-8"?>
	<set  android:interpolator="@android:anim/decelerate_interpolator"
		    xmlns:android="http://schemas.android.com/apk/res/android">
		<translate android:duration="2000"
			android:fromYDelta="100.0%p" android:toYDelta="0.0" />
	</set>
	
	
2.2 slide_down_out.xml

	<?xml version="1.0" encoding="utf-8"?>
	<set android:interpolator="@android:anim/accelerate_interpolator"
		xmlns:android="http://schemas.android.com/apk/res/android">
		<translate android:duration="2000"
			android:fromYDelta="0.0" android:toYDelta="100.0%p" />
	</set>
	


2.3、在style.xml文件中

	<style name="dialogWindowAnim" parent="android:Animation" mce_bogus="1">
        <item name="android:windowEnterAnimation">@anim/slide_up_in</item>
        <item name="android:windowExitAnimation">@anim/slide_down_out</item>
    </style>


清单文件

    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
	注意6.0以上的设备需要动态获取运行时权限。
