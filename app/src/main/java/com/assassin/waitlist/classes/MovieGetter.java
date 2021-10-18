package com.assassin.waitlist.classes;

import android.os.AsyncTask;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class MovieGetter  extends AsyncTask<Void, Void, MovieDb> {


        protected MovieDb doInBackground(Void... v) {
            TmdbMovies movies = new TmdbApi("myAPIkey_here").getMovies();
            MovieDb movie = movies.getMovie(5353, "en");
            return movie;
        }

        protected void onPostExecute(MovieDb movie) {
            // Do something with movie

        }
}
