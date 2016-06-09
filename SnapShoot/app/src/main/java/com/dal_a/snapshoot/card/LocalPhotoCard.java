package com.dal_a.snapshoot.card;

import android.content.Context;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.SquareImageCard;

/**
 * Created by GA on 2015. 9. 25..
 */
public class LocalPhotoCard  extends SquareImageCard {

    String title;

    public LocalPhotoCard(Context context) {
        super(context);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getLayout() {
        return R.layout.card_localphoto;
    }
}
