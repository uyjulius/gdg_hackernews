package hackernews.android.network;

/**
 * Created by viki on 4/3/17.
 */

public class NetworkCalls {
    public static final String getTopStories()
    {
        return "https://hacker-news.firebaseio.com/v0/topstories.json";
    }

    public static final String getStory( int id )
    {
        return "https://hacker-news.firebaseio.com/v0/item/" + id + ".json?print=pretty";
    }
}
