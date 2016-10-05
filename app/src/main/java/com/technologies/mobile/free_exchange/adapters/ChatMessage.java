package com.technologies.mobile.free_exchange.adapters;

import com.technologies.mobile.free_exchange.rest.model.User;

/**
 * Created by diviator on 26.09.2016.
 */
public class ChatMessage {

    private String mText;
    private String mMyId;
    private String mFromId;
    private int mType;

    private User mFromUser;

    public ChatMessage(User fromUser, String text, String myId){
        mText = text;
        mMyId = myId;
        mFromId = fromUser.getId();
        mType = calculateType();

        mFromUser = fromUser;
    }

    public ChatMessage(String text){
        mType = MessageListAdapter.OUTPUT_MESSAGE;
        mText = text;
    }

    public int getType(){
        return mType;
    }

    public String getText(){
        return mText;
    }

    private int calculateType(){
        if( mFromId.equals(mMyId) ){
            return MessageListAdapter.OUTPUT_MESSAGE;
        }else{
            return MessageListAdapter.INPUT_MESSAGE;
        }
    }

    public String getPhotoUrl(){
        return mFromUser.getPhoto();
    }

}
