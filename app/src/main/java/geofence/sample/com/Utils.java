package geofence.sample.com;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;

import com.google.android.gms.common.GoogleApiAvailability;

/**
 * Created by sumandas on 04/12/2016.
 */

public class Utils {

    public static final int TIMEOUT_SECONDS=30;

    public static boolean isInternetConnected(Context context) {
        ConnectivityManager con = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (con.getActiveNetworkInfo() == null)
            return false;
        return con.getActiveNetworkInfo().isConnected();

    }

    public static int isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        return googleApiAvailability.isGooglePlayServicesAvailable(activity);
    }

    //status code 1, 2, 3 or 9 are user recoverable
    public static boolean isPlayServicesErrorUserRecoverable(int status) {
        return GoogleApiAvailability.getInstance().isUserResolvableError(status);
    }

    public static void showPlayServicesErrorDialog(Activity activity, int status) {
        GoogleApiAvailability.getInstance().getErrorDialog(activity, status, 2404).show();
    }
}
