package com.example.movies.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "trailers")
public class Trailer implements Parcelable {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "trailer_id")
    private String trailerId;
    private long id;
    @ColumnInfo(name = "trailer_name")
    private String trailerName;
    private String key;

    public Trailer(@NonNull String trailerId, long id, String trailerName, String key) {
        this.trailerId = trailerId;
        this.id = id;
        this.trailerName = trailerName;
        this.key = key;
    }

    protected Trailer(Parcel in) {
        trailerId = in.readString();
        id = in.readLong();
        trailerName = in.readString();
        key = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    @NonNull
    public String getTrailerId() {
        return trailerId;
    }


    public void setTrailerId(@NonNull String trailerId) {
        this.trailerId = trailerId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTrailerName() {
        return trailerName;
    }

    public void setTrailerName(String trailerName) {
        this.trailerName = trailerName;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(trailerId);
        dest.writeLong(id);
        dest.writeString(trailerName);
        dest.writeString(key);
    }
}
