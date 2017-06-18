package com.example.heroshe.lovemovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by herohe on 29/04/16.
 */
public class GridReviewAdapter extends BaseAdapter {
    private Context context;
    private List<MovieReview> reviews;
    LayoutInflater inflater;

    public GridReviewAdapter(Context context, List<MovieReview> reviews) {
        this.context = context;
        this.reviews = reviews;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.review, parent, false);
            holder = new ViewHolder();
            holder.auther=(TextView) convertView.findViewById(R.id.auther);
            holder.content = (TextView) convertView.findViewById(R.id.content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.auther.setText(reviews.get(position).auther);
        holder.content.setText(reviews.get(position).content);
        return convertView;
    }

    class ViewHolder {
        TextView auther;
        TextView content;
    }

}
