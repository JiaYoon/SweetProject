package com.dal_a.snapshoot.model;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dal_a.snapshoot.R;

public class SquareImageCardView<T extends SquareImageCard> extends BaseCardView<T> {

    // Default constructors
    public SquareImageCardView(Context context) {
        super(context);
    }

    public SquareImageCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareImageCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(T card) {
        super.build(card);
        ImageView image = (ImageView) findViewById(R.id.image);

        Glide.with(getContext())
                .load(card.getImage())
                .thumbnail(0.3f)
                .into(image);

    }

}
