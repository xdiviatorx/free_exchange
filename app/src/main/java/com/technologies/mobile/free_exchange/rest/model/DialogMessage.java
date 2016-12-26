package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 20.09.2016.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class DialogMessage {

    @JsonProperty("id")
    String messageId;

    @JsonProperty("from")
    String fromId;

    @JsonProperty("to")
    String toId;

    @JsonProperty("date")
    String time;

    @JsonProperty("dlg_id")
    String dialogId;

    @JsonProperty("viewed")
    String viewed;

    @JsonProperty("text")
    String text;

    @JsonProperty("udata_from")
    User userDataFrom;

    @JsonProperty("udata_to")
    User userDataTo;

    public String getText() {
        return text;
    }

    public String getMessageId() {
        return messageId;
    }

    public String getFromId() {
        return fromId;
    }

    public String getToId() {
        return toId;
    }

    public String getTime() {
        return time;
    }

    public String getDialogId() {
        return dialogId;
    }

    public String getViewed() {
        return viewed;
    }

    public User getUserDataFrom() {
        return userDataFrom;
    }

    public User getUserDataTo() {
        return userDataTo;
    }
}
