package ru.freask.yamob.superstars.http;

import retrofit.http.GET;
import ru.freask.yamob.superstars.models.StarList;

public interface StarService {
    @GET("/download.cdn.yandex.net/mobilization-2016/artists.json")
    StarList starsSearch();
}