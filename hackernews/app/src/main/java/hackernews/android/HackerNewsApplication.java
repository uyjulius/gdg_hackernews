package hackernews.android;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;

import hackernews.android.beans.Analytics;
import hackernews.android.network.VolleyManager;
import io.fabric.sdk.android.Fabric;

/**
 * Created by viki on 4/3/17.
 */

public class HackerNewsApplication extends Application {

    private static final String TAG = "HackerNewsApplication";
    private static FirebaseAnalytics mFirebaseAnalytics;
    private static Context context;
    private static String sessionName;
    private static int eventCounter;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;


        /**
         * Ideally, your session must be a unique key where there cannot be two instances of. This is so that
         * you can distinguish between different sessions across world accessing your app simultaneously.
         * Using System.currentTimeMillis() + a randomly generated four digit number should generally give you
         * a safe enough distinguishing characteristic between sessions.
         *
         * For the sake of Google Devfest PH, we'll use Devfest_Demo_Session" as the session name.
         *
         */

        sessionName = "Devfest_Demo_" + System.currentTimeMillis();

        /**
         * The event counter always begins at 0 for every new session. The idea is for you to know the
         * exact step by step actions that was taken by the user across the event. While you can choose to
         * use System.currentTimeMillis() in place of this, this counter is much more readable.
         *
         */
        eventCounter = 0;


        /**
         * We initialize the libraries here:
         * Volley is our networking library of choice (Retrofit is a good alternative)
         * Firebase Analytics will be used to track our events
         * Fabric will be used to track our crashes
         */
        VolleyManager.prepareVolley( this );
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        Fabric.with(this, new Crashlytics());

        /**
         * We set the Crashlytics username to sessionName. It can be the user_id of your logged in user or
         * whatever you think is useful. The whole point of using this is to cross reference between Firebase
         * and Fabric when there is a crash.
         */
        Crashlytics.setUserName( sessionName );
    }

    /**
     * Method used to log the events to Firebase
     *
     * @param eventName The name of the event
     * @param bundle The parameters passed with the event
     */
    public static void logEvent( String eventName, Bundle bundle )
    {
        /**
         * We increment the eventCounter every time an event is logged in order for us to properly track
         * the progression of user action.
         */
        eventCounter++;

        bundle.putString(Analytics.Param.SESSION_NAME, sessionName );
        bundle.putString(Analytics.Param.EVENT_COUNTER, eventCounter + "" );
        bundle.putString(Analytics.Param.UTC_TIME, System.currentTimeMillis() + "" );
        mFirebaseAnalytics.logEvent( eventName, bundle );


        StringBuilder b = new StringBuilder();

        b.append( bundle.getString( Analytics.Param.SESSION_NAME ) + " | " );
        b.append( bundle.getString( Analytics.Param.EVENT_COUNTER ) + " | " );
        b.append( eventName );
        for (String key : bundle.keySet())
        {
            if( key.equals( Analytics.Param.SESSION_NAME ) ||
                key.equals( Analytics.Param.EVENT_COUNTER ) ||
                key.equals( Analytics.Param.UTC_TIME ) )
                continue;

            b.append(" | " );
            b.append( bundle.get(key).toString());
        }

        Log.d( TAG, b.toString() );

    }
}
