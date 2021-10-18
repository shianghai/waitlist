package com.assassin.waitlist.classes;

import android.util.Log;

import com.assassin.waitlist.interfaces.OnGetGenresCallback;
import com.assassin.waitlist.interfaces.OnGetMovieCallback;
import com.assassin.waitlist.interfaces.OnGetReviewsCallback;
import com.assassin.waitlist.interfaces.OnGetSeriesCallback;
import com.assassin.waitlist.interfaces.OnGetSingleSeriesCallback;
import com.assassin.waitlist.interfaces.OnGetTrailersCallback;
import com.assassin.waitlist.interfaces.TMDbApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SeriesRepository {
    public static final String POPULAR = "popular";
    public static final String TOP_RATED = "top_rated";
    public static final String LATEST = "latest";
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String LANGUAGE = "en-US";

    private static SeriesRepository repository;

    private TMDbApi api;

    private SeriesRepository(TMDbApi api) {
        this.api = api;
    }

    public static SeriesRepository getInstance() {
        if (repository == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            repository = new SeriesRepository(retrofit.create(TMDbApi.class));
        }

        return repository;
    }


    public void getSeries(int page, String sortBy, final OnGetSeriesCallback callback) {
        Log.d("SeriesRepository", "Next Page = " + page);
        Callback<SeriesResponse> call = new Callback<SeriesResponse>() {
            @Override
            public void onResponse(Call<SeriesResponse> call, Response<SeriesResponse> response) {
                if (response.isSuccessful()) {
                    SeriesResponse seriesResponse = response.body();
                    if (seriesResponse != null && seriesResponse.getSeries() != null) {
                        callback.onSuccess(seriesResponse.getPage(), sortBy, seriesResponse.getSeries());
                    } else {
                        callback.onError();
                    }
                } else {
                    callback.onError();
                }
            }

            @Override
            public void onFailure(Call<SeriesResponse> call, Throwable t) {
                callback.onError();
            }
        };



        switch (sortBy) {
            case TOP_RATED:
                api.getTopRatedSeries("5000f4402e077c17c71a2b4a193790ed", LANGUAGE, page)
                        .enqueue(call);
                break;
            case LATEST:
                api.getLatestSeries("5000f4402e077c17c71a2b4a193790ed", LANGUAGE, page)
                        .enqueue(call);
                break;
            case POPULAR:
            default:
                api.getPopularSeries("5000f4402e077c17c71a2b4a193790ed", LANGUAGE, page)
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

    public void getSingleSeries(int seriesId, final OnGetSingleSeriesCallback callback) {
        api.getSingleSeries(seriesId, "5000f4402e077c17c71a2b4a193790ed", LANGUAGE)
                .enqueue(new Callback<SeriesInfo>() {
                    @Override
                    public void onResponse(Call<SeriesInfo> call, Response<SeriesInfo> response) {
                        if (response.isSuccessful()) {
                            SeriesInfo series = response.body();
                            if (series != null) {
                                callback.onSuccess(series);
                            } else {
                                callback.onError();
                            }
                        } else {
                            callback.onError();
                        }
                    }

                    @Override
                    public void onFailure(Call<SeriesInfo> call, Throwable t) {
                        callback.onError();
                    }
                });
    }

    public void getSeriesTrailers(int seriesId, final OnGetTrailersCallback callback) {
        api.getSeriesTrailers(seriesId, "5000f4402e077c17c71a2b4a193790ed", LANGUAGE)
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

    public void getSeriesReviews(int seriesId, final OnGetReviewsCallback callback) {
        api.getSeriesReviews(seriesId, "5000f4402e077c17c71a2b4a193790ed", LANGUAGE)
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
