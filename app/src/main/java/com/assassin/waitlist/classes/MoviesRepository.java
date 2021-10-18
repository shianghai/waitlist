package com.assassin.waitlist.classes;

import android.util.Log;

import androidx.annotation.NonNull;

import com.assassin.waitlist.BuildConfig;
import com.assassin.waitlist.interfaces.OnGetGenresCallback;
import com.assassin.waitlist.interfaces.OnGetMovieCallback;
import com.assassin.waitlist.interfaces.OnGetReviewsCallback;
import com.assassin.waitlist.interfaces.OnGetTrailersCallback;
import com.assassin.waitlist.interfaces.TMDbApi;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbMovies;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.assassin.waitlist.interfaces.OnGetMoviesCallback;

import java.util.List;

public class MoviesRepository {
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String UPCOMING = "upcoming";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static MoviesRepository repository;

    private TMDbApi api;

    private MoviesRepository(TMDbApi api) {
        this.api = api;
    }

    public static MoviesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new MoviesRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }


    public void getMovies(int page, String sortBy, final OnGetMoviesCallback callback) {
        Log.d("MoviesRepository", "Next Page = " + page);
        Callback<MoviesResponse> call = new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                if (response.isSuccessful()) {
                    MoviesResponse moviesResponse = response.body();
                    if (moviesResponse != null && moviesResponse.getMovies() != null) {
                        callback.onSuccess(moviesResponse.getPage(), sortBy, moviesResponse.getMovies());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                callback.onError();
            }
        };



        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedMovies("5000f4402e077c17c71a2b4a193790ed", LANGUAGE, page)
                        .enqueue(call);
                break;
            case UPCOMING:
                api.getUpcomingMovies("5000f4402e077c17c71a2b4a193790ed", LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
            default:
                api.getPopularMovies("5000f4402e077c17c71a2b4a193790ed", LANGUAGE, page)
                        .enqueue(call);
                break;
        }
    }

    public void getGenres(final OnGetGenresCallback callback) {
        api.getGenres("5000f4402e077c17c71a2b4a193790ed", LANGUAGE)
                .enqueue(new Callback<GenreResponse>() {
                    @Override
                    public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                        if (response.isSuccessful()) {
                            GenreResponse genresResponse = response.body();
                            if (genresResponse != null && genresResponse.getGenres() != null) {
                                callback.onSuccess(genresResponse.getGenres());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<GenreResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getMovie(int movieId, final OnGetMovieCallback callback) {
        api.getMovie(movieId, "5000f4402e077c17c71a2b4a193790ed", LANGUAGE)
                .enqueue(new Callback<MovieInfo>() {
                    @Override
                    public void onResponse(Call<MovieInfo> call, Response<MovieInfo> response) {
                        if (response.isSuccessful()) {
                            MovieInfo movie = response.body();
                            if (movie != null) {
                                callback.onSuccess(movie);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieInfo> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getTrailers(int movieId, final OnGetTrailersCallback callback) {
        api.getTrailers(movieId, "5000f4402e077c17c71a2b4a193790ed", LANGUAGE)
                .enqueue(new Callback<TrailerResponse>() {
                    @Override
                    public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                        if (response.isSuccessful()) {
                            TrailerResponse trailerResponse = response.body();
                            if (trailerResponse != null && trailerResponse.getTrailers() != null) {
                                callback.onSuccess(trailerResponse.getTrailers());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<TrailerResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getReviews(int movieId, final OnGetReviewsCallback callback) {
        api.getReviews(movieId, "5000f4402e077c17c71a2b4a193790ed", LANGUAGE)
                .enqueue(new Callback<ReviewResponse>() {
                    @Override
                    public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                        if (response.isSuccessful()) {
                            ReviewResponse reviewResponse = response.body();
                            if (reviewResponse != null && reviewResponse.getReviews() != null) {
                                callback.onSuccess(reviewResponse.getReviews());
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewResponse> call, Throwable t) {
                        callback.onError();
                    }
                });
    }
}



