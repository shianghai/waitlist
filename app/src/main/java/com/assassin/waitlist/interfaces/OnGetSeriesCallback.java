package com.assassin.waitlist.interfaces;


import com.assassin.waitlist.classes.SeriesInfo;

import java.util.List;

public interface OnGetSeriesCallback {
    void onSuccess(int page, String sortBy, List<SeriesInfo> movies);

    void onError();
}
