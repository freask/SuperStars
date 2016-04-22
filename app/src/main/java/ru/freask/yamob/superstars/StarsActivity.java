package ru.freask.yamob.superstars;

import android.content.Intent;
import java.sql.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import ru.freask.yamob.superstars.adapters.StarsAdapter;
import ru.freask.yamob.superstars.db.OrmHelper;
import ru.freask.yamob.superstars.db.StarDao;
import ru.freask.yamob.superstars.http.requests.GetStarsSearchRequest;
import ru.freask.yamob.superstars.models.Star;
import ru.freask.yamob.superstars.models.StarList;

public class StarsActivity extends BaseActivity {

    private StarsAdapter starListAdapter;
    GetStarsSearchRequest starSearchRequest;
    private ListView starListView;
    private static OrmHelper ormHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActivityLayoutRes(R.layout.activity_stars);
        super.onCreate(savedInstanceState);
        initRequests();
        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);

        starListView = (ListView) findViewById(R.id.listViewStars);
        starListAdapter = new StarsAdapter(this);
        starListView.setAdapter(starListAdapter);
        starListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Star star = (Star) starListView.getItemAtPosition(position);
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

    protected void onResume() {
        super.onResume();

        int version = 2;
        getSpiceManager().execute(starSearchRequest, "superstars_" + version, DurationInMillis.ONE_DAY, new StarListRequestListener());
    }

    private void initRequests() {
        starSearchRequest = new GetStarsSearchRequest();
    }

    public final class StarListRequestListener implements RequestListener<StarList> {

        @Override
        public void onRequestFailure(SpiceException spcExcptn) {
            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(StarList starList) {
            if (starListView != null) {
                fillStarList(starList);
            }
        }

        public void fillStarList(StarList stars) {
            starListAdapter.clear();
            for (Star star : stars) {
                starListAdapter.add(star);
            }
        }
    }

}
