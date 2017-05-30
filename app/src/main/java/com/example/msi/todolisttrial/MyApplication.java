package com.example.msi.todolisttrial;

import io.skygear.skygear.SkygearApplication;

/**
 * Created by MSI on 2017/5/27.
 */

public class MyApplication extends SkygearApplication {
    @Override
    public String getSkygearEndpoint() {
        return "https://todolisttrial.skygeario.com/";
    }

    @Override
    public String getApiKey() {
        return "368cc7af10c34fb1b7d3eb330d17ea48";
    }
}
