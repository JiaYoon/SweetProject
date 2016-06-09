package com.dal_a.snapshoot.model;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class BaseCardView<T extends BaseCard> extends LinearLayout {

    private T card;

    public BaseCardView(Context context) {
        super(context);
    }

    public BaseCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void build(T card){
        this.card = card;
    }

    public Object getTag(){
        return card.getTag();
    }

}