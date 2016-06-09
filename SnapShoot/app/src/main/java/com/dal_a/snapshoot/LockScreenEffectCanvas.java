package com.dal_a.snapshoot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by GA on 2015. 9. 7..
 */
public class LockScreenEffectCanvas extends View {
    float dx = -1;
    float dy = -1;

    Paint p;


    public LockScreenEffectCanvas(Context context) {
        super(context);
        p = new Paint();
    }


    public LockScreenEffectCanvas(Context context, AttributeSet attrs) {
        super(context, attrs);
        p = new Paint();

    }

    public LockScreenEffectCanvas(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        p = new Paint();

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(this.dx == -1 | this.dy == -1){
            return;
        }
        p.setColor(Color.argb(33, 33, 33, 33));
        p.setStyle(Paint.Style.FILL);
        canvas.drawCircle(this.dx, this.dy, 300, p);
    }

    public float getDx() {
        return dx;
    }
    public float getDy() {
        return dy;
    }

    public void setPosition(float dx, float dy) {

        this.dx = dx;
        this.dy = dy;
        invalidate();
    }

}
