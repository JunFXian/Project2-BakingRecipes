///**
// * Created by Jun Xian for Udacity Android Developer Nanodegree project 2
// * Date: July 5, 2017
// * Reference:
// *      https://github.com/udacity/Android_Me
// */
//
//package com.udacity.jun.bakingrecipes.adapters;
//
//import android.app.Activity;
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//import com.udacity.jun.bakingrecipes.R;
//
//import java.util.ArrayList;
//
//// Custom adapter class that displays a list of recipe cards in a GridView
//public class MainGridAdapter extends ArrayAdapter<String> {
//    // Create ViewHolder subclass
//    private static class ViewHolder {
//        TextView recipeName;
//    }
//    //constructor for the adapter
//    public MainGridAdapter(Activity context, ArrayList<String> list) {
//        super(context, 0, list);
//    }
//
//    //Creates a new TextView for each item referenced by the adapter
//    @NonNull
//    @Override
//    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
//        String name = "";
//        if (getItem(position) != null) {
//            name = getItem(position);
//        }
//
//        ViewHolder viewHolder;
//        if (convertView == null) {
//            viewHolder = new ViewHolder();
//            LayoutInflater inflater = LayoutInflater.from(getContext());
//            convertView = inflater.inflate(R.layout.recipe_card, parent, false);
//            viewHolder.recipeName = (TextView) convertView.findViewById(R.id.recipe_card_item);
//            convertView.setTag(viewHolder);
//        } else {
//            viewHolder = (ViewHolder) convertView.getTag();
//        }
//        viewHolder.recipeName.setText(name);
//        return convertView;
//    }
//}