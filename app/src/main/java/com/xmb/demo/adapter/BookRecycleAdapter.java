package com.xmb.demo.adapter;

import android.app.Application;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.xmb.demo.R;
import com.xmb.demo.activity.BookMainActivity;
import com.xmb.demo.listener.BookRecycleViewItemClickListener;
import com.xmb.demo.utils.BookSharedPreferencesUtils;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Author by Ben
 * On 2020-03-02.
 *
 * @Descption
 */
public class BookRecycleAdapter extends RecyclerView.Adapter<BookRecycleAdapter.MyViewHolder> {
    private List<String> mDataset;
    private BookRecycleViewItemClickListener bookRecycleViewItemClickListener;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View view;
        public TextView titleTextView;
        public TextView tagTextView;
        ViewGroup viewGroup;

        public MyViewHolder(View v) {
            super(v);
            view = v;
            titleTextView = view.findViewById(R.id.book_recycler_view_adapter_title_textView);
            tagTextView = view.findViewById(R.id.tagTextView);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public BookRecycleAdapter(List<String> myDataset, BookRecycleViewItemClickListener bookRecycleViewItemClickListener) {
        mDataset = myDataset;
        this.bookRecycleViewItemClickListener = bookRecycleViewItemClickListener;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public BookRecycleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.book_recycle_adapter, parent, false));
        myViewHolder.viewGroup = parent;
        return myViewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.titleTextView.setText("第" + (position + 1) + "章");
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bookRecycleViewItemClickListener != null) {
                    bookRecycleViewItemClickListener.onClick(position);
                }
            }
        });
        int chapterNum = BookSharedPreferencesUtils.instants(holder.viewGroup.getContext()).getChapterNum();
        if (position == chapterNum) {
            holder.tagTextView.setVisibility(View.VISIBLE);
        } else {
            holder.tagTextView.setVisibility(View.GONE);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


}