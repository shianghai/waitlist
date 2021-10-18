package com.assassin.waitlist.interfaces;

import com.assassin.waitlist.classes.Review;

import java.util.List;

public interface OnGetReviewsCallback {
    void onSuccess(List<Review> reviews);

    void onError();
}
