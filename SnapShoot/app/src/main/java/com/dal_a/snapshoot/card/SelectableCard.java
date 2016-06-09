package com.dal_a.snapshoot.card;

import android.content.Context;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.SquareImageCard;

/**
 * Created by GA on 2015. 9. 20..
 */
public class SelectableCard extends SquareImageCard {
    int index;
    String name;
    boolean selecting;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public SelectableCard(Context context) {
        super(context);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelecting() {
        return selecting;
    }

    public void setSelecting(boolean selecting) {
        this.selecting = selecting;
    }

    @Override
    public int getLayout(){
        return R.layout.card_selectable;
    }
}

