package com.smart.dialog_library.callback;

import com.smart.dialog_library.DialogCustom;

/**
 * Created by xu on 2017/3/11.
 */

public interface OnDownloadApkListener {
    void result(DialogCustom dialogCustom);
    void failure(Exception e,DialogCustom dialogCustom);//给外界一个接口，返回用户错误信息
}
