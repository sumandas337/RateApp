package geofence.sample.com.application;

import geofence.sample.com.net.RestClient;
import geofence.sample.com.prefs.AppPrefManager;
import geofence.sample.com.rate.service.LocationService;
import geofence.sample.com.RxBus;

/**
 * Created by sumandas on 04/12/2016.
 */
public interface IAppComponent {

    RestClient getRestClient();

    RxBus getRxBus();

    AppPrefManager getAppManager();

    void injectLocationService(LocationService locationService);
}
