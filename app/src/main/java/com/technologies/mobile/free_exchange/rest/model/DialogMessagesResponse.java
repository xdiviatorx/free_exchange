package com.technologies.mobile.free_exchange.rest.model;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by diviator on 20.09.2016.
 */
public class DialogMessagesResponse {

    @JsonProperty("RESPONSE")
    DialogMessages dialogMessages;

    public DialogMessages getDialogMessages() {
        return dialogMessages;
    }
}
