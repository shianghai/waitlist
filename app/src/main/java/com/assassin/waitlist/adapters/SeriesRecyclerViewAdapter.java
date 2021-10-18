package com.assassin.waitlist.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.assassin.waitlist.R;
import com.assassin.waitlist.classes.GenreInfo;
import com.assassin.waitlist.classes.MovieInfo;
import com.assassin.waitlist.classes.SeriesInfo;
import com.assassin.waitlist.interfaces.OnSeriesClickCallback;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class SeriesRecyclerViewAdapter extends RecyclerView.Adapter<SeriesRecyclerViewAdapter.viewHolder> {
    private List<SeriesInfo> SeriesInfo;
    private List<GenreInfo> allGenres;
    private String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/w500";
    private OnSeriesClickCallback callback;

    public SeriesRecyclerViewAdapter(List<SeriesInfo> SeriesInfo, List<GenreInfo> allGenres, OnSeriesClickCallback callback){
        this.SeriesInfo = SeriesInfo;
        this.allGenres = allGenres;
        this.callback = callback;
    }


    @NonNull
    @Override
    public SeriesRecyclerViewAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_recyclerview_items, parent, false);
        viewHolder viewHolder = new viewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SeriesRecyclerViewAdapter.viewHolder holder, int position) {
        holder.bind(SeriesInfo.get(position));
    }

    @Override
    public int getItemCount() {
        return SeriesInfo.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{
        //the widgets are initialised here
        TextView firstAirDate;
        TextView name;
        TextView rating;
        TextView genres;
        ImageView poster;
        SeriesInfo series;


        public viewHolder(@NonNull View ItemView){
            super(ItemView);
             //items are assigned to the widgets here
            firstAirDate = itemView.findViewById(R.id.item_series_first_release_date);
            name = itemView.findViewById(R.id.item_series_name);
            rating = itemView.findViewById(R.id.item_series_rating);
            genres = itemView.findViewById(R.id.item_series_genre);
            poster = itemView.findViewById(R.id.item_series_poster);

            itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                callback.onClick(series);
                                            }
                                        }
            );
        }

        public void bind(SeriesInfo SeriesInfo) {
            this.series = SeriesInfo;
            firstAirDate.setText(SeriesInfo.getFirstAirDate().split("-")[0]);
            name.setText(SeriesInfo.getName());
            rating.setText(String.valueOf(SeriesInfo.getRating()));
            genres.setText(getGenres(SeriesInfo.getGenreIds()));
            Glide.with(itemView)
                    .load(IMAGE_BASE_URL + SeriesInfo.getPosterPath())
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

    public void appendMovies(List<SeriesInfo> moviesToAppend) {
        SeriesInfo.addAll(moviesToAppend);
        notifyDataSetChanged();
    }
    public void clearMovies() {
        SeriesInfo.clear();
        notifyDataSetChanged();
    }
}
