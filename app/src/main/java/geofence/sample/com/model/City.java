package geofence.sample.com.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.util.ArrayList;
import java.util.List;

import geofence.sample.com.database.RateDatabase;
import geofence.sample.com.LocationUtils;

/**
 * Created by sumandas on 04/12/2016.
 */
@Table(database = RateDatabase.class)
public class City extends BaseModel implements Parcelable{

    @PrimaryKey
    @Column
    @SerializedName("name")
    public String mCityName;

    @Column
    @SerializedName("description")
    public String mCityDesc;

    @Column
    @SerializedName("latitude")
    public double mLatitude;

    @Column
    @SerializedName("longitude")
    public double mLongitude;

    @Column
    @SerializedName("radius")
    public int mRadius;

    @Column
    public float mRating;

    public boolean isLocationWithinCity;

    /**
     * will return null if lat long not within city radius
     */
    public static City getCityForLocation(double latitude, double longitude) {
        List<City> cities = SQLite.select().from(City.class).queryList();
        for (City city : cities) {
            double distance = LocationUtils.getDistanceFromLatLonInKM(city.mLatitude, city.mLongitude,
                    latitude, longitude);
            if (distance < city.mRadius) {
                return city;
            }
        }
        return null;
    }

    public static void setWithinCity(ArrayList<City>cities,double latitude,double longitude){
        for(City city:cities){
            double distance = LocationUtils.getDistanceFromLatLonInKM(city.mLatitude, city.mLongitude,
                    latitude, longitude);
            if (distance < city.mRadius) {
                city.isLocationWithinCity=true;
            }
        }
    }

    public static ArrayList<City> getAllCities(){
        return new ArrayList<>(SQLite.select().from(City.class).queryList());
    }


    public City() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mCityName);
        dest.writeString(this.mCityDesc);
        dest.writeDouble(this.mLatitude);
        dest.writeDouble(this.mLongitude);
        dest.writeInt(this.mRadius);
        dest.writeFloat(this.mRating);
        dest.writeByte(this.isLocationWithinCity ? (byte) 1 : (byte) 0);
    }

    protected City(Parcel in) {
        this.mCityName = in.readString();
        this.mCityDesc = in.readString();
        this.mLatitude = in.readDouble();
        this.mLongitude = in.readDouble();
        this.mRadius = in.readInt();
        this.mRating = in.readFloat();
        this.isLocationWithinCity = in.readByte() != 0;
    }

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel source) {
            return new City(source);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };
}
