package geofence.sample.com.rate.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Named;

import geofence.sample.com.R;
import geofence.sample.com.application.RateApplication;
import geofence.sample.com.prefs.AppPrefManager;
import geofence.sample.com.rate.IRateContract;
import geofence.sample.com.rate.inject.DaggerRateComponent;
import geofence.sample.com.rate.inject.RateComponent;
import geofence.sample.com.rate.inject.RateModule;
import geofence.sample.com.rate.presenter.LocationSplashPresenter;
import geofence.sample.com.PermissionUtils;

/**
 * Created by sumandas on 04/12/2016.
 */

public class LocationSplashActivity extends AppCompatActivity implements IRateContract.ILocationSplashView{

    private static final int REQUEST_LOCATION_PERMISSION = 10219;

    @Inject
    LocationSplashPresenter mSplashPresenter;

    GoogleApiClient mGoogleApiClient;

    @Inject
    AppPrefManager mAppManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_splash);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        initSplashComponent();

        mGoogleApiClient=providesSplashClient(mSplashPresenter);
        mSplashPresenter.setGoogleApiClient(mGoogleApiClient);

        mSplashPresenter.create();

    }

    public void initSplashComponent(){
       RateComponent rateComponent= DaggerRateComponent.builder()
                .appComponent(((RateApplication)getApplication()).getmApplicationComponent())
                .rateModule(new RateModule(this))
                .build();
        rateComponent.injectSplash(this);
    }

    @Inject
    public void setUp() {
        mSplashPresenter.setMvpView(this);
        mSplashPresenter.setAppManager(mAppManager);

    }

    GoogleApiClient providesSplashClient(LocationSplashPresenter locationSplashPresenter){
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(locationSplashPresenter)
                .addOnConnectionFailedListener(locationSplashPresenter)
                .addApi(LocationServices.API)
                .build();
    }



    @Override
    public void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_LOCATION_PERMISSION);
    }

    @Override
    @TargetApi(23)
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        mSplashPresenter.detectLocation();
                    } else if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permissions[0]);
                        String btnText;
                        if (!showRationale) {
                            btnText = getString(R.string.go_to_settings);
                        } else {
                            btnText = getString(R.string.btn_allow);
                        }
                        showGiveLocationPermissionsDialog(btnText);
                    }
                    break;
                }
        }
    }

    @Override
    public void showEnableLocationPermissionScreen() {
        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        finish();
    }


    @Override
    public void onLocationDetected() {
        Intent intent=new Intent(this,RateActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSplashPresenter.destroy();
    }


    @Override
    public void setUpLocationAnimation() {
        ImageView mLocationIcon = (ImageView) findViewById(R.id.location_iv);
        Animation mAnimation = new TranslateAnimation(0, 0, 0, 50);
        mAnimation.setDuration(2000);
        mAnimation.setRepeatCount(-1);
        mAnimation.setRepeatMode(Animation.REVERSE);
        mAnimation.setInterpolator(new LinearInterpolator());
        mLocationIcon.setAnimation(mAnimation);
    }

    public void showGiveLocationPermissionsDialog(String btnText) {
        Activity activity = this;

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(activity, android.R.style.Theme_Material_Light_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(activity);
        }
        builder.setMessage(R.string.permissions_denied_dialog_msg)
                .setPositiveButton(btnText, (dialogInterface, i) -> {
                    if (btnText.equals(getString(R.string.btn_allow)))
                        requestLocationPermission();
                    else if (btnText.equals(getString(R.string.go_to_settings)))
                        goToAppSettingsPage();
                })
                .setCancelable(false)
                .show();
    }

    public void goToAppSettingsPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }


}
