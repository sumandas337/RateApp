package geofence.sample.com.rate.inject;

import android.content.Context;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import geofence.sample.com.rate.presenter.LocationSplashPresenter;
import geofence.sample.com.rate.presenter.RatePresenter;

/**
 * Created by sumandas on 04/12/2016.
 */
@Module
public class RateModule {

    private Context mContext;

    public RateModule(Context context){
        mContext=context;
    }

    @Provides
    LocationSplashPresenter providesLocationSplashPresenter(){
        return new LocationSplashPresenter(mContext);
    }

    @Provides
    RatePresenter providesRatePresenter(){
        return new RatePresenter(mContext);
    }



    @Provides
    @Named("rate")
    GoogleApiClient providesRateClient(RatePresenter ratePresenter){
        return new GoogleApiClient.Builder(mContext)
                .addConnectionCallbacks(ratePresenter)
                .addOnConnectionFailedListener(ratePresenter)
                .addApi(LocationServices.API)
                .build();
    }

}
