package com.dal_a.snapshoot.model;

import android.content.Context;
import android.content.res.Resources;

public abstract class BaseCard {
    private Object tag;
    private long id;

    private final Context mContext;

    //행동이 오면 체크하고 처리
    private boolean mDismissible;

    public BaseCard(Context context) {
        this.mContext = context;
    }

    public boolean isDismissible() {
        return mDismissible;
    }

    public void setDismissible(boolean canDismiss) {
        this.mDismissible = canDismiss;
    }


    public abstract int getLayout();

    public Object getTag(){
        return tag;
    }

    public void setTag(Object tag){
        this.tag = tag;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected String getString(int id) {
        return mContext.getString(id);
    }

    protected Resources getResources() {
        return mContext.getResources();
    }

}
