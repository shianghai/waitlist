package com.assassin.waitlist.interfaces;

import com.assassin.waitlist.classes.SeriesInfo;

public interface OnGetSingleSeriesCallback {
    void onSuccess(SeriesInfo series);

    void onError();
}
