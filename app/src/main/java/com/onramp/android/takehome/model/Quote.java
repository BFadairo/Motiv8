package com.onramp.android.takehome.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import androidx.annotation.NonNull;

public class Quote implements Parcelable {

    /**ID of the quote used for room **/
    @SerializedName("id")
    @Expose
    private int id;

    /**Used to store the quote**/
    @SerializedName("title")
    @Expose
    private String title;

    /**Used to store the person who said the quote**/
    @SerializedName("author")
    private String author;

    /**String used to store the image link **/
    @SerializedName("media")
    private String quoteLink;

    public Quote(int id, String quote, String speaker, String quoteLink) {
        this.id = id;
        this.title = quote;
        this.author = speaker;
        this.quoteLink = quoteLink;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String quote) {
        this.title = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getQuoteLink() {
        return quoteLink;
    }

    public void setQuoteLink(String quoteLink) {
        this.quoteLink = quoteLink;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.author);
        dest.writeString(this.quoteLink);
    }

    protected Quote(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.author = in.readString();
        this.quoteLink = in.readString();
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
