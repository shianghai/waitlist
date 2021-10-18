package com.assassin.waitlist.interfaces;

import com.assassin.waitlist.classes.MovieInfo;

public interface OnGetSingleMovieCallback {
    void onSuccess(MovieInfo movieInfo);

    void onError();
}
