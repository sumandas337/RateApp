package geofence.sample.com.application;

import javax.inject.Singleton;

import dagger.Component;
import geofence.sample.com.net.RestApiModule;

/**
 * Created by sumandas on 04/12/2016.
 */
@Singleton
@Component(modules = {RestApiModule.class,AppModule.class})
public interface AppComponent extends IAppComponent {
}
