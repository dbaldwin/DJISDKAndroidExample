package com.unmannedairlines.djisdkandroidexample;
import android.app.Application;
import android.content.Context;
import com.secneo.sdk.Helper;

import dji.sdk.base.BaseProduct;
import dji.sdk.sdkmanager.DJISDKManager;

/**
 * Created by db on 5/14/18.
 */

public class MApplication extends Application {

    private static BaseProduct product;
    private static Application app = null;

    @Override
    protected void attachBaseContext(Context paramContext) {
        super.attachBaseContext(paramContext);
        Helper.install(MApplication.this);
        app = this;
    }

    public static Application getInstance() {
        return MApplication.app;
    }

    public static synchronized BaseProduct getProductInstance() {
        if (null == product) {
            product = DJISDKManager.getInstance().getProduct();
        }
        return product;
    }

}
