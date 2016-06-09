package com.dal_a.snapshoot.card;

import android.content.Context;

import com.dal_a.snapshoot.R;
import com.dal_a.snapshoot.model.BaseCard;

/**
 * Created by GA on 2015. 9. 16..
 */
public class PhotoInformationCard extends BaseCard {
    String artist;
    String title;
    String ect;
    String explanation;

    public PhotoInformationCard(Context context) {
        super(context);
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEct() {
        return ect;
    }

    public void setEct(String ect) {
        this.ect = ect;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public int getLayout() {
        return R.layout.card_photo_information;
    }
}
