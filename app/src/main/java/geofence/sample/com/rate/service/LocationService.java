package geofence.sample.com.rate.service;

import android.app.IntentService;
import android.content.Intent;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.LocationResult;

import javax.inject.Inject;

import geofence.sample.com.application.RateApplication;
import geofence.sample.com.event.CityChangedEvent;
import geofence.sample.com.model.City;
import geofence.sample.com.prefs.AppPrefManager;
import geofence.sample.com.RxBus;

/**
 * Created by sumandas on 04/12/2016.
 */

public class LocationService extends IntentService {

    private final String TAG = "LocationService";

    @Inject
    AppPrefManager mAppPrefManager;

    @Inject
    RxBus mRxBus;

    public LocationService() {
        super("LocationService");
        (RateApplication.getAppContext()).getmApplicationComponent().injectLocationService(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (LocationResult.hasResult(intent)) {
            LocationResult locationResult = LocationResult.extractResult(intent);
            Location location = locationResult.getLastLocation();
            if (location != null) {
                Log.d(TAG, location.getLatitude() + " " + location.getLongitude());
                mAppPrefManager.setLatitude(location.getLatitude());
                mAppPrefManager.setLongitude(location.getLongitude());
                City city=City.getCityForLocation(location.getLatitude(),location.getLongitude());
               mRxBus.postEvent(new CityChangedEvent(city));
            }
        }
    }
}