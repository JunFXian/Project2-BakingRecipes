/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: July 27, 2017
 * Reference:
 *      https://stackoverflow.com/questions/27390682/highlight-selected-item-inside-a-recyclerview
 */

package com.udacity.jun.bakingrecipes.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.jun.bakingrecipes.R;
import com.udacity.jun.bakingrecipes.models.RecipeStep;

import java.util.ArrayList;

public class StepsRecyclerViewAdapter extends
        RecyclerView.Adapter<StepsRecyclerViewAdapter.StepsViewHolder> {
    // The context is used to utility methods, app resources and layout inflaters
    private final Activity mActivity;
    private final ArrayList<RecipeStep> mStepsList;
    private final RecyclerViewAdapterOnClickHandler mClickHandler;

    //The interface that receives onClick messages.
    public interface RecyclerViewAdapterOnClickHandler {
        void onClick(RecipeStep selectedStep);
    }

    //Constructor to creates an Adapter
    public StepsRecyclerViewAdapter(@NonNull Activity activity, ArrayList<RecipeStep> list,
                                    RecyclerViewAdapterOnClickHandler clickHandler) {
        mActivity = activity;
        mStepsList = list;
        mClickHandler = clickHandler;
    }

    // A ViewHolder is a required part of the pattern for RecyclerViews.
    class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        final TextView step;
        StepsViewHolder(View itemView) {
            super(itemView);
            step = (TextView) itemView.findViewById(R.id.step_name_text);
            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(mStepsList.get(adapterPosition));
        }
    }

    //This gets called when each new ViewHolder is created.
    @Override
    public StepsViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.recipe_step_list,
                viewGroup, false);
        view.setFocusable(true);
        return new StepsViewHolder(view);
    }

    //OnBindViewHolder is called by the RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(StepsViewHolder viewHolder, int position) {
        //step name
        viewHolder.step.setSelected(mStepsList.contains(position));
        String name = String.valueOf(position) +
                " " + mStepsList.get(position).getShortDescription();
        viewHolder.step.setText(name);
    }

    //This method simply returns the number of items to display
    @Override
    public int getItemCount() {
        return mStepsList.size();
    }
}