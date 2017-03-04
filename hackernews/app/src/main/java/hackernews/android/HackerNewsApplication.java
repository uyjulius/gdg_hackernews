package hackernews.android;

import android.app.Application;

import hackernews.android.network.VolleyManager;

/**
 * Created by viki on 4/3/17.
 */

public class HackerNewsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyManager.prepareVolley( this );
    }
}
