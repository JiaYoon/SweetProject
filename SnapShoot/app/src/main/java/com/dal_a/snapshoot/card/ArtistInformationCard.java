package com.dal_a.snapshoot.card;

import android.content.Context;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCard;

/**
 * Created by GA on 2015. 9. 16..
 */
public class ArtistInformationCard extends BaseCard {
    String name;
    String follows;
    String explanation;
    String ToUserID;//사진을 업로드 한 사람의 아이디
    String FromUserID;//사진을 보고 있는 사람의 아이디
    int layout;
    public ArtistInformationCard(Context context) {
        super(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getFollows() {
        return follows;
    }

    public void setFollows(String follows) {
        this.follows = follows;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getToUserID() {
        return ToUserID;
    }

    public void setToUserID(String toUserID) {
        ToUserID = toUserID;
    }

    public String getFromUserID() {
        return FromUserID;
    }

    public void setFromUserID(String fromUserID) {
        FromUserID = fromUserID;
    }

    public void setLayout(int layout) {
        this.layout = layout;
    }

    @Override
    public int getLayout() {
        return layout;
    }
}

