package geofence.sample.com.rate.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

import javax.inject.Inject;

import geofence.sample.com.R;
import geofence.sample.com.RxBus;
import geofence.sample.com.RxUtils;
import geofence.sample.com.application.RateApplication;
import geofence.sample.com.event.CityChangedEvent;
import geofence.sample.com.model.City;
import geofence.sample.com.net.RestClient;
import geofence.sample.com.prefs.AppPrefManager;
import geofence.sample.com.rate.IRateContract;
import geofence.sample.com.rate.adapter.RateCityAdapter;
import geofence.sample.com.rate.inject.DaggerRateComponent;
import geofence.sample.com.rate.inject.RateComponent;
import geofence.sample.com.rate.inject.RateModule;
import geofence.sample.com.rate.presenter.RatePresenter;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sumandas on 04/12/2016.
 */
public class RateActivity extends AppCompatActivity  implements IRateContract.IRateView,RateCityAdapter.CityRatedListener{

    @Inject
    RatePresenter mRatePresenter;

    GoogleApiClient mGoogleApiClient;

    @Inject
    AppPrefManager mAppManager;

    @Inject
    RestClient mRestClient;

    @Inject
    RxBus mRxBus;

    private RecyclerView mRatingList;

    private RateCityAdapter mAdapter;

    private CompositeSubscription mSubscription=new CompositeSubscription();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRatingList=(RecyclerView)findViewById(R.id.city_rating_list);
        setUpCityList();
        initRateComponent();

        mGoogleApiClient=providesRateClient(mRatePresenter);
        mRatePresenter.setGoogleApiClient(mGoogleApiClient);
        mRatePresenter.create();

        mSubscription.add(RxUtils.build(mRxBus.toObservable()).subscribe(event -> {
            if(event instanceof CityChangedEvent){
                Log.d("RateActivity","city changed event");
                City city=((CityChangedEvent)event).mCity;
                mAdapter.onCityChanged(city);
            }
        }));

    }

    public void initRateComponent(){
        RateComponent rateComponent= DaggerRateComponent.builder()
                .appComponent(((RateApplication)getApplication()).getmApplicationComponent())
                .rateModule(new RateModule(this))
                .build();
        rateComponent.injectRate(this);
    }

    GoogleApiClient providesRateClient(RatePresenter ratePresenter){
        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(ratePresenter)
                .addOnConnectionFailedListener(ratePresenter)
                .addApi(LocationServices.API)
                .build();
    }


    @Inject
    public void setUp() {
        mRatePresenter.setMvpView(this);

        mRatePresenter.setAppManager(mAppManager);
        mRatePresenter.setRestClient(mRestClient);
    }

    public void setUpCityList() {
        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        layoutManager1.setOrientation(LinearLayoutManager.VERTICAL);
        mRatingList.setLayoutManager(layoutManager1);
        mRatingList.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mRatePresenter.destroy();
        RxUtils.unSubscribe(mSubscription);
    }

    @Override
    public void onCitiesLoaded(ArrayList<City> cities) {
        mAdapter = new RateCityAdapter(this, cities, this);
        mRatingList.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onCityRated(City city, float rating) {
        mRatePresenter.onCityRated(city,rating);
    }
}
