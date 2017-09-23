package hackernews.android;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import hackernews.android.adapter.StoryAdapter;
import hackernews.android.beans.Analytics;
import hackernews.android.beans.Story;
import hackernews.android.network.NetworkCalls;
import hackernews.android.network.VolleyManager;

/**
 * An activity representing a list of Stories. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link CommentActivity} representing
 * item points. On tablets, the activity presents the list of items and
 * item points side-by-side using two vertical panes.
 */
public class StoryListActivity extends AppCompatActivity {

    private static final String TAG = "StoryListActivity";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean isTwoPane;
    private int terminator = 0; //This is used to find the number of times loadStory is called before we show the list. There are more elegant solutions, such as using Reactive programming, but for the sake of the demo, I'll use this one.
    private StoryAdapter adapter;
    private SwipeRefreshLayout swp;
    private long screenLoadTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());
        swp = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.story_list);

        if (findViewById(R.id.story_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            isTwoPane = true;
        }

        adapter = new StoryAdapter(this, isTwoPane, TAG);
        recyclerView.setAdapter( adapter );

        LinearLayoutManager layoutManager = new LinearLayoutManager( this );
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation() );

        recyclerView.addItemDecoration(dividerItemDecoration);

        swp.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        loadTopStories();
                    }
                }
        );

        //Devfest: ANALYTICS START
        Bundle bundle = new Bundle();
        bundle.putString(Analytics.Param.PAGE, TAG);
        HackerNewsApplication.logEvent(Analytics.Event.SCREEN_VIEW, bundle);
        //Devfest: ANALYTICS END

        loadTopStories();
    }

    private void loadTopStories() {

        try
        {
            adapter.clear();
            swp.setRefreshing( true );
            VolleyManager.makeVolleyStringRequest(NetworkCalls.getTopStories(), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        //Devfest: ANALYTICS START
                        Bundle bundle = new Bundle();
                        bundle.putString(Analytics.Param.PAGE, TAG);
                        bundle.putString(Analytics.Param.WHAT, "loadTopStories" );
                        HackerNewsApplication.logEvent(Analytics.Event.API_LOAD_SUCCESS, bundle);
                        //Devfest: ANALYTICS END

                        JSONArray topStoriesArray = new JSONArray( response );
                        terminator = Math.min( 10, topStoriesArray.length() );
                        for( int i = 0; i < terminator; i++ )
                        {
                            loadStory( topStoriesArray.getInt( i ) );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.e( TAG, e.getMessage() );
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //Devfest: ANALYTICS START
                    Bundle bundle = new Bundle();
                    bundle.putString(Analytics.Param.PAGE, TAG);
                    bundle.putString(Analytics.Param.WHAT, "loadTopStories" );
                    HackerNewsApplication.logEvent(Analytics.Event.API_LOAD_FAIL, bundle);
                    //Devfest: ANALYTICS END

                    Log.e( TAG, error.getMessage() );
                    Toast.makeText( StoryListActivity.this, getString( R.string.error_loading_top_stories ), Toast.LENGTH_SHORT );
                }
            });
        }
        catch( Exception e )
        {
            Log.e( TAG, e.getMessage() );
            Toast.makeText( this, getString( R.string.error_loading_top_stories ), Toast.LENGTH_SHORT );
        }
    }

    /**
     * Loads the story
     *
     * @param id
     */
    private void loadStory(int id) {
        try
        {
            VolleyManager.makeVolleyStringRequest(NetworkCalls.getStory( id ), new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try
                    {
                        terminator--;
                        Story story = new Story( new JSONObject( response ) );
                        adapter.addItem( story );

                        if( terminator == 0 )
                        {
                            adapter.notifyItemRangeInserted( 0, adapter.getItemCount() );
                            swp.setRefreshing( false );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.e( TAG, e.getMessage() );
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e( TAG, error.getMessage() );
                }
            });
        }
        catch( Exception e )
        {
            Log.e( TAG, e.getMessage() );
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();

        screenLoadTime = System.currentTimeMillis();

        //Devfest: ANALYTICS START
        Bundle bundle = new Bundle();
        bundle.putString(Analytics.Param.PAGE, TAG);
        HackerNewsApplication.logEvent(Analytics.Event.SCREEN_VIEW, bundle);
        //Devfest: ANALYTICS END
    }

    @Override
    public void onStop()
    {
        super.onStop();

        //Devfest: ANALYTICS START
        long timeSpent = System.currentTimeMillis() - screenLoadTime;

        Bundle b = new Bundle();
        b.putString( Analytics.Param.PAGE, TAG );
        b.putString( Analytics.Param.TIME_SPENT, timeSpent + "" );
        HackerNewsApplication.logEvent(Analytics.Event.SCREEN_EXIT, b );
        //Devfest: ANALYTICS END
    }


}
