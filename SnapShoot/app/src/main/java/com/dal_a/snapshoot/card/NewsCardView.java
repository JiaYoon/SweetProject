package com.dal_a.snapshoot.card;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.dal_a.snapshoot.Constant;
import com.dal_a.snapshoot.Http.HttpRequest;
import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCard;
import com.dal_a.snapshoot.model.BaseCardView;
import com.dal_a.snapshoot.model.ListCard;
import com.dal_a.snapshoot.view.FitWidthImageView;
import com.dal_a.snapshoot.view.SquareImageView;
import com.facebook.Profile;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

/**
 * Created by GA on 2015. 9. 7..
 */
public class NewsCardView extends BaseCardView<ListCard> {

    // Default constructors
    public NewsCardView(Context context) {
        super(context);
    }

    public NewsCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(final ListCard card) {
        super.build(card);
        final FitWidthImageView image = (FitWidthImageView) findViewById(R.id.image);
        ((TextView) findViewById(R.id.title)).setText(card.getTitle());
        ((TextView) findViewById(R.id.name)).setText(card.getUserName());
        ((TextView) findViewById(R.id.NewsCommentNum)).setText(Integer.toString(card.getCommentsNum()));
        ((TextView) findViewById(R.id.heartNum)).setText(card.getFavorite() + "");
        Log.d("adasd", card.getMyFavorite());
        if (card.getMyFavorite().equals("True")){//좋아요를 했던 사진들은 하트모양을 보여줌
            ((ImageView)findViewById(R.id.heart)).setImageResource(R.mipmap.heartfull);
            ((ImageView)findViewById(R.id.heart)).setSelected(true);
        } else{
            ((ImageView)findViewById(R.id.heart)).setImageResource(R.mipmap.heart);
            ((ImageView)findViewById(R.id.heart)).setSelected(false);

        }


        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card.getImageListener() != null) {
                    card.getImageListener().onButtonPressedListener(v, card);
                }
            }
        });

        findViewById(R.id.heart).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card.getLikeListener() != null) {
                    card.getLikeListener().onButtonPressedListener(v, card);
                }
            }
        });

        findViewById(R.id.comment).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (card.getCommentListener() != null) {
                    card.getCommentListener().onButtonPressedListener(v, card);
                }
            }
        });

        image.setSize(card.getWidth(), card.getHeight());
        image.requestLayout();
        Glide.with(getContext())
                .load(card.getImage()+"/600/600")
                .thumbnail(0.3f)
                .into(image);

    }

}