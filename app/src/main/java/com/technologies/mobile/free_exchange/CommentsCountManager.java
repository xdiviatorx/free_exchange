package com.technologies.mobile.free_exchange;

/**
 * Created by diviator on 21.10.2016.
 */

public class CommentsCountManager {

    private static int mPostId = -1;
    private static int mCommentCount = -1;

    public static void setCommentCount(int commentCount, int postId) {
        mCommentCount = commentCount;
        mPostId = postId;
    }

    public static int getCommentCount(int postId) {
        if (mPostId == postId) {
            return mCommentCount;
        }else{
            return -1;
        }
    }

}
