package hackernews.android.beans;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import hackernews.android.utils.TimeUtils;

/**
 * Created by viki on 4/3/17.
 */

public class Comment {
    private final String TAG = "Comment";

    private long id;
    private long parent;
    private String text;
    private String author;
    private long time;
    private int kid;
    private boolean isParent;

    public Comment( JSONObject json, long terminatorId )
    {
        try
        {
            id = json.has ( "id" ) ? json.getLong( "id" ) : 0;
            parent = json.has ( "parent" ) ? json.getLong( "parent" ) : 0;
            if( parent == terminatorId )
            {
                parent = 0;
            }
            text = json.has( "text" ) ? json.getString( "text" ) : "";
            author = json.has( "by" ) ? json.getString( "by" ) : "";
            time = json.has ( "time" ) ? json.getLong( "time" ) : 0;
            kid = json.has( "kids" ) ? json.getJSONArray( "kids" ).getInt( 0 ) : 0;
            isParent = parent == 0 ? true : false;
        }
        catch( JSONException e )
        {
            Log.e( TAG, e.getMessage() );
        }
    }

    public long getId()
    {
        return id;
    }

    public long getParent()
    {
        return parent;
    }

    public String getText()
    {
        return text;
    }

    public String getAuthor()
    {
        return author;
    }

    public boolean isParent()
    {
        return isParent;
    }

    public String getTime( Context context )
    {
        return TimeUtils.getTimeAgo( context, System.currentTimeMillis() - time * 1000 );
    }

    public int getKid()
    {
        return kid;
    }

    @Override
    public String toString() {
        return text;
    }
}
