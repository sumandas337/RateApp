package geofence.sample.com.application;

import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

/**
 * Created by sumandas on 04/12/2016.
 */

public class RateApplication extends Application {

    private AppComponent mAppComponent;


    private static Context mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp=getApplicationContext();
        initAppComponent();
        initDbFlow();

    }

    public void initAppComponent() {
     mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public AppComponent getmApplicationComponent() {
        return mAppComponent;
    }

    public void initDbFlow() {
        FlowManager.init(new FlowConfig.Builder(this)
                .openDatabasesOnInit(true).build());
    }


    public static RateApplication getAppContext() {
        return (RateApplication) mApp;
    }


}
