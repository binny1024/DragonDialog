package com.smart.dialog_library;

import android.os.Environment;

import java.io.File;

/**
 * Created by xu on 2017/3/3.
 */

public final class DragonConstant {
	public static final String downloadCachePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
			+  "Download/";
	public static final String downloadCachePath21 = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator
			+ "Download/";

	public final static int ANIM_SLIDE_UP_DOWN = 0x01;
	public final static int ANIM_FADE_IN_OUT = 0x02;
	public final static int ANIM_SCALE_IN_ALPHA_OUT = 0x03;
	public final static int ANIM_HYPERSPACE_IN_OUT = 0x04;
	public final static int ANIM_PUSH_LEFT_IN_OUT = 0x05;
	public final static int ANIM_PUSH_UP_IN_OUT = 0x06;
	public final static int ANIM_SLIDE_LEFT_RIGHT = 0x07;
}
