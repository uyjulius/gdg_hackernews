package hackernews.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hackernews.android.HackerNewsApplication;
import hackernews.android.R;
import hackernews.android.StoryDetailActivity;
import hackernews.android.StoryDetailFragment;
import hackernews.android.beans.Analytics;
import hackernews.android.beans.Story;

/**
 * Created by viki on 4/3/17.
 */

public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private final List<Story> values;
    private boolean isTwoPane;
    private AppCompatActivity activity;
    private String page;

    public StoryAdapter(AppCompatActivity activity, boolean isTwoPane, String page ) {
        values = new ArrayList();
        this.isTwoPane = isTwoPane;
        this.activity = activity;
        this.page = page;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.story = values.get(position);
        holder.titleTextView.setText(values.get(position).getTitle());
        holder.authorTextView.setText(values.get(position).getAuthor());
        holder.pointsTextView.setText( activity.getString( R.string.points, "" + values.get(position).getPoints() ) );
        holder.timeTextView.setText(values.get(position).getTime( activity ));

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Devfest: ANALYTICS START
                Bundle bundle = new Bundle();
                bundle.putString(Analytics.Param.PAGE, page);
                bundle.putString(Analytics.Param.WHAT, "story_row" );
                HackerNewsApplication.logEvent(Analytics.Event.CLICK, bundle);
                //Devfest: ANALYTICS END

                if (isTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(StoryDetailFragment.KIDS, holder.story.getKids());
                    arguments.putString(StoryDetailFragment.TITLE, holder.story.getTitle());
                    arguments.putLong(StoryDetailFragment.STORY_ID, holder.story.getId());
                    StoryDetailFragment fragment = new StoryDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.story_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, StoryDetailActivity.class);
                    intent.putExtra(StoryDetailFragment.KIDS, holder.story.getKids());
                    intent.putExtra(StoryDetailFragment.TITLE, holder.story.getTitle());
                    intent.putExtra(StoryDetailFragment.STORY_ID, holder.story.getId());

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleTextView;
        public final TextView authorTextView;
        public final TextView pointsTextView;
        public final TextView timeTextView;
        public final Button goButton;
        public Story story;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleTextView = (TextView) view.findViewById(R.id.textview_title);
            authorTextView = (TextView) view.findViewById(R.id.textview_author);
            pointsTextView = (TextView) view.findViewById(R.id.textview_points);
            timeTextView = (TextView) view.findViewById(R.id.textview_time);
            goButton = (Button) view.findViewById(R.id.button);
            goButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //Devfest: ANALYTICS START
                    Bundle bundle = new Bundle();
                    bundle.putString(Analytics.Param.PAGE, page);
                    bundle.putString(Analytics.Param.WHAT, "go_button" );
                    HackerNewsApplication.logEvent(Analytics.Event.CLICK, bundle);
                    //Devfest: ANALYTICS END

                    //Devfest: CRASH REPORTING START
//                    int i = 1 / 0;
                    //Devfest: CRASH REPORTING END

                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor( activity.getResources().getColor( R.color.colorPrimary ));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.launchUrl( activity, Uri.parse( story.getUrl() ));
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextView.getText() + "'";
        }
    }

    public synchronized void addItem( Story story )
    {
        values.add( story );
    }


    public void clear() {
        int size = values.size();
        values.clear();
        notifyItemRangeRemoved( 0, size );
    }
}
