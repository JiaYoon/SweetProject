package com.dal_a.snapshoot.card;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.SquareImageCardView;

/**
 * Created by GA on 2015. 9. 20..
 */
public class SelectableCardView extends SquareImageCardView<SelectableCard> {


    public SelectableCardView(Context context) {
        super(context);
    }

    public SelectableCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectableCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    public void build(SelectableCard card) {
        super.build(card);

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(card.getName());

        if(card.isSelecting())  setSelected(true);
        else                    setSelected(false);

    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
    }

}

