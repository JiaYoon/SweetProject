package com.dal_a.snapshoot.card;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCardView;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by GA on 2015. 9. 23..
 */
public class ArtistCardView extends BaseCardView<ArtistInformationCard> {

    // Default constructors
    public ArtistCardView(Context context) {
        super(context);
    }

    public ArtistCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ArtistCardView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void build(ArtistInformationCard card) {
        super.build(card);

        StaggeredGridLayoutManager.LayoutParams sglp = (StaggeredGridLayoutManager.LayoutParams) this.getLayoutParams();
        sglp.setFullSpan(true);
        this.setLayoutParams(sglp);

//        ((TextView)findViewById(R.id.artist)).setText(card.getArtist());

        ((ProfilePictureView)findViewById(R.id.userImage)).setProfileId(card.ToUserID);

        ((TextView)findViewById(R.id.artist2)).setText(card.getName());
        ((TextView)findViewById(R.id.followers)).setText(card.getFollows()+"명의 팔로워");
        ((TextView)findViewById(R.id.introduction)).setText(card.getExplanation());

    }
}