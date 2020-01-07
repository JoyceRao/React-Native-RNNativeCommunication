package com.rj_rncommunication.module;

import androidx.annotation.NonNull;

import com.facebook.react.ReactPackage;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.uimanager.ViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 作者：joyce-rao
 * 时间：2020-01-06  17:53
 * 描述：
 * 更改：
 */
public class LivePackage implements ReactPackage {



    private LiveModule liveModule;



    /**
     * @param reactContext react application context that can be used to create modules
     * @return list of native modules to register with the newly created catalyst instance
     */
    @NonNull
    @Override
    public List<NativeModule> createNativeModules(@NonNull ReactApplicationContext reactContext) {
        List<NativeModule> nativeModules = new ArrayList<>();
        this.liveModule = new LiveModule(reactContext);
        //注册这个模块
        nativeModules.add(liveModule);
        return nativeModules;
    }

    /**
     * @param reactContext
     * @return a list of view managers that should be registered with {@link UIManagerModule}
     */
    @NonNull
    @Override
    public List<ViewManager> createViewManagers(@NonNull ReactApplicationContext reactContext) {
        return Collections.emptyList();
    }


    public LiveModule getLiveModule() {
        return liveModule;
    }
}
