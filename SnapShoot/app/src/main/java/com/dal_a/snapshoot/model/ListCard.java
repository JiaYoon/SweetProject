package com.dal_a.snapshoot.model;

import android.content.Context;

import com.dal_a.snapshoot.R;

/**
 * Created by GA on 2015. 9. 26..
 */
public class ListCard extends BaseCard {
    String image;
    String title;
    String userID;
    int favorite;//총 좋아요의 개수
    String time;
    String text;
    String myFavorite;//내가 좋아요 했던 사진이면 "True", 아니면 "False"
    int followers;//나를 팔로워한 사람의 수\
    String UserName;
    int commentsNum; //사진에 등록되어 있는 댓글의 개수
    int width; //사진 너비
    int height; //사진 높이
    int sigPosition; // 낙관을 선택한 위치( 0 = leftTop, 1 = rightTop, 2 = leftBottom, 3 = rightBottom )

    boolean favClicked;

    int layout;


    private OnButtonPressListener mLikeListener;
    private OnButtonPressListener mCommentListener;
    private OnButtonPressListener mShareListener;
    private OnButtonPressListener mImageListener;


    public OnButtonPressListener getLikeListener() {
        return mLikeListener;
    }


    public void setLikeListener(OnButtonPressListener mLikeListener) {
        this.mLikeListener = mLikeListener;
    }

    public OnButtonPressListener getCommentListener() {
        return mCommentListener;
    }

    public void setCommentListener(OnButtonPressListener mCommentListener) {
        this.mCommentListener = mCommentListener;
    }

    public OnButtonPressListener getShareListener() {
        return mShareListener;
    }

    public void setShareListener(OnButtonPressListener mShareListener) {
        this.mShareListener = mShareListener;
    }

    public OnButtonPressListener getImageListener() {
        return mImageListener;
    }

    public void setImageListener(OnButtonPressListener mImageListener) {
        this.mImageListener = mImageListener;
    }

    public ListCard(Context context) {
        super(context);
    }

    public int getCommentsNum() {
        return commentsNum;
    }

    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }
    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }
    public String getMyFavorite() {
        return myFavorite;
    }

    public void setMyFavorite(String myFavorite) {
        this.myFavorite = myFavorite;
    }
    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public void addFavorite(int favorite) {
        this.favorite += favorite;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }

    public int getFavorite() {
        return favorite;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }


    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSigPosition() {
        return sigPosition;
    }

    public void setSigPosition(int sigPosition) {
        this.sigPosition = sigPosition;
    }
    public boolean isFavClicked() {
        return favClicked;
    }

    public void setFavClicked(boolean favClicked) {
        this.favClicked = favClicked;
    }


    public void setLayout(int layout) {
        this.layout = layout;
    }

    @Override
    public int getLayout() {
        return layout;
    }

}


