package geofence.sample.com.event;

import geofence.sample.com.model.City;

/**
 * Created by sumandas on 05/12/2016.
 */

public class CityChangedEvent {

    public City mCity;

    public CityChangedEvent(City city){
        mCity=city;
    }
}
