package com.technologies.mobile.free_exchange.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by diviator on 17.08.2016.
 */
public class SearchExtraditionItem implements Parcelable {

    public SearchExtraditionItem() {

    }

    @JsonProperty("id")
    int id;

    @JsonProperty("pid")
    int pid;

    @JsonProperty("uid")
    int uid;

    @JsonProperty("text")
    String text;

    @JsonProperty("photos")
    String photos;

    @JsonProperty("give")
    String give;

    @JsonProperty("get")
    String get;

    @JsonProperty("contacts")
    String contacts;

    @JsonProperty("place")
    String place;

    @JsonProperty("created")
    long created;

    @JsonProperty("published")
    int published;

    @JsonProperty("type")
    String type;

    @JsonProperty("category")
    int category;

    @JsonProperty("hidden")
    int hidden;

    @JsonProperty("udata")
    User userData;

    @JsonProperty("comments")
    int commentsCount;

    protected SearchExtraditionItem(Parcel in) {
        id = in.readInt();
        pid = in.readInt();
        uid = in.readInt();
        text = in.readString();
        photos = in.readString();
        give = in.readString();
        get = in.readString();
        contacts = in.readString();
        place = in.readString();
        created = in.readLong();
        published = in.readInt();
        type = in.readString();
        category = in.readInt();
        hidden = in.readInt();
        userData = in.readParcelable(User.class.getClassLoader());
        commentsCount = in.readInt();
    }

    public static final Creator<SearchExtraditionItem> CREATOR = new Creator<SearchExtraditionItem>() {
        @Override
        public SearchExtraditionItem createFromParcel(Parcel in) {
            return new SearchExtraditionItem(in);
        }

        @Override
        public SearchExtraditionItem[] newArray(int size) {
            return new SearchExtraditionItem[size];
        }
    };

    public int getId() {
        return id;
    }

    public int getPid() {
        return pid;
    }

    public int getUid() {
        return uid;
    }

    public String getText() {
        return text;
    }

    public String getPhotos() {
        return photos;
    }

    @Nullable
    public Photo[] getPhotosList() {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        try {
            return mapper.readValue(photos, typeFactory.constructArrayType(Photo.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Nullable
    public String[] getPhotosArray() {
        Photo[] photos = getPhotosList();
        if( photos == null ){
            return null;
        }
        String[] res = new String[photos.length];
        for (int i = 0; i < photos.length; i++) {
            if( photos[i].getPhoto807() != null ) {
                res[i] = photos[i].getPhoto807();
            }else{
                res[i] = "";
            }
        }
        return res;
    }

    public String getGive() {
        if (give != null) {
            return give;
        } else {
            return "";
        }
    }

    public String getGet() {
        if (get != null) {
            return get;
        } else {
            return "";
        }
    }

    public String getContacts() {
        if (contacts != null) {
            return contacts;
        } else {
            return "";
        }
    }

    public String getPlace() {
        if (place != null) {
            return place;
        } else {
            return "";
        }
    }

    public Long getCreated() {
        return created;
    }

    public String getDate() {
        long timestamp = getCreated();
        timestamp *= 1000;
        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        return df.format(new Date(timestamp));
    }

    public int isPublished() {
        return published;
    }

    public String getType() {
        return type;
    }

    public int getCategory() {
        return category;
    }

    public int isHidden() {
        return hidden;
    }

    public User getUserData() {
        return userData;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeInt(pid);
        parcel.writeInt(uid);
        parcel.writeString(text);
        parcel.writeString(photos);
        parcel.writeString(give);
        parcel.writeString(get);
        parcel.writeString(contacts);
        parcel.writeString(place);
        parcel.writeLong(created);
        parcel.writeInt(published);
        parcel.writeString(type);
        parcel.writeInt(category);
        parcel.writeInt(hidden);
        parcel.writeParcelable(userData, i);
        parcel.writeInt(commentsCount);
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }
}
