package com.assassin.waitlist.interfaces;

import com.assassin.waitlist.classes.MovieInfo;
import com.assassin.waitlist.classes.MoviesResponse;

import java.util.List;

public interface OnGetMovieCallback {


    void onSuccess(MovieInfo movies);

    void onError();


}
