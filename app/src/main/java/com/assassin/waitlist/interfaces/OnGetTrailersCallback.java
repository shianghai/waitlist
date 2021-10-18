package com.assassin.waitlist.interfaces;

import com.assassin.waitlist.classes.Trailer;

import java.util.List;

public interface OnGetTrailersCallback {
    void onSuccess(List<Trailer> trailers);

    void onError();
}

