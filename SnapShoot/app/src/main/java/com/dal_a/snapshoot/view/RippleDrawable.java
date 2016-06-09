package com.dal_a.snapshoot.view;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class RippleDrawable extends Drawable {
    int color;
    int background;

    int from, to;
    int duration;
    long startTime;

    PointF touch;
    boolean animating;
    Paint paint;

    Interpolator interpolator;

    public RippleDrawable(int color, int background) {
        this.color = color;
//        this.background = background;

    }
    public RippleDrawable(int background) {
//        this.color = color;
        this.background = background;

    }

    public RippleDrawable setColor(int color) {
        this.color = color;
        return this;
    }

    public RippleDrawable setBackground(int background) {
//        this.background = background;
        return this;
    }

    public void start(float x, float y) {
        from = 10;
        to = 1200;
//        to = Math.max(getBounds().width(), getBounds().height());
        interpolator = new DecelerateInterpolator();
        duration = 2000;
        startTime = System.currentTimeMillis();
        touch = new PointF(x, y);
        animating = true;
        if(paint == null){
            paint = new Paint();
        }
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawColor(background);

        if (animating) {
            long time = System.currentTimeMillis();
            float interpolation = interpolator.getInterpolation((time - startTime) / (float) duration);
            float size = from * (1 - interpolation) + to * interpolation;
            paint.setColor(color);
            paint.setAlpha((int) (255 * (1 - interpolation)));
            canvas.drawCircle(touch.x, touch.y, size, paint);
            if (startTime + duration < time) {
                animating = false;
//                background = color;
            }
            invalidateSelf();
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 1;
    }

}
