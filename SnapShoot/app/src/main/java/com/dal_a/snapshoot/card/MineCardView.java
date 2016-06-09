package com.dal_a.snapshoot.card;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.model.ListCard;
import com.dal_a.snapshoot.model.SquareImageCardView;

/**
 * Created by GA on 2015. 9. 19..
 */
public class MineCardView extends BaseCardView<ListCard> {


    public MineCardView(Context context) {
        super(context);
    }

    public MineCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MineCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void build(ListCard card) {
        super.build(card);

        ImageView image = (ImageView) findViewById(R.id.image);

        Glide.with(getContext())
                .load(card.getImage()+"/300/300")
                .thumbnail(0.3f)
                .into(image);

        TextView likes = (TextView)findViewById(R.id.likes);
        likes.setText(card.getFavorite()+"");

    }
}
