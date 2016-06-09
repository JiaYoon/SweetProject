package com.dal_a.snapshoot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by GA on 2015. 9. 22..
 */
public class FitImageView extends ImageView {
    public FitImageView(Context context) {
        super(context);
    }

    public FitImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FitImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if(getDrawable() == null) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            return;
        }
        //가로로 길 경우 width가 matchparent
        //세로로 길 경우 height가 300dp

        float width = getDrawable().getIntrinsicWidth();
        float height = getDrawable().getIntrinsicHeight();
        Log.d("asda", width + " " + height );
        if(width/height > 1){
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = (int)(w * height / width);
            setMeasuredDimension(w, h);
        }else{
            int h = (int)(MeasureSpec.getSize(widthMeasureSpec));
            int w = (int)(h * width / height);
            setMeasuredDimension(w, h);
        }

    }
}
