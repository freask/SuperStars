package ru.freask.yamob.superstars.http;

import android.util.Log;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;

import roboguice.util.temp.Ln;

/**
 * Created by Alexander.Kashin01 on 30.04.2015.
 */
public class ClientService extends RetrofitGsonSpiceService {
    public static final Boolean DEBUG = false;
    public static final String API_URL = "http://cache-default05e.cdn.yandex.net";

    @Override
    protected String getServerUrl() {
        return API_URL;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Ln.getConfig().setLoggingLevel(Log.ERROR);
        addRetrofitInterface(StarService.class);
    }
}
