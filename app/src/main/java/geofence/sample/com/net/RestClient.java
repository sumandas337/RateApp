package geofence.sample.com.net;

import geofence.sample.com.model.CityResponse;
import rx.Observable;

/**
 * Created by sumandas on 04/12/2016.
 */

public class RestClient {

    private RestApi mRestApi;

    public RestClient(RestApi restApi){
        mRestApi=restApi;
    }

    public Observable<CityResponse> getCities() {
        return mRestApi.getCities();
    }
}
