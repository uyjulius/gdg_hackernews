package hackernews.android.beans;

/**
 * Created by viki on 4/3/17.
 */

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import hackernews.android.utils.TimeUtils;

/**
 * A dummy item representing a piece of author.
 */
public class Story {
    private final String TAG = "Story";

    private String title;
    private String author;
    private int points;
    private long time;
    private String kids;
    private String url;
    private long id;

    public Story( JSONObject json )
    {
        try
        {
            id = json.has ( "id" ) ? json.getLong( "id" ) : 0;
            title = json.has( "title" ) ? json.getString( "title" ) : "";
            author = json.has( "by" ) ? json.getString( "by" ) : "";
            url = json.has( "url" ) ? json.getString( "url" ) : "";
            points = json.has( "score" ) ? json.getInt( "score" ) : 0;
            time = json.has ( "time" ) ? json.getLong( "time" ) : 0;
            kids = json.has( "kids" ) ? json.getJSONArray( "kids" ).toString() : null;
        }
        catch( JSONException e )
        {
            Log.e( TAG, e.getMessage() );
        }
    }

    public String getTitle()
    {
        return title;
    }

    public String getAuthor()
    {
        return author;
    }

    public int getPoints()
    {
        return points;
    }

    public long getId()
    {
        return id;
    }

    public String getTime( Context context )
    {
        return TimeUtils.getTimeAgo( context, System.currentTimeMillis() - time * 1000 );
    }

    public String getKids()
    {
        return kids;
    }

    public String getUrl()
    {
        return url;
    }

    @Override
    public String toString() {
        return title;
    }
}
