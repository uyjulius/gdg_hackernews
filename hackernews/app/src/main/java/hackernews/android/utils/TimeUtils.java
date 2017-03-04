package hackernews.android.utils;

import android.content.Context;
import android.text.TextUtils;

import hackernews.android.R;

/**
 * Created by viki on 4/3/17.
 */

public class TimeUtils {

    public final static long ONE_SECOND = 1000;
    public final static long SECONDS = 60;

    public final static long ONE_MINUTE = ONE_SECOND * 60;
    public final static long MINUTES = 60;

    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long HOURS = 24;

    public final static long ONE_DAY = ONE_HOUR * 24;
    public final static long DAYS = 7;

    public final static long ONE_WEEK = ONE_DAY * 7;

    /**
     * converts time (in milliseconds) to human-readable format
     *  "<w> days, <x> hours, <y> minutes and (z) seconds"
     */
    public static String getTimeAgo(Context context, long duration) {
        StringBuffer res = new StringBuffer();
        long temp = 0;
        if (duration >= ONE_SECOND) {
            temp = duration / ONE_WEEK;
            if (temp > 0) {
                duration -= temp * ONE_WEEK;
                res.append( context.getResources().getQuantityString( R.plurals.week, (int) temp, (int) temp ) )
                        .append(duration >= ONE_DAY ? " " : "");
            }
            if (res.length() > 0){
                return res.toString();
            }

            temp = duration / ONE_DAY;
            if (temp > 0) {
                duration -= temp * ONE_DAY;
                res.append( context.getResources().getQuantityString( R.plurals.day, (int) temp, (int) temp ) )
                        .append(duration >= ONE_HOUR ? " " : "");
            }
            if (res.length() > 0){
                return res.toString();
            }

            temp = duration / ONE_HOUR;
            if (temp > 0) {
                duration -= temp * ONE_HOUR;
                res.append( context.getResources().getQuantityString( R.plurals.hour, (int) temp, (int) temp ) )
                        .append(duration >= ONE_MINUTE ? " " : "");
            }

            if (res.length() > 0){
                return res.toString();
            }

            temp = duration / ONE_MINUTE;
            if (temp > 0) {
                duration -= temp * ONE_MINUTE;
                res.append( context.getResources().getQuantityString( R.plurals.minute, (int) temp, (int) temp ) );
            }

            if (res.length() > 0){
                return res.toString();
            }

            if (!TextUtils.isEmpty(res.toString()) && duration >= ONE_SECOND) {
                res.append(" and ");
            }

            temp = duration / ONE_SECOND;
            if (temp > 0) {
                res.append( context.getResources().getQuantityString( R.plurals.second, (int) temp, (int) temp ) );
            }
            return res.toString();
        } else {
            return context.getResources().getQuantityString( R.plurals.minute, 1, 1 ) ;
        }
    }
}
