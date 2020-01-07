package com.rj_rncommunication.module;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

import java.util.Map;
import java.util.Objects;

/**
 * 作者：joyce-rao
 * 时间：2020-01-06  17:41
 * 描述：
 * 更改：
 */
public class LiveModule extends ReactContextBaseJavaModule {

    private static final String PROMISE_VALUE = "小明同学";
    private static final String CALL_VALUE = "小王同学";
    private static final String ERROR = "错误：非1";

    private ReactContext reactContext;

    public LiveModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @ReactMethod
    public void pushLive(ReadableMap params) {

    }

    /**
     *原生通过回调的形式返回值
     * @param callback
     * @return
     */
    @ReactMethod
    public void findEvents(Callback successCallback) {
        successCallback.invoke("1", CALL_VALUE);
    }

    /**
     *
     * @param promise
     * @return
     */
    @ReactMethod
    public void liveValue(boolean isOne, Promise promise) {
        if (isOne == true) {
            promise.resolve(PROMISE_VALUE);
        }else {
            promise.reject(ERROR, new Exception());
        }
    }

    @ReactMethod
    public void pushLiveViewController(String name) {
        Intent intent = new Intent(getCurrentActivity(), OtherActivity.class);
        Objects.requireNonNull(getCurrentActivity()).startActivity(intent);

    }

    public void sendEvent(String eventName,
                           @Nullable WritableMap params) {

        this.reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
    }

    /**
     * @return the name of this module. This will be the name used to {@code require()} this module
     * from javascript.
     */
    @NonNull
    @Override
    public String getName() {
        return "LiveModule";
    }

    /**
     *optional
     * @return a map of constants this module exports to JS. Supports JSON types.
     */
    @Nullable
    @Override
    public Map<String, Object> getConstants() {
        return super.getConstants();
    }
}
