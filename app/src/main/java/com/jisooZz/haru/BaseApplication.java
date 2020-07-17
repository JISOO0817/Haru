package com.jisooZz.haru;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    private static BaseApplication application = null;
    private String userId = null;

    public static BaseApplication getInstance(Context context){
        if (application == null) {
            synchronized (BaseApplication.class) {
                application = new BaseApplication();
            }
        }
        return application;
    }

    public void onCreate() {
        super.onCreate();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
