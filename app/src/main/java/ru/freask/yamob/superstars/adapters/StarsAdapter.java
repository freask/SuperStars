package ru.freask.yamob.superstars.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.freask.yamob.superstars.R;
import ru.freask.yamob.superstars.models.Star;

public class StarsAdapter extends ArrayAdapter<Star> {
    Context context;

    public StarsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.context = context;
    }


    static class ViewHolder{
        TextView name;
        TextView genres;
        TextView counts;
        ImageView image;

        void populateItem(Context context, final Star star) {
            name.setText(star.getLabel());
            genres.setText(star.getGenresString());
            counts.setText(star.getCountsString(context, ", "));
            Picasso.with(context).load(star.getThumbnail()).into(image);
        }
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.star_item, parent, false);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.genres = (TextView) convertView.findViewById(R.id.genres);
            holder.counts = (TextView) convertView.findViewById(R.id.counts);
            holder.image = (ImageView) convertView.findViewById(R.id.item_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Star star = getItem(position);
        holder.populateItem(context, star);
        return convertView;
    }
}
