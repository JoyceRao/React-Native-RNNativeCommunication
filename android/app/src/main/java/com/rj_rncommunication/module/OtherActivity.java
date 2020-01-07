package com.rj_rncommunication.module;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.WritableMap;
import com.rj_rncommunication.MainApplication;
import com.rj_rncommunication.R;

/**
 * 作者：joyce-rao
 * 时间：2020-01-07  09:57
 * 描述：
 * 更改：
 */
public class OtherActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otherlayout);
    }

    public void touchClick(View view) {
        WritableMap map = Arguments.createMap();
        map.putString("message", "123456");
        MainApplication.getLivePackage().getLiveModule().sendEvent("EventName", map);
        finish();
    }
}
