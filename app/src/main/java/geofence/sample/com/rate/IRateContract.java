package geofence.sample.com.rate;

import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;

import geofence.sample.com.base.BasePresenter;
import geofence.sample.com.base.BaseView;
import geofence.sample.com.model.City;
import geofence.sample.com.prefs.AppPrefManager;

/**
 * Created by sumandas on 04/12/2016.
 */

public interface IRateContract {

    interface ILocationSplashView extends BaseView{
        void onLocationDetected();
        void setUpLocationAnimation();
        void requestLocationPermission();
        void showEnableLocationPermissionScreen();
    }

    interface ILocationPresenter extends BasePresenter {
        void detectLocation();
        void setGoogleApiClient(GoogleApiClient googleApiClient);
        void setAppManager(AppPrefManager appManager);
    }

    interface IRateView extends BaseView{
        void onCitiesLoaded(ArrayList<City> cities);
    }

    interface IRatePresenter extends BasePresenter {
        void startLocationUpdates();
        void removeLocationUpdates();
        void setGoogleApiClient(GoogleApiClient googleApiClient);
        void setAppManager(AppPrefManager appManager);
        void getCities();
        void onCityRated(City city,float rating);

    }

    interface IRateAdapterView{
        void onCityChanged(City city);
    }

}
