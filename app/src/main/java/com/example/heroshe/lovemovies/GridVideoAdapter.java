package com.example.heroshe.lovemovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by herohe on 29/04/16.
 */
public class GridVideoAdapter extends BaseAdapter {
    private Context context;
    private List<MovieTrailer> videos;
    LayoutInflater inflater;

    public GridVideoAdapter(Context context, List<MovieTrailer> videos) {

        this.context = context;
        this.videos = videos;
        inflater = LayoutInflater.from(context);
    }
/*
    @Override
    public int getItemCount() {
        return videos.size();
    }
*/
    @Override
    public int getCount() {
        return videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.video_view, parent, false);
            holder = new ViewHolder();
            holder.imageView=(ImageView) convertView.findViewById(R.id.video);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.root = (LinearLayout) convertView.findViewById(R.id.trailerRoot);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        final String vidUrl = "https://www.youtube.com/watch?v=" + videos.get(position).videoKey;
        String imgUrl = "http://img.youtube.com/vi/" + videos.get(position).videoKey+"/default.jpg";

        holder.name.setText(videos.get(position).videoName);
        Picasso.with(convertView.getContext()).load(imgUrl).into(holder.imageView);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(vidUrl));
                v.getContext().startActivity(browserIntent);
            }
        });
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
        TextView name;
        LinearLayout root;
    }

}
