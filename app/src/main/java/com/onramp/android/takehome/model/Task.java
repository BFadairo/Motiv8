package com.onramp.android.takehome.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

@Entity(tableName = "tasks")
public class Task implements Parcelable {

    /**The ID of the Task for room **/
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    /**Used to store in the task title*/
    @ColumnInfo(name = "title")
    private String title;

    /**Used to store the task description*/
    @ColumnInfo(name = "description")
    private String description;

    /**Used to store the task's priority*/
    @ColumnInfo(name = "priority")
    private String priorty;

    public Task(String title, String description, String priorty) {
        this.title = title;
        this.description = description;
        this.priorty = priorty;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriorty() {
        return priorty;
    }

    public void setPriorty(String priorty) {
        this.priorty = priorty;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.priorty);
    }

    protected Task(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.priorty = in.readString();
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel source) {
            return new Task(source);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };
}
