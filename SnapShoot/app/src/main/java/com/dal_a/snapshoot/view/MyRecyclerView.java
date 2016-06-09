package com.dal_a.snapshoot.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.dal_a.snapshoot.model.BaseCard;

import java.util.HashMap;
import java.util.List;

public class MyRecyclerView extends RecyclerView {
//    GestureDetector.SimpleOnGestureListener gestureListener;
//    MyGestureDetector gd;

    public MyRecyclerView(Context context){
        super(context);
        setMyGesture(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs){
        super(context, attrs);
        setMyGesture(context);
    }

    public MyRecyclerView(Context context, AttributeSet attrs, int style){
        super(context, attrs, style);
        setMyGesture(context);
    }

    private void setMyGesture(Context context){
//        gestureListener = new GestureListener();
//        gd = new MyGestureDetector(context, gestureListener);
//        gd.setIsLongpressEnabled(false);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev){
        //return gd.onTouchEvent(ev);
//        return false;
//
//
//        //Ignore scroll events.
//        if(ev.getAction() == MotionEvent.ACTION_MOVE)
//            return true;
//
//        //Dispatch event for non-scroll actions, namely clicks!
        return super.dispatchTouchEvent(ev);
    }
    public void addOnItemTouchListener(MyRecyclerItemClickListener.OnItemClickListener listener){

        MyRecyclerItemClickListener itemClickListener = new MyRecyclerItemClickListener(getContext(), listener);

        itemClickListener.setRecyclerView(this);
        super.addOnItemTouchListener(itemClickListener);
    }

    public void setCards(List<BaseCard> cards) {
        ((ImageRecyclerViewAdapter) (getAdapter())).setAllCards(cards);
    }

    public void addCards(List<BaseCard> cards) {
        ((ImageRecyclerViewAdapter) (getAdapter())).addAllCards(cards);
    }

    public void changeCard(BaseCard card) {
        ((ImageRecyclerViewAdapter) (getAdapter())).changeCard(card.getId(), card);
    }

}