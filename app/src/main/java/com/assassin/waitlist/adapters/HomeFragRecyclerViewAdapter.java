package com.assassin.waitlist.adapters;

import android.content.Context;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assassin.waitlist.R;
import com.assassin.waitlist.classes.GenreInfo;
import com.assassin.waitlist.classes.MovieInfo;
import com.assassin.waitlist.interfaces.OnGetMovieCallback;
import com.assassin.waitlist.interfaces.OnGetMoviesCallback;
import com.assassin.waitlist.interfaces.OnMoviesClickCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class HomeFragRecyclerViewAdapter extends RecyclerView.Adapter<HomeFragRecyclerViewAdapter.viewHolder> {
    private List<MovieInfo> movieInfo;
    private List<GenreInfo> allGenres;
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private OnMoviesClickCallback callback;

    public HomeFragRecyclerViewAdapter(List<MovieInfo> movieInfo, List<GenreInfo> allGenres, OnMoviesClickCallback callback) {
        this.movieInfo = movieInfo;
        this.allGenres = allGenres;
        this.callback = callback;
    }


    @NonNull
    @Override
    public HomeFragRecyclerViewAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_recyclerview_item, parent, false);

        viewHolder vH = new viewHolder(view);
        return vH;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeFragRecyclerViewAdapter.viewHolder holder, int position) {
        //populate the adapter.viewholder with movies
        holder.bind(movieInfo.get(position));

    }

    @Override
    public int getItemCount() {
        return movieInfo.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{

        TextView releaseDate;
        TextView title;
        TextView rating;
        TextView genres;
        ImageView poster;
        MovieInfo movie;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            //set the individual items from the api into their widgets
            releaseDate = itemView.findViewById(R.id.item_movie_release_date);
            title = itemView.findViewById(R.id.item_movie_title);
            rating = itemView.findViewById(R.id.item_movie_rating);
            genres = itemView.findViewById(R.id.item_movie_genre);
            poster = itemView.findViewById(R.id.item_movie_poster);


            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callback.onClick(movie);
                                            }
                                        }
            );


        }

        public void bind(MovieInfo movieInfo) {
            this.movie = movieInfo;
            releaseDate.setText(movieInfo.getReleaseDate().split("-")[0]);
            title.setText(movieInfo.getTitle());
            rating.setText(String.valueOf(movieInfo.getRating()));
            genres.setText(getGenres(movieInfo.getGenreIds()));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + movieInfo.getPosterPath())
                    .apply(RequestOptions.placeholderOf(R.color.colorPrimary))
                    .into(poster);
        }
        }
        private String getGenres(List<Integer> genreIds) {
            List<String> movieGenres = new ArrayList<>();
            for (Integer genreId : genreIds) {
                for (GenreInfo genre : allGenres) {
                    if (genre.getId() == genreId) {
                        movieGenres.add(genre.getName());
                        break;
                    }
                }
            }
            return TextUtils.join(", ", movieGenres);
        }

    public void appendMovies(List<MovieInfo> moviesToAppend) {
        movieInfo.addAll(moviesToAppend);
        notifyDataSetChanged();
    }
    public void clearMovies() {
        movieInfo.clear();
        notifyDataSetChanged();
    }
}

