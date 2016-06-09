package com.dal_a.snapshoot.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.dal_a.snapshoot.card.CommentCard;

/**
 * Created by GA on 2015. 9. 7..
 */
public class SquareImageView extends ImageView
{

    public SquareImageView(final Context context)
    {
        super(context);
    }

    public SquareImageView(final Context context, final AttributeSet attrs)
    {
        super(context, attrs);
    }

    public SquareImageView(final Context context, final AttributeSet attrs, final int defStyle)
    {
        super(context, attrs, defStyle);
    }


    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec)
    {
        final int width = getDefaultSize(getSuggestedMinimumWidth(),widthMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh)
    {
        super.onSizeChanged(w, w, oldw, oldh);
    }


}
