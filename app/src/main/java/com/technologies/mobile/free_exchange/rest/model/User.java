package com.technologies.mobile.free_exchange.rest.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 23.09.2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Parcelable{

    public User(){

    }

    @JsonProperty("id")
    String id;

    @Nullable
    @JsonProperty("name")
    String name;

    @Nullable
    @JsonProperty("photo")
    String photo;

    @Nullable
    @JsonProperty("vk_id")
    String vkId;

    protected User(Parcel in) {
        id = in.readString();
        name = in.readString();
        photo = in.readString();
        vkId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getId() {
        return id;
    }

    @Nullable
    public String getName() {
        return name;
    }

    @Nullable
    public String getPhoto() {
        return photo;
    }

    @Nullable
    public String getVkId() {
        return vkId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(photo);
        parcel.writeString(vkId);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    public void setPhoto(@Nullable String photo) {
        this.photo = photo;
    }
}
