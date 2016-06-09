package com.dal_a.snapshoot.card;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.view.SquareImageView;

/**
 * Created by GA on 2015. 9. 16..
 */
public class PhotoInformationCardView extends BaseCardView<PhotoInformationCard> {

    // Default constructors
    public PhotoInformationCardView(Context context) {
        super(context);
    }

    public PhotoInformationCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoInformationCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(PhotoInformationCard card) {
        super.build(card);

        StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) this.getLayoutParams();
        sglp.setFullSpan(true);
        this.setLayoutParams(sglp);

        ((TextView)findViewById(R.id.artist)).setText(card.getArtist());
        ((TextView)findViewById(R.id.title)).setText(card.getTitle());
        ((TextView)findViewById(R.id.text)).setText(card.getExplanation());
        ((TextView)findViewById(R.id.ect)).setText(card.getEct());

    }

}

