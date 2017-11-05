/**
 * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
 * Date: Aug 7, 2017
 * Reference:
 *      https://github.com/udacity/Android_Me
 *      https://www.raywenderlich.com/126528/android-recyclerview-tutorial
 */

package com.udacity.jun.bakingrecipes.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.jun.bakingrecipes.R;
import com.udacity.jun.bakingrecipes.models.RecipeItem;

import java.util.ArrayList;

// Custom adapter class that displays a list of recipe cards in a GridView
public class MainRecyclerViewAdapter extends
        RecyclerView.Adapter<MainRecyclerViewAdapter.RecipeHolder> {
    private Context mContext;
    private ArrayList<RecipeItem> mRecipesList;
    private RecyclerViewAdapterOnClickHandler mClickHandler;

    //The interface that receives onClick messages.
    public interface RecyclerViewAdapterOnClickHandler {
        void RecipeItemOnClick(RecipeItem selectedItem);
    }

    //Constructor to creates an Adapter
    public MainRecyclerViewAdapter(@NonNull Context context, ArrayList<RecipeItem> list,
                                   RecyclerViewAdapterOnClickHandler clickHandler) {
        mContext = context;
        mRecipesList = list;
        mClickHandler = clickHandler;
    }

    // Create ViewHolder subclass
    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView recipeName;
        ImageView recipeImage;

        RecipeHolder(View view){
            super(view);
            recipeName = (TextView) view.findViewById(R.id.recipe_card_text);
            recipeImage = (ImageView) view.findViewById(R.id.recipe_card_image);
            view.setClickable(true);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v){
            int adapterPosi = getAdapterPosition();
            mClickHandler.RecipeItemOnClick(mRecipesList.get(adapterPosi));
        }
    }

    //This gets called when each new ViewHolder is created.
    @Override
    public RecipeHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recipe_card, viewGroup, false);
        view.setFocusable(true);
        return new RecipeHolder(view);
    }

    //OnBindViewHolder is called by the RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(RecipeHolder viewHolder, int position) {
        //recipe name
        viewHolder.recipeName.setSelected(mRecipesList.contains(position));
        String name = mRecipesList.get(position).getName();
        viewHolder.recipeName.setText(name);
        //recipe image
        viewHolder.recipeImage.setSelected(mRecipesList.contains(position));
        //handle the situation if the image url is empty
        if(TextUtils.isEmpty(mRecipesList.get(position).getImage())){
            viewHolder.recipeImage.setImageResource(R.drawable.no_image);
        } else {
            String imageUrl = mRecipesList.get(position).getImage();
            Picasso.with(mContext).load(imageUrl).into(viewHolder.recipeImage);
        }
    }

    //This method simply returns the number of items to display
    @Override
    public int getItemCount() {
        return mRecipesList.size();
    }
}