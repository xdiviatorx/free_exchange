package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 20.09.2016.
 */
public class DialogMessages {

    @JsonProperty("dialog_id")
    String dialogId;

    @JsonProperty("messages")
    DialogMessage[] dialogMessage;

    public String getDialogId() {
        return dialogId;
    }

    public DialogMessage[] getDialogMessage() {
        return dialogMessage;
    }
}
