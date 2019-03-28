package com.onramp.android.takehome.view.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.onramp.android.takehome.R;
import com.onramp.android.takehome.model.Task;
import com.onramp.android.takehome.view.Fragments.TaskFragment.OnListFragmentInteractionListener;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Task} and makes a call to the
 * specified {@link OnListFragmentInteractionListener}.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {

    private List<Task> mTasks = new ArrayList<>();
    private Context mContext;
    private final OnListFragmentInteractionListener mListener;

    public TaskAdapter(Context context, OnListFragmentInteractionListener listener) {
        mContext = context;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mTasks.get(position);
        holder.mTitleView.setText(holder.mItem.getTitle());
        holder.mDescriptionView.setText(holder.mItem.getDescription());
        holder.mPriorityView.setText(holder.mItem.getPriority());
        holder.mTimeView.setText(holder.mItem.getTime());
        switch (holder.mItem.getPriority()) {
            case "High":
                holder.mPriorityView.setTextColor(mContext.getColor(R.color.high_priority));
                break;
            case "Medium":
                holder.mPriorityView.setTextColor(mContext.getColor(R.color.medium_priority));
                break;
            case "Low":
                holder.mPriorityView.setTextColor(mContext.getColor(R.color.low_priority));
                break;
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    public void setTasks(List<Task> tasks) {
        this.mTasks = tasks;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.task_title)
        TextView mTitleView;
        @BindView(R.id.task_description)
        TextView mDescriptionView;
        @BindView(R.id.task_priority_level)
        TextView mPriorityView;
        @BindView(R.id.time_view)
        TextView mTimeView;
        public final View mView;
        public Task mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, mView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mDescriptionView.getText() + "'";
        }
    }
}
