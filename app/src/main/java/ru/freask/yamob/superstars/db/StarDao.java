package ru.freask.yamob.superstars.db;


import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

import ru.freask.yamob.superstars.models.Star;

public class StarDao extends CommonDao<Star, Long> {
    public StarDao(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Star.class);
    }

    public Star findStar(Integer id)  throws SQLException{
        QueryBuilder<Star, Long> queryBuilder = queryBuilder();
        queryBuilder.where().eq("id", id);
        PreparedQuery<Star> preparedQuery = queryBuilder.prepare();
        return queryForFirst(preparedQuery);
    }
}