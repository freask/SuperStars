package ru.freask.yamob.superstars.http.requests;

import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;

import ru.freask.yamob.superstars.http.StarService;
import ru.freask.yamob.superstars.models.StarList;

public class GetStarsSearchRequest extends RetrofitSpiceRequest<StarList, StarService> {
    public String query;

    public GetStarsSearchRequest() {
        super(StarList.class, StarService.class);
    }

    @Override
    public StarList loadDataFromNetwork() throws Exception {
        try {
            StarList starSearch;
            starSearch = getService().starsSearch();
            return starSearch;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new IllegalAccessError("The fuck happen!");
        }
    }
}
