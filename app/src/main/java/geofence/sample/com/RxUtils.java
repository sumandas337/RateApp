package geofence.sample.com;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by sumandas on 04/12/2016.
 */

public class RxUtils {

    public static <T> Observable<T> build(Observable<T> observable) {
        return observable
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public static <T> Observable<T> buildTest(Observable<T> observable) {
        return observable
                .observeOn(Schedulers.immediate())
                .subscribeOn(Schedulers.immediate());
    }


    public static void unSubscribe(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return;
        }
        subscription.unsubscribe();
    }

}
