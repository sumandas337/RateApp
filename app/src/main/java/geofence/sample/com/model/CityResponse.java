package geofence.sample.com.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sumandas on 04/12/2016.
 */

public class CityResponse {

    @SerializedName("cities")
    public ArrayList<City> cities;
}
