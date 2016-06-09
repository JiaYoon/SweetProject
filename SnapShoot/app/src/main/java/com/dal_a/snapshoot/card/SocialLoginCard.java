package com.dal_a.snapshoot.card;

import android.content.Context;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCard;

/**
 * Created by GA on 2015. 9. 20..
 */
public class SocialLoginCard extends BaseCard {

    public SocialLoginCard(Context context) {
        super(context);
    }

    @Override
    public int getLayout() {
        return R.layout.card_social_login;
    }

}
