package ru.freask.yamob.superstars;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.squareup.picasso.Picasso;
import java.sql.SQLException;

import ru.freask.yamob.superstars.db.OrmHelper;
import ru.freask.yamob.superstars.db.StarDao;
import ru.freask.yamob.superstars.models.Star;

public class OneStarActivity extends BaseActivity implements View.OnClickListener {
    Button openLink, shareBut;
    ImageView image;
    Star star;
    TextView genres, counts, description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActivityLayoutRes(R.layout.activity_one_star);
        super.onCreate(savedInstanceState);
        OrmHelper ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);

        Bundle bundle = getIntent().getExtras();
        Integer star_id = bundle.getInt("star_id", -1);

        if(star_id == -1)
         return;

        try {
            StarDao starDao = (StarDao) ormHelper.getDaoByClass(Star.class);
            star = starDao.queryForId(Long.parseLong(star_id.toString()));
        } catch (SQLException e) {
            return;
        }

        image = (ImageView) findViewById(R.id.star_image);
        openLink = (Button) findViewById(R.id.open_url);
        shareBut = (Button) findViewById(R.id.share);
        genres = (TextView) findViewById(R.id.genres);
        counts = (TextView) findViewById(R.id.counts);
        description = (TextView) findViewById(R.id.description);
        openLink.setOnClickListener(this);
        shareBut.setOnClickListener(this);

        Picasso.with(context).load(star.getImage()).into(image);

        String name = star.getLabel();
        this.setTitle(name);

        genres.setText(star.getGenresString());
        counts.setText(star.getCountsString(context, " • "));
        description.setText(star.getDescription());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.open_url:
                Uri address = Uri.parse(star.getUrl());
                Intent openlink = new Intent(Intent.ACTION_VIEW, address);
                startActivity(openlink);
                break;
            case R.id.share:
                Intent It = new Intent();
                It.setAction(Intent.ACTION_SEND);
                It.setType("text/plain");
                It.putExtra(android.content.Intent.EXTRA_TEXT, this.getString(R.string.share_text) + ' ' + star.getLabel() + ": " + star.getUrl());
                startActivity(Intent.createChooser(It, this.getString(R.string.share)));
                break;
        }
    }
}
