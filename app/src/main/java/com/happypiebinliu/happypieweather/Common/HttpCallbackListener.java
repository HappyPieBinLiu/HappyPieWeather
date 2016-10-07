package com.happypiebinliu.happypieweather.Common;

/**
 * Created by ope001 on 2016/10/07.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
