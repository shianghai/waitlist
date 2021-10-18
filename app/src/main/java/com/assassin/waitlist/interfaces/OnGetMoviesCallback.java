package com.assassin.waitlist.interfaces;

import com.assassin.waitlist.classes.MovieInfo;
import com.assassin.waitlist.classes.MoviesResponse;

import java.util.List;

public interface OnGetMoviesCallback {
    void onSuccess(int page, String sortBy, List<MovieInfo> movies);

    void onError();
}
