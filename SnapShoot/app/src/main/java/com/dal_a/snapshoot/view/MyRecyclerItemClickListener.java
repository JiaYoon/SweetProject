package com.dal_a.snapshoot.view;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCardView;

/**
 * Created by GA on 2015. 9. 7..
 */
public class MyRecyclerItemClickListener implements RecyclerView.OnItemTouchListener{

    private RecyclerView mRecyclerView;

    public static interface OnItemClickListener {
        public void onItemClick(BaseCardView view, int position);
        public void onItemLongClick(BaseCardView view, int position);
    }

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;

    public MyRecyclerItemClickListener(Context context, OnItemClickListener listener){
        mListener = listener;

        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e){
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e){
                BaseCardView childView = (BaseCardView)mRecyclerView.findChildViewUnder(e.getX(), e.getY());

                if(childView != null && mListener != null){
                    mListener.onItemLongClick(childView, mRecyclerView.getChildPosition(childView));
                }
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e){
        BaseCardView childView = (BaseCardView)view.findChildViewUnder(e.getX(), e.getY());
        if(childView != null && mListener != null && mGestureDetector.onTouchEvent(e)){
            mListener.onItemClick(childView, view.getChildPosition(childView));
        }

        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent){}

    public void setRecyclerView(RecyclerView recyclerView){
        mRecyclerView = recyclerView;
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {

    }

}
