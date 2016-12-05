package geofence.sample.com.base;

/**
 * Created by sumandas on 04/12/2016.
 */

public interface BasePresenter {

    void setMvpView(BaseView baseView);

    void create();

    void destroy();

    BaseView getView();
}
