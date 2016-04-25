package ru.freask.yamob.superstars.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;

import java.sql.SQLException;

import ru.freask.yamob.superstars.OneStarActivity;
import ru.freask.yamob.superstars.R;
import ru.freask.yamob.superstars.StarsActivity;
import ru.freask.yamob.superstars.db.OrmHelper;
import ru.freask.yamob.superstars.db.StarDao;
import ru.freask.yamob.superstars.models.Star;

public class StarsAdapter extends ArrayAdapter<Star> {
    Context context;
    static OrmHelper ormHelper;

    public StarsAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
        this.context = context;
        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);
    }

    static class ViewHolder{
        TextView name;
        TextView genres;
        TextView counts;
        ImageView image;
        ImageButton link;
        ImageButton share;
        SwipeLayout swipeLayout;

        void populateItem(final Context context, final Star star) {
            name.setText(star.getLabel());
            genres.setText(star.getGenresString());
            counts.setText(star.getCountsString(context, ", "));
            link.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri address = Uri.parse(star.getUrl());
                    Intent openlink = new Intent(Intent.ACTION_VIEW, address);
                    context.startActivity(openlink);
                }
            });
            link.setVisibility(star.getUrl() == null ? View.INVISIBLE : View.VISIBLE);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent It = new Intent();
                    It.setAction(Intent.ACTION_SEND);
                    It.setType("text/plain");
                    It.putExtra(android.content.Intent.EXTRA_TEXT, context.getString(R.string.share_text) + ' ' + star.getLabel() + ": " + star.getUrl());
                    context.startActivity(Intent.createChooser(It, context.getString(R.string.share)));
                }
            });
            Picasso.with(context).load(star.getThumbnail()).into(image);

            swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        StarDao starDao = (StarDao) ormHelper.getDaoByClass(Star.class);
                        Star starSearched = starDao.findStar(star.getId());
                        int star_id;

                        //put model to db for access from next activity
                        if (starSearched == null)
                        {
                            starDao.create(star);
                            star_id = star.getId();
                        } else {
                            star_id = starSearched.getId();
                        }
                        Intent i = new Intent(context, OneStarActivity.class);
                        i.putExtra("star_id", star_id);
                        context.startActivity(i);
                    } catch (SQLException e) {

                    }
                }
            });
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
            holder.link = (ImageButton) convertView.findViewById(R.id.open_url);
            holder.share = (ImageButton) convertView.findViewById(R.id.share);
            holder.swipeLayout = (SwipeLayout) convertView.findViewById(R.id.swipe_layout);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Star star = getItem(position);
        holder.populateItem(context, star);
        return convertView;
    }
}
