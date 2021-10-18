package com.assassin.waitlist.ui.dashboard;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.assassin.waitlist.classes.MovieGetter;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;

public class DashboardViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public class doAsyncTask extends AsyncTask<Void, Void, MovieDb>{
        @Override
        protected MovieDb doInBackground(Void... voids) {
            TmdbMovies movies = new TmdbApi("5000f4402e077c17c71a2b4a193790ed").getMovies();
            MovieDb movie = movies.getMovie(5353, "en");
            return movie;
        }

        @Override
        protected void onPostExecute(MovieDb movieDb) {
            super.onPostExecute(movieDb);
            String text = movieDb.getTitle();
            mText.setValue(text);
        }
    }
    public  DashboardViewModel() {
        mText = new MutableLiveData<>();
        doAsyncTask asyncTask = new doAsyncTask();

        asyncTask.execute();


    }

    public LiveData<String> getText() {
        return mText;
    }
}