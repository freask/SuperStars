package ru.freask.yamob.superstars;

import android.content.Intent;
import java.sql.SQLException;
import java.util.Random;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class StarsActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private StarsAdapter starListAdapter;
    GetStarsSearchRequest starSearchRequest;
    private ListView starListView;
    private static OrmHelper ormHelper;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setActivityLayoutRes(R.layout.activity_stars);
        super.onCreate(savedInstanceState);
        initRequests();
        ormHelper = OpenHelperManager.getHelper(context, OrmHelper.class);

        starListView = (ListView) findViewById(R.id.listViewStars);
        starListAdapter = new StarsAdapter(this);
        starListView.setAdapter(starListAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setOnRefreshListener(this);
            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }
    }

    @Override
    public void onRefresh() {
        loadData(false);
    }

    protected void onResume() {
        super.onResume();
        loadData(true);
    }

    private void loadData(Boolean cache) {
        int version = 2;
        if (cache)
            getSpiceManager().execute(starSearchRequest, "superstars_" + version, DurationInMillis.ONE_DAY, new StarListRequestListener());
        else
            getSpiceManager().execute(starSearchRequest, new StarListRequestListener());
    }


    private void initRequests() {
        starSearchRequest = new GetStarsSearchRequest();
    }

    public final class StarListRequestListener implements RequestListener<StarList> {

        @Override
        public void onRequestFailure(SpiceException spcExcptn) {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(context, R.string.connection_error, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(StarList starList) {
            mSwipeRefreshLayout.setRefreshing(false);
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
