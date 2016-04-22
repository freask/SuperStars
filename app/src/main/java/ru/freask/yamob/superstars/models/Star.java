package ru.freask.yamob.superstars.models;

import android.content.Context;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;

import ru.freask.yamob.superstars.R;

@DatabaseTable(tableName = "Star")
public class Star {
    @DatabaseField(generatedId = true)
    Integer id;
    @DatabaseField()
    Integer tracks;
    @DatabaseField()
    Integer albums;
    @DatabaseField
    String name;
    @DatabaseField
    String description;
    @DatabaseField
    String link;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    String[] genres;
    @DatabaseField(dataType = DataType.SERIALIZABLE)
    HashMap<String, String> cover;

    public Star() {}

    public String getLabel() {
        return name;
    }

    public Integer getId() {
        return id;
    }

    public String getThumbnail() {
        return cover.get("small");
    }

    public String getImage() {
        return cover.get("big");
    }

    public String getUrl() {
        return link;
    }
    public Integer getTracks() {
        return tracks;
    }
    public Integer getAlbums() {
        return albums;
    }
    public String getGenresString() {
        return StringUtils.join(genres, ", ");
    }
    public String getCountsString(Context context, String delimiter) {
        return context.getString(R.string.counts_str, getAlbums(), delimiter, getTracks());
    }
    public String getDescription() {
        return Character.toUpperCase(description.charAt(0)) + description.substring(1);
    }

}
