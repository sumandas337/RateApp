package geofence.sample.com.rate.inject;

import dagger.Component;
import geofence.sample.com.application.AppComponent;
import geofence.sample.com.application.PerActivity;
import geofence.sample.com.rate.activity.LocationSplashActivity;
import geofence.sample.com.rate.activity.RateActivity;

/**
 * Created by sumandas on 04/12/2016.
 */
@PerActivity
@Component(dependencies = AppComponent.class,modules = {RateModule.class})
public interface RateComponent {

    void injectSplash(LocationSplashActivity locationSplashActivity);
    void injectRate(RateActivity rateActivity);
}
