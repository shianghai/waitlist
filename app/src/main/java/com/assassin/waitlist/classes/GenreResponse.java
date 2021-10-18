package com.assassin.waitlist.classes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenreResponse {
    @SerializedName("genres")
    @Expose
    private List<GenreInfo> genres;

    public List<GenreInfo> getGenres() {
        return genres;
    }

    public void setGenres(List<GenreInfo> genres) {
        this.genres = genres;
    }
}
