package hackernews.android.adapter;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import hackernews.android.R;
import hackernews.android.beans.Comment;

/**
 * Created by viki on 4/3/17.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    private final List<Comment> values;
    private FragmentActivity activity;

    public CommentAdapter(FragmentActivity activity ) {
        values = new ArrayList();
        this.activity = activity;
    }

    @Override
    public CommentAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comment_list_content, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CommentAdapter.ViewHolder holder, int position) {
        holder.textTextView.setText( Html.fromHtml( values.get(position).getText()) );
        holder.authorTextView.setText(values.get(position).getAuthor());
        holder.timeTextView.setText(values.get(position).getTime( activity ));

        if( values.get( position ).isParent() )
        {
            applyPadding( holder.textTextView, 0 );
            applyPadding( holder.authorTextView, 0 );
            applyPadding( holder.timeTextView, 0 );
        }
        else
        {
            applyPadding( holder.textTextView, 16 );
            applyPadding( holder.authorTextView, 16 );
            applyPadding( holder.timeTextView, 16 );
        }
    }

    private void applyPadding( View view, int padding )
    {
        view.setPadding( (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, padding, activity.getResources().getDisplayMetrics()) ,0 ,0 ,0 );
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView textTextView;
        public final TextView authorTextView;
        public final TextView timeTextView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            authorTextView = (TextView) view.findViewById(R.id.textview_author);
            textTextView = (TextView) view.findViewById(R.id.textview_text);
            timeTextView = (TextView) view.findViewById(R.id.textview_time);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + textTextView.getText() + "'";
        }
    }

    public synchronized void addItem( Comment comment )
    {
        values.add( comment );
    }

    public synchronized void addItem( Comment comment, long parent )
    {
        for( int i = 0; i < values.size(); i++ )
        {
            if( values.get( i ).getId() == parent )
            {
                values.add( i + 1, comment );
                break;
            }
        }
    }


    public void clear() {
        int size = values.size();
        values.clear();
        notifyItemRangeRemoved( 0, size );
    }
}
