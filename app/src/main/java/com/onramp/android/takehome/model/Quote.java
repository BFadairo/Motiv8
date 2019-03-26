package com.onramp.android.takehome.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

@Entity(tableName = "quotes")
public class Quote implements Parcelable {


    /**ID of the quote used for room **/
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**Used to store the quote**/
    @ColumnInfo(name = "quote")
    private String quote;

    /**Used to store the person who said the quote**/
    @ColumnInfo(name = "speaker")
    private String speaker;

    public Quote(int id, String quote, String speaker) {
        this.id = id;
        this.quote = quote;
        this.speaker = speaker;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.quote);
        dest.writeString(this.speaker);
    }

    protected Quote(Parcel in) {
        this.id = in.readInt();
        this.quote = in.readString();
        this.speaker = in.readString();
    }

    public static final Creator<Quote> CREATOR = new Creator<Quote>() {
        @Override
        public Quote createFromParcel(Parcel source) {
            return new Quote(source);
        }

        @Override
        public Quote[] newArray(int size) {
            return new Quote[size];
        }
    };
}
