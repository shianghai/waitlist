package com.assassin.waitlist.ui.series;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.assassin.waitlist.MovieDetails;
import com.assassin.waitlist.R;
import com.assassin.waitlist.SeriesDetails;
import com.assassin.waitlist.adapters.SeriesRecyclerViewAdapter;
import com.assassin.waitlist.classes.GenreInfo;
import com.assassin.waitlist.classes.MoviesRepository;
import com.assassin.waitlist.classes.SeriesInfo;
import com.assassin.waitlist.classes.SeriesRepository;
import com.assassin.waitlist.interfaces.OnGetGenresCallback;
import com.assassin.waitlist.interfaces.OnGetSeriesCallback;
import com.assassin.waitlist.interfaces.OnSeriesClickCallback;

import java.util.List;

import static com.assassin.waitlist.R.id.s_latest;

public class SeriesFragment extends Fragment {


    RecyclerView SeriesRecyclerView;
    private boolean isFetchingSeries;
    private int currentPage = 1;
    private List<GenreInfo> seriesGenres;
    private String sortBy = SeriesRepository.POPULAR;
    private SeriesRepository seriesRepository;
    SeriesRecyclerViewAdapter SeriesRecyclerViewAdapter;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    Menu menu;

    public static SeriesFragment newInstance() {
        return new SeriesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        seriesRepository = SeriesRepository.getInstance();

        View view = inflater.inflate(R.layout.fragment_series, container, false);

        SeriesRecyclerView = (RecyclerView)view.findViewById(R.id.series_recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        SeriesRecyclerView.setLayoutManager(linearLayoutManager);
        setupOnScrollListener();


        getGenres();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.stoolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        this.menu = menu;
        inflater.inflate(R.menu.menu_series, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.series_sort:
                showSortMenu();
                return true;
            case R.id.series_searchBar:
                showSearchBar(menu);
                return true;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    private void setupOnScrollListener() {
        final LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        SeriesRecyclerView.setLayoutManager(manager);
        SeriesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingSeries) {
                        getSeries(currentPage + 1);
                    }
                }
            }
        });
    }

    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(getActivity(), getActivity().findViewById(R.id.series_sort));

        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*
                 * Every time we sort, we need to go back to page 1
                 */
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.s_popular:
                        sortBy = SeriesRepository.POPULAR;
                        getSeries(currentPage);
                        return true;
                    case s_latest:
                        sortBy = SeriesRepository.LATEST;
                        getSeries(currentPage);
                        return true;
                    case R.id.s_top_rated:
                        sortBy = SeriesRepository.TOP_RATED;
                        getSeries(currentPage);
                        return true;

                        default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_series_sort);
        sortMenu.show();
    }

    public void showSearchBar(Menu menu){
        MenuItem searchItem = menu.findItem(R.id.series_searchBar);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
    }

    private void getGenres() {
        seriesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<GenreInfo> genres) {
                seriesGenres = genres;
                getSeries(currentPage);
            }
            @Override
            public void onError() {
                showError();
            }
        });
    }

    private void getSeries(int page) {
        isFetchingSeries = true;
        seriesRepository.getSeries(page, sortBy, new OnGetSeriesCallback() {
            @Override
            public void onSuccess(int page, String sortBy,List<SeriesInfo> series) {
                Log.d("SeriesRepository", "Current Page = " + page);
                if (SeriesRecyclerViewAdapter == null) {
                    SeriesRecyclerViewAdapter = new SeriesRecyclerViewAdapter(series, seriesGenres, callback);
                    SeriesRecyclerView.setAdapter(SeriesRecyclerViewAdapter);
                } else {
                    if(page == 1){
                        SeriesRecyclerViewAdapter.clearMovies();
                    }
                    SeriesRecyclerViewAdapter.appendMovies(series);
                }
                currentPage = page;
                isFetchingSeries = false;
                //setTitle(sortBy);
            }

            @Override
            public void onError() {

            }
        });
    }

    OnSeriesClickCallback callback = new OnSeriesClickCallback(){

        @Override
        public void onClick(SeriesInfo series) {
  //          Toast.makeText(getContext(), "series clicked", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), SeriesDetails.class);
            intent.putExtra(SeriesDetails.SERIES_ID, series.getId());
            startActivity(intent);
        }
    };
    private void showError () {
        Toast.makeText(getActivity(), "Please check your internet connection.", Toast.LENGTH_SHORT).show();
    }
    private void setTitle(String sortBy) {
        switch (sortBy) {
            case MoviesRepository.POPULAR:
                setTitle(getString(R.string.popular));
                break;
            case MoviesRepository.TOP_RATED:
                setTitle(getString(R.string.top_rated));
                break;
            case MoviesRepository.UPCOMING:
                setTitle(getString(R.string.upcoming));
                break;
        }
    }

}
