package com.ryan.corelibs;

import android.app.Application;
import android.os.Build;
import android.os.StrictMode;

import com.corelibs.api.ApiFactory;
import com.corelibs.common.Configuration;
import com.corelibs.exception.GlobalExceptionHandler;
import com.corelibs.utils.PreferencesHelper;
import com.corelibs.utils.ToastMgr;
import com.ryan.corelibs.constants.Urls;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        GlobalExceptionHandler.getInstance().init(this, getResources().getString(R.string.app_name)); //初始化全局异常捕获
        ToastMgr.init(getApplicationContext()); //初始化Toast管理器
        Configuration.enableLoggingNetworkParams(); //打开网络请求Log打印，需要在初始化Retrofit接口工厂之前调用
        ApiFactory.getFactory().add(Urls.GITHUB_API); //初始化Retrofit接口工厂
        PreferencesHelper.init(getApplicationContext()); //初始化SharedPreferences工具类

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            // android 7.0系统解决拍照报exposed beyond app through ClipData.Item.getUri()
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
    }
}
