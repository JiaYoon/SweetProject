package com.dal_a.snapshoot.card;

import android.content.Context;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCard;

/**
 * Created by GA on 2015. 9. 16..
 */
public class CommentCard extends BaseCard {

    String name;
    String time;
    String text;

    public CommentCard(Context context) {
        super(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public int getLayout() {
        return R.layout.card_comment;
    }
}
