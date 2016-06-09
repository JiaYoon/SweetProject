package com.dal_a.snapshoot.model;

import android.content.Context;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCard;

/**
 * Created by GA on 2015. 9. 7..
 */
public class SquareImageCard extends BaseCard {
    String image;

    public SquareImageCard(Context context) {
        super(context);
    }

    @Override
    public int getLayout() {
        return R.layout.card_square_image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}

