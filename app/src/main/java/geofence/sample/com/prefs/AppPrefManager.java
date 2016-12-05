package geofence.sample.com.prefs;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by sumandas on 04/12/2016.
 */


public class AppPrefManager {

    private static final String SHARED_PREF_NAME = "rateapp_app_prefs";


    private static final String APP_CITIES_SYNCED = "app_cities_synced";


    public static final String PREF_USER_LATITUDE = "latitude";
    public static final String PREF_USER_LONGITUDE = "longitude";

    private static volatile AppPrefManager sPref;

    private volatile SharedPreferences mPref;
    private volatile SharedPreferences.Editor mEditor;

    private Context mContext;

    public static AppPrefManager initializeManager(Context context) {
        if (sPref == null) {
            synchronized (AppPrefManager.class) {
                if (sPref == null) {
                    sPref = new AppPrefManager(context);
                }
            }
        }
        return sPref;
    }

    public static AppPrefManager getInstance() {
        if (sPref == null) {
            throw new IllegalStateException("Manager not initialized . Call initializeManager");
        } else {
            return sPref;
        }
    }

    private AppPrefManager(Context context) {
        mContext = context;
        mPref = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        mEditor = mPref.edit();
    }

    public void clearPreferences() {
        mEditor.clear().commit();
    }


    public boolean isAppCitiesSync() {
        return mPref.getBoolean(APP_CITIES_SYNCED, false);
    }

    public void setAppCitiesSync(boolean isAppBookingSync) {
        mEditor.putBoolean(APP_CITIES_SYNCED, isAppBookingSync).commit();
    }

    public void setLatitude(double latitude) {
        mEditor.putLong(PREF_USER_LATITUDE, Double.doubleToLongBits(latitude)).commit();
    }

    public double getLatitude() {
        return Double.longBitsToDouble(mPref.getLong(PREF_USER_LATITUDE, 0));
    }

    public void setLongitude(double longitude) {
        mEditor.putLong(PREF_USER_LONGITUDE, Double.doubleToLongBits(longitude)).commit();
    }

    public double getLongitude() {
        return Double.longBitsToDouble(mPref.getLong(PREF_USER_LONGITUDE, 0));
    }


}


