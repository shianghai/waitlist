package com.assassin.waitlist.interfaces;

import com.assassin.waitlist.classes.GenreInfo;

import java.util.List;

public interface OnGetGenresCallback {
    void onSuccess(List<GenreInfo> genres);

    void onError();
}
