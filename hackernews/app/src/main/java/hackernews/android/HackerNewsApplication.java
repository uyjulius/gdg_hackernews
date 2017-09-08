package hackernews.android;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

import hackernews.android.beans.Analytics;
import hackernews.android.network.VolleyManager;

/**
 * Created by viki on 4/3/17.
 */

public class HackerNewsApplication extends Application {

    private static FirebaseAnalytics mFirebaseAnalytics;
    private static Context context;
    private static String sessionName;
    private static int eventCounter;

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyManager.prepareVolley( this );
        context = this;
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        sessionName = System.currentTimeMillis() + "";
        eventCounter = 0;
    }

    public static void logEvent( String eventName, Bundle bundle )
    {
        eventCounter++;
        bundle.putString(Analytics.Param.SESSION_NAME, sessionName );
        bundle.putString(Analytics.Param.EVENT_COUNTER, eventCounter + "" );
        mFirebaseAnalytics.logEvent( eventName, bundle );
    }


}
