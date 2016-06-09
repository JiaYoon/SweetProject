package com.dal_a.snapshoot.card;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;

import com.dal_a.snapshoot.model.BaseCardView;

/**
 * Created by GA on 2015. 9. 20..
 */
public class SocialLoginCardView extends BaseCardView<SocialLoginCard> {
    public SocialLoginCardView(Context context) {
        super(context);
    }

    public SocialLoginCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SocialLoginCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(SocialLoginCard card) {
        super.build(card);

        StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) this.getLayoutParams();
        sglp.setFullSpan(true);
        this.setLayoutParams(sglp);

    }


}
