package com.assassin.waitlist.ui.movies;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.assassin.waitlist.MainActivity;
import com.assassin.waitlist.MovieDetails;
import com.assassin.waitlist.R;
import com.assassin.waitlist.adapters.HomeFragRecyclerViewAdapter;
import com.assassin.waitlist.classes.GenreInfo;
import com.assassin.waitlist.classes.MovieInfo;
import com.assassin.waitlist.classes.MoviesRepository;
import com.assassin.waitlist.classes.MoviesResponse;
import com.assassin.waitlist.interfaces.OnGetGenresCallback;
import com.assassin.waitlist.interfaces.OnGetMoviesCallback;
import com.assassin.waitlist.interfaces.OnMoviesClickCallback;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;



public class MoviesFragment extends Fragment {

    private RecyclerView recyclerView;
    public ArrayList<String> wordList;
    private HomeViewModel homeViewModel;
    private  MoviesRepository moviesRepository;
    HomeFragRecyclerViewAdapter recyclerViewAdapter;
    private boolean isFetchingMovies;
    private int currentPage = 1;
    private List<GenreInfo> movieGenres;
    private String sortBy = MoviesRepository.POPULAR;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    Menu menu;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        moviesRepository = MoviesRepository.getInstance();


        View view = inflater.inflate(R.layout.fragment_movies, container, false);




        recyclerView = (RecyclerView) view.findViewById(R.id.home_recyclerview);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        setupOnScrollListener();



        getGenres();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.mtoolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {

        this.menu = menu;
        inflater.inflate(R.menu.menu_movies, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sort:
                showSortMenu();
                return true;
            case R.id.movie_searchBar:
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
        recyclerView.setLayoutManager(manager);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int totalItemCount = manager.getItemCount();
                int visibleItemCount = manager.getChildCount();
                int firstVisibleItem = manager.findFirstVisibleItemPosition();

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }
    private void showSortMenu() {
        PopupMenu sortMenu = new PopupMenu(getActivity(), getActivity().findViewById(R.id.sort));

        sortMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*
                 * Every time we sort, we need to go back to page 1
                 */
                currentPage = 1;

                switch (item.getItemId()) {
                    case R.id.popular:
                        sortBy = MoviesRepository.POPULAR;
                        getMovies(currentPage);
                        return true;
                    case R.id.top_rated:
                        sortBy = MoviesRepository.TOP_RATED;
                        getMovies(currentPage);
                        return true;
                    case R.id.upcoming:
                        sortBy = MoviesRepository.UPCOMING;
                        getMovies(currentPage);
                        return true;
                    default:
                        return false;
                }
            }
        });
        sortMenu.inflate(R.menu.menu_movies_sort);
        sortMenu.show();
    }

    public void showSearchBar(Menu menu){
        MenuItem searchItem = menu.findItem(R.id.movie_searchBar);
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
        moviesRepository.getGenres(new OnGetGenresCallback() {
            @Override
            public void onSuccess(List<GenreInfo> genres) {
                movieGenres = genres;
                getMovies(currentPage);
            }

            @Override
            public void onError() {
                showError();
            }
        });
    }
    private void getMovies(int page) {
        isFetchingMovies = true;
        moviesRepository.getMovies(page, sortBy, new OnGetMoviesCallback() {
            @Override
            public void onSuccess(int page, String sortBy,List<MovieInfo> movies) {
                Log.d("MoviesRepository", "Current Page = " + page);
                if (recyclerViewAdapter == null) {
                    recyclerViewAdapter = new HomeFragRecyclerViewAdapter(movies, movieGenres, callback);
                    recyclerView.setAdapter(recyclerViewAdapter);
                } else {
                    if(page == 1){
                        recyclerViewAdapter.clearMovies();
                    }
                    recyclerViewAdapter.appendMovies(movies);
                }
                currentPage = page;
                isFetchingMovies = false;
                //setTitle(sortBy);
            }

            @Override
            public void onError() {

            }
        });
    }

    OnMoviesClickCallback callback = new OnMoviesClickCallback() {
        @Override
        public void onClick(MovieInfo movie) {
            Intent intent = new Intent(getActivity(), MovieDetails.class);
            intent.putExtra(MovieDetails.MOVIE_ID, movie.getId());
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