package com.dal_a.snapshoot.card;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCardView;

/**
 * Created by GA on 2015. 9. 16..
 */
public class CommentCardView extends BaseCardView<CommentCard> {


    public CommentCardView(Context context) {
        super(context);
    }

    public CommentCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CommentCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void build(CommentCard card) {
        super.build(card);

        ((TextView)findViewById(R.id.name)).setText(card.getName());
        ((TextView)findViewById(R.id.time)).setText(card.getTime());
        ((TextView)findViewById(R.id.comment)).setText(card.getText());

    }
}
