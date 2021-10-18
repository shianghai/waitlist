package com.assassin.waitlist.interfaces;


import com.assassin.waitlist.classes.GenreResponse;
import com.assassin.waitlist.classes.MovieInfo;
import com.assassin.waitlist.classes.MoviesResponse;
import com.assassin.waitlist.classes.ReviewResponse;
import com.assassin.waitlist.classes.SeriesInfo;
import com.assassin.waitlist.classes.SeriesResponse;
import com.assassin.waitlist.classes.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TMDbApi {
    @GET("movie/popular")
    Call<MoviesResponse> getPopularMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/upcoming")
    Call<MoviesResponse> getUpcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("movie/{movie_id}")
    Call<MovieInfo> getMovie(
            @Path("movie_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/videos")
    Call<TrailerResponse> getTrailers(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("movie/{movie_id}/reviews")
    Call<ReviewResponse> getReviews(
            @Path("movie_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("tv/{tv_id}")
    Call<SeriesResponse> getSeries(
          @Path("tv_id") int id,
          @Query("api_key") String apiKey,
          @Query("language") String language
    );

    @GET("tv/latest")
    Call<SeriesResponse> getLatestSeries(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    ) ;



    @GET("tv/popular")
    Call<SeriesResponse> getPopularSeries(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/top_rated")
    Call<SeriesResponse> getTopRatedSeries(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") int page
    );

    @GET("tv/{tv_id}")
    Call<SeriesInfo> getSingleSeries(
            @Path("tv_id") int id,
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("tv/{tv_id}/videos")
    Call<TrailerResponse> getSeriesTrailers(
            @Path("tv_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

    @GET("tv/{tv_id}/reviews")
    Call<ReviewResponse> getSeriesReviews(
            @Path("tv_id") int id,
            @Query("api_key") String apiKEy,
            @Query("language") String language
    );

}

