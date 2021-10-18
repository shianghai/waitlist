package com.assassin.waitlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.assassin.waitlist.classes.GenreInfo;
import com.assassin.waitlist.classes.MovieInfo;
import com.assassin.waitlist.classes.Review;
import com.assassin.waitlist.classes.SeriesInfo;
import com.assassin.waitlist.classes.SeriesRepository;
import com.assassin.waitlist.classes.Trailer;
import com.assassin.waitlist.interfaces.OnGetGenresCallback;
import com.assassin.waitlist.interfaces.OnGetMovieCallback;
import com.assassin.waitlist.interfaces.OnGetReviewsCallback;
import com.assassin.waitlist.interfaces.OnGetSeriesCallback;
import com.assassin.waitlist.interfaces.OnGetSingleSeriesCallback;
import com.assassin.waitlist.interfaces.OnGetTrailersCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class SeriesDetails extends AppCompatActivity {
    public static String SERIES_ID = "series_id";

    private static String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w780";
    private static String YOUTUBE_VIDEO_URL = "http://www.youtube.com/watch?v=%s";
    private static String YOUTUBE_THUMBNAIL_URL = "http://img.youtube.com/vi/%s/0.jpg";

    private ImageView seriesBackdrop;
    private TextView seriesTitle;
    private TextView seriesGenres;
    private TextView seriesOverview;
    private TextView seriesOverviewLabel;
    private TextView seriesReleaseDate;
    private RatingBar seriesRating;
    private LinearLayout seriesTrailers;
    private LinearLayout seriesReviews;
    private TextView trailersLabel;
    private TextView reviewsLabel;

    private SeriesRepository SeriesRepository;
    private int series_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_details);

        series_id = getIntent().getIntExtra(SERIES_ID, series_id);

        SeriesRepository = SeriesRepository.getInstance();

        setUpToolbar();
        initUI();
        getSeries();


    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void initUI() {
        seriesBackdrop = findViewById(R.id.seriesDetailsBackdrop);
        seriesTitle = findViewById(R.id.seriesDetailsTitle);
        seriesGenres = findViewById(R.id.seriesDetailsGenres);
        seriesOverview = findViewById(R.id.seriesDetailsOverview);
        seriesOverviewLabel = findViewById(R.id.summaryLabel);
        seriesReleaseDate = findViewById(R.id.seriesDetailsReleaseDate);
        seriesRating = findViewById(R.id.seriesDetailsRating);
        seriesTrailers = findViewById(R.id.seriesTrailers);
        seriesReviews = findViewById(R.id.seriesReviews);
        trailersLabel = findViewById(R.id.trailersLabel);
        reviewsLabel = findViewById(R.id.reviewsLabel);
    }

    private void getSeries() {
        SeriesRepository.getSingleSeries(series_id, new OnGetSingleSeriesCallback(){

            @Override
            public void onSuccess(SeriesInfo series) {

                seriesTitle.setText(series.getTitle());
                seriesOverviewLabel.setVisibility(View.VISIBLE);
                seriesOverview.setText(series.getOverview());
                seriesRating.setVisibility(View.VISIBLE);
                seriesRating.setRating(series.getRating() / 2);
                getSeriesGenres(series);
                seriesReleaseDate.setText(series.getReleaseDate());
                if (!isFinishing()) {
                    Glide.with(SeriesDetails.this)
                            .load(IMAGE_BASE_URL + series.getBackdrop())
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                            .into(seriesBackdrop);
                }
                getSeriesTrailers(series);
                getSeriesReviews(series);
            }

            @Override
            public void onError() {
                finish();
            }
        });
    }
    private void getSeriesGenres(final SeriesInfo series) {
        SeriesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<GenreInfo> genres) {
                if (series.getGenres() != null) {
                    List<String> currentGenres = new ArrayList<>();
                    for (GenreInfo genre : series.getGenres()) {
                        currentGenres.add(genre.getName());
                    }
                    seriesGenres.setText(TextUtils.join(", ", currentGenres));
                }
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }
    private void getSeriesTrailers(SeriesInfo series) {
        SeriesRepository.getSeriesTrailers(series.getId(), new OnGetTrailersCallback() {
            @Override
            public void onSuccess(List<Trailer> trailers) {
                trailersLabel.setVisibility(View.VISIBLE);
                seriesTrailers.removeAllViews();
                for (final Trailer trailer : trailers) {
                    View parent = getLayoutInflater().inflate(R.layout.thumbnail_trailer, seriesTrailers, false);
                    ImageView thumbnail = parent.findViewById(R.id.thumbnail);
                    thumbnail.requestLayout();
                    thumbnail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showTrailer(String.format(YOUTUBE_VIDEO_URL, trailer.getKey()));
                        }
                    });
                    Glide.with(SeriesDetails.this)
                            .load(String.format(YOUTUBE_THUMBNAIL_URL, trailer.getKey()))
                            .apply(RequestOptions.placeholderOf(R.color.colorPrimary).centerCrop())
                            .into(thumbnail);
                    seriesTrailers.addView(parent);
                    //TODO: make youtube logo appear on the trailers
                }
            }

            @Override
            public void onError() {
                // Do nothing
                trailersLabel.setVisibility(View.GONE);
            }
        });
    }

    private void getSeriesReviews(SeriesInfo series) {
        SeriesRepository.getSeriesReviews(series.getId(), new OnGetReviewsCallback() {
            @Override
            public void onSuccess(List<Review> reviews) {
                reviewsLabel.setVisibility(View.VISIBLE);
                seriesReviews.removeAllViews();
                if(reviews.isEmpty()){
                    View parent = getLayoutInflater().inflate(R.layout.review_placeholder, seriesReviews, false );
                    seriesReviews.addView(parent);
                }
                for (Review review : reviews) {
                    View parent = getLayoutInflater().inflate(R.layout.review, seriesReviews, false);
                    TextView author = parent.findViewById(R.id.reviewAuthor);
                    TextView content = parent.findViewById(R.id.reviewContent);
                    author.setText(review.getAuthor());
                    content.setText(review.getContent());
                    seriesReviews.addView(parent);
                }
            }

            @Override
            public void onError() {
                // Do nothing
            }
        });
    }

    private void showTrailer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void showError() {
        Toast.makeText(SeriesDetails.this, "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
}