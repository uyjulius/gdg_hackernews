package hackernews.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hackernews.android.R;
import hackernews.android.StoryDetailActivity;
import hackernews.android.StoryDetailFragment;
import hackernews.android.dummy.DummyContent;

/**
 * Created by viki on 4/3/17.
 */

public class StoryAdapter
        extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

    private final List<DummyContent.Story> mValues;
    private boolean isTwoPane;
    private AppCompatActivity activity;

    public StoryAdapter(List<DummyContent.Story> items, AppCompatActivity activity, boolean isTwoPane ) {
        mValues = items;
        this.isTwoPane = isTwoPane;
        this.activity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.story_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.titleTextView.setText(mValues.get(position).title);
        holder.authorTextView.setText(mValues.get(position).author);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(StoryDetailFragment.ARG_ITEM_ID, holder.mItem.title);
                    StoryDetailFragment fragment = new StoryDetailFragment();
                    fragment.setArguments(arguments);
                    activity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.story_detail_container, fragment)
                            .commit();
                } else {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, StoryDetailActivity.class);
                    intent.putExtra(StoryDetailFragment.ARG_ITEM_ID, holder.mItem.title);

                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleTextView;
        public final TextView authorTextView;
        public final TextView pointsTextView;
        public final TextView timeTextView;
        public DummyContent.Story mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleTextView = (TextView) view.findViewById(R.id.textview_title);
            authorTextView = (TextView) view.findViewById(R.id.textview_author);
            pointsTextView = (TextView) view.findViewById(R.id.textview_points);
            timeTextView = (TextView) view.findViewById(R.id.textview_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + titleTextView.getText() + "'";
        }
    }
}
