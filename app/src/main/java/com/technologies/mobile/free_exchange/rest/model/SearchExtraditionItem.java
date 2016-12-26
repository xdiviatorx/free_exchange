package com.technologies.mobile.free_exchange.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;

import com.fasterxml.jackson.annotation.JacksonInject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchExtraditionItem implements Parcelable {

    int id;
    int pid;
    int uid;
    String text;
    String photos;
    String give;
    String get;
    String contacts;
    String place;
    long created;
    String published;
    String type;
    int category;
    int hidden;
    User userData;
    int commentsCount;

    String[] photosArray;
    Spanned formattedGet;
    Spanned formattedGive;

    @JsonCreator
    public SearchExtraditionItem(@JsonProperty("id") int id,
                                 @JsonProperty("pid") int pid,
                                 @JsonProperty("uid") int uid,
                                 @JsonProperty("text") String text,
                                 @JsonProperty("photos") String photos,
                                 @JsonProperty("give") String give,
                                 @JsonProperty("get") String get,
                                 @JsonProperty("contacts") String contacts,
                                 @JsonProperty("place") String place,
                                 @JsonProperty("created") long created,
                                 @JsonProperty("published") String published,
                                 @JsonProperty("type") String type,
                                 @JsonProperty("category") int category,
                                 @JsonProperty("hidden") int hidden,
                                 @JsonProperty("udata") User userData,
                                 @JsonProperty("comments") int commentsCount) {
        this.id = id;
        this.pid = pid;
        this.uid = uid;
        this.text = text;
        this.photos = photos;
        this.give = give;
        this.get = get;
        this.contacts = contacts;
        this.place = place;
        this.created = created;
        this.published = published;
        this.type = type;
        this.category = category;
        this.hidden = hidden;
        this.userData = userData;
        this.commentsCount = commentsCount;

        createPhotosArray();
        formatGiveGet();
    }

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
        published = in.readString();
        type = in.readString();
        category = in.readInt();
        hidden = in.readInt();
        userData = in.readParcelable(User.class.getClassLoader());
        commentsCount = in.readInt();

        createPhotosArray();
        formatGiveGet();
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
    private Photo[] getPhotosList() {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        try {
            return mapper.readValue(photos, typeFactory.constructArrayType(Photo.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void createPhotosArray() {
        Photo[] photos = getPhotosList();
        if (photos == null) {
            photosArray = new String[0];
            return;
        }
        String[] res = new String[photos.length];
        for (int i = 0; i < photos.length; i++) {
            if (photos[i].getPhoto2560() != null) {
                res[i] = photos[i].getPhoto2560();
            } else if (photos[i].getPhoto1280() != null) {
                res[i] = photos[i].getPhoto1280();
            } else if (photos[i].getPhoto807() != null) {
                res[i] = photos[i].getPhoto807();
            } else if (photos[i].getPhoto604() != null) {
                res[i] = photos[i].getPhoto604();
            } else if (photos[i].getPhoto130() != null) {
                res[i] = photos[i].getPhoto130();
            } else if (photos[i].getPhoto75() != null) {
                res[i] = photos[i].getPhoto75();
            } else {
                res[i] = "";
            }
        }
        photosArray = res;
    }

    private void formatGiveGet(){
        if (android.os.Build.VERSION.SDK_INT < 24) {
            if( give != null ) {
                formattedGive = Html.fromHtml(give);
            }else{
                formattedGive = Html.fromHtml("");
            }
            if( get != null ) {
                formattedGet = Html.fromHtml(get);
            }else{
                formattedGet = Html.fromHtml("");
            }
        } else {
            if( give != null) {
                formattedGive = Html.fromHtml(give, Html.FROM_HTML_MODE_LEGACY);
            }else{
                formattedGive = Html.fromHtml("", Html.FROM_HTML_MODE_LEGACY);
            }
            if( get != null ) {
                formattedGet = Html.fromHtml(get, Html.FROM_HTML_MODE_LEGACY);
            }else{
                formattedGet = Html.fromHtml("", Html.FROM_HTML_MODE_LEGACY);
            }
        }
    }

    @Nullable
    public String[] getPhotosArray() {
        return photosArray;
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
        DateFormat df = new SimpleDateFormat("HH:mm dd.MM");
        return df.format(new Date(timestamp));
    }

    public Spanned getFormattedGet() {
        return formattedGet;
    }

    public Spanned getFormattedGive() {
        return formattedGive;
    }

    public String getPublished() {
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
        parcel.writeString(published);
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
