package geofence.sample.com.rate.presenter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationAvailability;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import geofence.sample.com.base.BaseView;
import geofence.sample.com.prefs.AppPrefManager;
import geofence.sample.com.rate.IRateContract;
import geofence.sample.com.LocationUtils;
import geofence.sample.com.PermissionUtils;

/**
 * Created by sumandas on 04/12/2016.
 */

public class LocationSplashPresenter implements IRateContract.ILocationPresenter,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private IRateContract.ILocationSplashView mView;

    private Context mContext;

    private GoogleApiClient mGoogleApiClient;

    private AppPrefManager mAppPrefManager;

    public LocationSplashPresenter(Context context){
        mContext=context;
    }


    @Override
    public void setMvpView(BaseView baseView) {
        mView=(IRateContract.ILocationSplashView) baseView;
    }

    @Override
    public void detectLocation() {
        if(!LocationUtils.isLocationEnabled(mContext)){
            mView.showEnableLocationPermissionScreen();
            return;
        }
        if (PermissionUtils.hasNoPermissionToAccessLocation(mContext)) {
            mView.requestLocationPermission();
        } else {
            startLocationUpdates();
        }
    }

    public void setGoogleApiClient(GoogleApiClient googleApiClient) {
        mGoogleApiClient =googleApiClient;
    }

    @Override
    public void setAppManager(AppPrefManager appManager) {
        mAppPrefManager=appManager;
    }


    public void create() {
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();

        }
        mView.setUpLocationAnimation();
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
        detectLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d("Location Services","google play suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Location Services","google play connection failed");
    }

    public void startLocationUpdates() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected())
            if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                LocationRequest locationRequest = LocationRequest.create()
                        .setNumUpdates(1)
                        .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                        .setInterval(0);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult result) {
                        Location location = result.getLastLocation();
                        mAppPrefManager.setLatitude(location.getLatitude());
                        mAppPrefManager.setLongitude(location.getLongitude());
                        mView.onLocationDetected();
                    }

                    @Override
                    public void onLocationAvailability(LocationAvailability locationAvailability) {
                        Log.d("onLocationAvailability ", "" + locationAvailability.isLocationAvailable());
                    }
                }, null);

            }

    }

}
