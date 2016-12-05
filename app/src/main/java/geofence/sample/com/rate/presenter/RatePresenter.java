package geofence.sample.com.rate.presenter;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import geofence.sample.com.RxUtils;
import geofence.sample.com.base.BaseView;
import geofence.sample.com.model.City;
import geofence.sample.com.net.RestClient;
import geofence.sample.com.prefs.AppPrefManager;
import geofence.sample.com.rate.IRateContract;
import geofence.sample.com.rate.service.LocationService;

/**
 * Created by sumandas on 04/12/2016.
 */

public class RatePresenter implements IRateContract.IRatePresenter,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private Context mContext;

    private GoogleApiClient mGoogleApiClient;

    private AppPrefManager mAppPrefManager;

    private PendingIntent mRequestLocationUpdatesPendingIntent;

    private IRateContract.IRateView mView;


    private RestClient mRestClient;

    public RatePresenter(Context context){
        mContext=context;
    }

    @Override
    public void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationRequest locationUpdateRequest = new LocationRequest();
                locationUpdateRequest.setInterval(1000 * 60 * 5);
                locationUpdateRequest.setFastestInterval(1000 * 60);
                locationUpdateRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                locationUpdateRequest.setSmallestDisplacement(50);

                Intent mRequestLocationUpdatesIntent = new Intent(mContext, LocationService.class);
                mRequestLocationUpdatesPendingIntent = PendingIntent.getService(mContext.getApplicationContext(), 0,
                        mRequestLocationUpdatesIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                        locationUpdateRequest,
                        mRequestLocationUpdatesPendingIntent);
            }
        }

    }

    @Override
    public void removeLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, mRequestLocationUpdatesPendingIntent);
    }

    @Override
    public void getCities() {
        if(!mAppPrefManager.isAppCitiesSync()){
            RxUtils.build(mRestClient.getCities()).subscribe(cityResponse -> {
                for(City city:cityResponse.cities){
                    city.save();
                }
                mAppPrefManager.setAppCitiesSync(true);
                City.setWithinCity(cityResponse.cities,mAppPrefManager.getLatitude(),mAppPrefManager.getLongitude());
                mView.onCitiesLoaded(cityResponse.cities);
            });
        }else{
            ArrayList<City> cities=City.getAllCities();
            City.setWithinCity(cities,mAppPrefManager.getLatitude(),mAppPrefManager.getLongitude());
            mView.onCitiesLoaded(cities);
        }
    }


    @Override
    public void onCityRated(City city,float rating) {
        city.mRating=rating;
        city.save();
    }

    @Override
    public void setMvpView(BaseView baseView) {
        mView=(IRateContract.IRateView)baseView;
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient =googleApiClient;
    }

    public void setAppManager(AppPrefManager appPrefManager) {
        mAppPrefManager = appPrefManager;
    }

    public void setRestClient(RestClient restClient) {
        mRestClient = restClient;
    }

    @Override
    public void create() {
        mGoogleApiClient.connect();
        getCities();
    }

    @Override
    public void destroy() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }

    @Override
    public BaseView getView() {
        return mView;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d("Location Services","google play connected");
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {

        Log.d("Location Services","google play suspended");
        removeLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Location Services","google play connection failed");
    }

}
