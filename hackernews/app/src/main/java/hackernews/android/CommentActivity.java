package hackernews.android;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import hackernews.android.beans.Analytics;

/**
 * An activity representing a single Story detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item points are presented side-by-side with a list of items
 * in a {@link CommentActivity}.
 */
public class CommentActivity extends AppCompatActivity {


    private long screenLoadTime;
    private static final String TAG = "CommentActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(CommentFragment.KIDS, getIntent().getStringExtra(CommentFragment.KIDS));
            arguments.putString(CommentFragment.TITLE, getIntent().getStringExtra(CommentFragment.TITLE));
            arguments.putLong(CommentFragment.STORY_ID, getIntent().getLongExtra(CommentFragment.STORY_ID, 0));
            CommentFragment fragment = new CommentFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.story_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more points, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // navigateUpTo(new Intent(this, StoryListActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
