package com.mohamednazeem.listviewwithimages;

import android.graphics.Bitmap;

public class RowItem {
    private String userId;
    private Bitmap pic;
    private String title;

    public RowItem(String userId, Bitmap pic, String title){
        this.userId = userId;
        this.pic = pic;
        this.title = title;
    }

    public String getUserId() {
        return userId;
    }
    public Bitmap getPic() {
        return pic;
    }
    public String getTitle() {
        return title;
    }

}
