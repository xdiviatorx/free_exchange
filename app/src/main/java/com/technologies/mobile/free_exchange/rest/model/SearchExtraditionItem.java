package com.technologies.mobile.free_exchange.rest.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.type.TypeFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by diviator on 17.08.2016.
 */
public class SearchExtraditionItem {

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
    public ArrayList<String> getPhotosList() {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        try {
            return mapper.readValue(photos, typeFactory.constructCollectionType(ArrayList.class, String.class));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getGive() {
        if( give != null ) {
            return give;
        }else{
            return "";
        }
    }

    public String getGet() {
        if( get != null ) {
            return get;
        }else{
            return "";
        }
    }

    public String getContacts() {
        if( contacts != null ) {
            return contacts;
        }else{
            return "";
        }
    }

    public String getPlace() {
        if( place != null ) {
            return place;
        }else{
            return "";
        }
    }

    public Long getCreated() {
        return created;
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
}
