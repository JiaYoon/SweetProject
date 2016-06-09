package com.dal_a.snapshoot.card;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.SquareImageCardView;

/**
 * Created by GA on 2015. 9. 25..
 */
public class LocalPhotoCardView extends SquareImageCardView<LocalPhotoCard> {


    public LocalPhotoCardView(Context context) {
        super(context);
    }

    public LocalPhotoCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LocalPhotoCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void build(LocalPhotoCard card) {
        super.build(card);

        TextView likes = (TextView)findViewById(R.id.folder);
        likes.setText(card.getTitle());

    }
}
