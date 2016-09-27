package com.technologies.mobile.free_exchange.adapters;

/**
 * Created by diviator on 26.09.2016.
 */
public class ChatMessage {

    private String mText;
    private String mMyId;
    private String mFromId;
    private int mType;


    public ChatMessage(String fromId, String text, String myId){
        mText = text;
        mMyId = myId;
        mFromId = fromId;
        mType = calculateType();
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

}
