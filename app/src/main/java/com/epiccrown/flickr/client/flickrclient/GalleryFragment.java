package com.epiccrown.flickr.client.flickrclient;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;

import java.util.List;

/**
 * Created by Epiccrown on 11.04.2018.
 */

public class GalleryFragment extends Fragment {
    RecyclerView mRecycler;
    PhotoAdapter mAdapter;
    FloatingActionButton fb;

    public List<GalleryItem> gallery_items;
    public ProgressBar progressBar;
    public int current_gallery_size = 0;
    public Parcelable recyclerViewState;
    public String usersQuery;
    public boolean isLoading = false;
    public boolean isSearching = false;
    public boolean isCleared = false;

    public static final String API_KEY = "5891c7536b3c1bb422ae6923c8f7840e";
    public static final String API_SECRET = "4fe3d582bd470e15";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        new FetchItemsTask().execute();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu,menu);

        MenuItem menuItem = menu.findItem(R.id.app_bar_search);

        final SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(!query.trim().equals("")){
                    isSearching = true;
                    isCleared = false;
                    usersQuery = query;
                    MyPreferences.setStoredQuery(getActivity(),query);
                    new FetchItemsTask(query).execute();
                    searchView.onActionViewCollapsed();
                    hideKeyboard(getView());
                }else {
                    isSearching = false;
                    isCleared = false;
                }

                return true;
            }



            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.trim().equals("")){
                    isSearching=false;
                    isCleared = false;
                    new FetchItemsTask().execute();
                }
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = MyPreferences.getStoredQuery(getActivity());
                searchView.setQuery(query,false);
            }
        });

        searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus) {
                    searchView.setIconified(true);
                    hideKeyboard(v);
                }
            }
        });
    }

    private void hideKeyboard(View v){
        Activity activity = getActivity();
        InputMethodManager imm = (InputMethodManager)activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.serach_clear:
                MyPreferences.setStoredQuery(getActivity(),null);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_fragment, null);

        mRecycler = v.findViewById(R.id.recycler);
        mRecycler.setLayoutManager(new GridLayoutManager(getActivity(),2));
        mRecycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    LinearLayoutManager mLayout = (LinearLayoutManager) recyclerView.getLayoutManager();
                    int visibleItemCount = mLayout.getChildCount();
                    int totalItemCount = mLayout.getItemCount();
                    int pastVisiblesItems = mLayout.findFirstVisibleItemPosition();

                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount-20) {
                        if(!isLoading) {
                            recyclerViewState = recyclerView.getLayoutManager().onSaveInstanceState();
                            isLoading = true;
                            if(!isSearching){
                                if (!(URLRequestToString.page == 10)) {
                                    new FetchItemsTask().execute();
                                } else {
                                    URLRequestToString.page = 1;
                                    new FetchItemsTask().execute();
                                }
                            }else {
                                if(usersQuery==null) return;
                                if (!(URLRequestToString.page == 10)) {
                                    new FetchItemsTask(usersQuery).execute();
                                } else {
                                    URLRequestToString.page = 1;
                                    new FetchItemsTask(usersQuery).execute();
                                }
                            }
                        }
                    }
                }
            }

        });

        fb = v.findViewById(R.id.floatingActionButton);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecycler.getLayoutManager().smoothScrollToPosition(mRecycler,null,0);
            }
        });
        mRecycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && fb.getVisibility() == View.VISIBLE) {
                    fb.hide();
                } else if (dy < 0 && fb.getVisibility() != View.VISIBLE) {
                    fb.show();
                }

            }
        });
        progressBar = v.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
        return v;
    }

    private void setupAdapter() {
        if (mAdapter == null) {
            mAdapter = new PhotoAdapter(getActivity());
            mAdapter.setItems(gallery_items);
            mRecycler.setAdapter(mAdapter);

        }else if(isSearching){
           addItems();
        }else {
           addItems();
        }
    }

    private void addItems(){
        if(!isCleared) {
            mAdapter = new PhotoAdapter(getActivity());
            mRecycler.setAdapter(mAdapter);
            isCleared = true;
        }
        mAdapter.setItems(gallery_items);
        //mAdapter.notifyItemRangeChanged(0,mAdapter.getItemCount());
        mRecycler.getLayoutManager().onRestoreInstanceState(recyclerViewState);
    }

    //-----------------------------------FETCH CLASS------------------------------------------------
    public class FetchItemsTask extends AsyncTask<Void, Void, List<GalleryItem>> {
        String query;

        public FetchItemsTask(String query) {
            this.query = query;
        }

        public FetchItemsTask() {
        }


        @Override
        protected void onPreExecute() {
            if(progressBar!=null){
                progressBar.setVisibility(View.VISIBLE);}
        }

        @Override
        protected List<GalleryItem> doInBackground(Void... voids) {

            if(query==null)
                return new URLRequestToString().fetchRecentPhotos();
            else
                return new URLRequestToString().searchPhotos(query);
        }

        @Override
        protected void onPostExecute(List<GalleryItem> galleryItems) {

            if (gallery_items == null) {
                gallery_items = galleryItems;
                current_gallery_size = gallery_items.size();
                setupAdapter();
                progressBar.setVisibility(View.GONE);
            } else {
                current_gallery_size = gallery_items.size();
                gallery_items = galleryItems;
                setupAdapter();
                progressBar.setVisibility(View.GONE);
            }
            isLoading = false;
        }
    }
    //----------------------------------------------------------------------------------------------
}
