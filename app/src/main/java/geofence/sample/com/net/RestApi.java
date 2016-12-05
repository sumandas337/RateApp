package geofence.sample.com.net;

import geofence.sample.com.model.CityResponse;
import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by sumandas on 04/12/2016.
 */

public interface RestApi {

    @GET("http://www.mocky.io/v2/58451e41110000281b0e6bd7")
    Observable<CityResponse> getCities();

}
