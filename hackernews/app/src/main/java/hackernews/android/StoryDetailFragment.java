package hackernews.android;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import hackernews.android.adapter.CommentAdapter;
import hackernews.android.beans.Analytics;
import hackernews.android.beans.Comment;
import hackernews.android.network.NetworkCalls;
import hackernews.android.network.VolleyManager;

/**
 * A fragment representing a single Story detail screen.
 * This fragment is either contained in a {@link StoryListActivity}
 * in two-pane mode (on tablets) or a {@link StoryDetailActivity}
 * on handsets.
 */
public class StoryDetailFragment extends Fragment {

    private static final String TAG = "StoryDetailFragment";

    public static final String STORY_ID = "story_id";
    public static final String KIDS = "kids";
    public static final String TITLE = "title";

    private String kids;
    private String title;
    private long storyId;
    private int terminator = 0; //This is used to find the number of times loadStory is called before we show the list. There are more elegant solutions, such as using Reactive programming, but for the sake of the demo, I'll use this one.
    private ArrayList<Comment> comments;
    private CommentAdapter adapter;
    private SwipeRefreshLayout swp;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(KIDS)) {
            kids = getArguments().getString(KIDS);
            title = getArguments().getString( TITLE );
            storyId = getArguments().getLong( STORY_ID );
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_detail, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle( title );

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.story_list);
        adapter = new CommentAdapter( getActivity() );
        recyclerView.setAdapter( adapter );

        swp = (SwipeRefreshLayout) rootView.findViewById(R.id.swiperefresh);

        LinearLayoutManager layoutManager = new LinearLayoutManager( getActivity() );
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation() );

        recyclerView.addItemDecoration(dividerItemDecoration);


        swp.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i(TAG, "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        loadKids();
                    }
                }
        );

        loadKids();

        //Devfest: ANALYTICS START
        Bundle bundle = new Bundle();
        bundle.putString(Analytics.Param.PAGE, TAG);
        HackerNewsApplication.logEvent(Analytics.Event.SCREEN_VIEW, bundle);
        //Devfest: ANALYTICS END

        return rootView;
    }

    /**
     * Loads the comments
     *
     */
    private void loadKids()
    {
        try
        {
            adapter.clear();
            comments = new ArrayList();
            swp.setRefreshing( true );
            if( kids != null && kids.length() > 0 )
            {
                //we assume this to be a String of JSONArray
                try {
                    JSONArray kidsArray = new JSONArray(kids);
                    terminator = Math.min( 10, kidsArray.length() );
                    for (int i = 0; i < terminator; i++) {
                        VolleyManager.makeVolleyStringRequest(NetworkCalls.getStory( kidsArray.getInt( i ) ), new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    terminator--;
                                    Comment comment = new Comment(new JSONObject(response), storyId );
                                    comments.add( comment );
                                    adapter.addItem(comment);

                                    if (terminator == 0) {
                                        adapter.notifyItemRangeInserted(0, adapter.getItemCount());
                                        loadSubComments();
                                    }
                                } catch (Exception e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, error.getMessage());
                            }
                        });

                    }
                }
                catch(Exception e)
                {
                    Log.e(TAG, e.getMessage());
                }
            }
        }
        catch( Exception e )
        {
            Log.e( TAG, e.getMessage() );
        }
    }

    private void loadSubComments()
    {
        try {
            terminator = comments.size();
            for (int i = 0; i < comments.size(); i++)
            {
                if( comments.get( i ).getKid() > 0 )
                {
                    VolleyManager.makeVolleyStringRequest(NetworkCalls.getStory( comments.get( i ).getKid() ), new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                terminator--;
                                Comment comment = new Comment(new JSONObject(response), storyId );
                                adapter.addItem(comment, comment.getParent() );

                                if (terminator == 0) {
                                    adapter.notifyDataSetChanged();
                                    swp.setRefreshing( false );
                                }
                            } catch (Exception e) {
                                Log.e(TAG, e.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.e(TAG, error.getMessage());
                        }
                    });
                }
                else
                {
                    terminator--;
                    if (terminator == 0) {
                        adapter.notifyDataSetChanged();
                        swp.setRefreshing( false );
                    }
                }
            }
        }
        catch(Exception e)
        {
            Log.e(TAG, e.getMessage());
        }
    }

}
