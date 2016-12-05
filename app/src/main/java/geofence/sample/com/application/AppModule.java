package geofence.sample.com.application;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import geofence.sample.com.prefs.AppPrefManager;
import geofence.sample.com.RxBus;

/**
 * Created by sumandas on 04/12/2016.
 */
@Module
public class AppModule {

    private Context mContext;

    public AppModule(Context context){
        mContext=context;
    }

    @Singleton
    @Provides
    RxBus providesRxBus(){
        return RxBus.getInstance();
    }

    @Singleton
    @Provides
    AppPrefManager providesAppPrefManager() {
        return AppPrefManager.initializeManager(mContext);
    }
}
