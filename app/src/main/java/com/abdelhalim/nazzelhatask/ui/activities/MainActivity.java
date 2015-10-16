package com.abdelhalim.nazzelhatask.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.abdelhalim.nazzelhatask.auth.Session;
import com.abdelhalim.nazzelhatask.utilities.connection.ConnectionState;
import com.abdelhalim.nazzelhatask.Constants;
import com.abdelhalim.nazzelhatask.R;
import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;
import com.abdelhalim.nazzelhatask.ui.recyclerviews.RecyclerViewDividerDecorator;
import com.abdelhalim.nazzelhatask.ui.recyclerviews.RepositoryRecyclerViewAdapter;
import com.abdelhalim.nazzelhatask.cache.CacheResponseHelper;
import com.abdelhalim.nazzelhatask.utilities.connection.GithubApisConnectionUtilities;
import com.abdelhalim.nazzelhatask.utilities.JSONParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abd El-Halim on 10/10/2015.
 */
public class MainActivity extends AbstractAppBarActivity {

    private List<AbstractDataModule> dataList;
    private boolean isLoadingData;
    private ConnectionState connectionState;
    private GithubApisConnectionUtilities githubApisConnectionUtilities;
    private RepositoryRecyclerViewAdapter repositoryRecyclerViewAdapter;
    private ProgressBar loadingProgress;
    private DrawerLayout drawerLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        loadingProgress = (ProgressBar) findViewById(R.id.loading_progress);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);

        View headerView = getLayoutInflater().inflate(R.layout.navigation_header, null);
        final TextView userNameTV = (TextView) headerView.findViewById(R.id.user_name_tv);
        navigationView.addHeaderView(headerView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_item_sign_in:
                        startActivity(new Intent(MainActivity.this, AuthActivity.class));
                        drawerLayout.closeDrawers();
                        return true;
                }
                return false;
            }
        });

        drawerLayout.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                userNameTV.setText((Session.getCurrentSession().getUserName() == null ?
                        "Authenticated UserName" : Session.getCurrentSession().getUserName()));
            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.accent));

        dataList = new ArrayList<>();
        isLoadingData = false;

        repositoryRecyclerViewAdapter = new RepositoryRecyclerViewAdapter(dataList);
        recyclerView.setAdapter(repositoryRecyclerViewAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new RecyclerViewDividerDecorator(this));

        connectionState = new ConnectionState(GithubApisConnectionUtilities.getRepositoriesUrl());
        githubApisConnectionUtilities = new GithubApisConnectionUtilities();
        githubApisConnectionUtilities.setOnResponseParseCompleteListener(new GithubApisConnectionUtilities.OnResponseParseCompleteListener() {
            @Override
            public void onParseComplete(List<AbstractDataModule> dataList) {
                if (dataList != null) {
                    repositoryRecyclerViewAdapter.updateDataList(dataList);
                }
                isLoadingData = false;
                swipeRefreshLayout.setRefreshing(false);
                loadingProgress.setVisibility(View.INVISIBLE);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                restRepositories();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int visibleItemCount = recyclerView.getChildCount();
                int allItemsCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisible = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

                if (!isLoadingData && visibleItemCount + firstVisible + Constants.REFRESH_THRESHOLD >= allItemsCount) {
                    loadNewRepositories();
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                loadNewRepositories();
            }
        });
    }

    private void loadNewRepositories() {
        isLoadingData = true;
        loadingProgress.setVisibility(View.VISIBLE);
        githubApisConnectionUtilities.requestNextDataPage(connectionState, JSONParser.DataType.REPOSITORY, true);
    }

    private void restRepositories() {
        new CacheResponseHelper().clearCacheDirectory();
        connectionState.setNextPageURL(Constants.REPOSITORIES_URL + Constants.PER_PAGE_URL_PARAMETER_KEY + "=" + Constants.PER_PAGE_ITEMS);
        repositoryRecyclerViewAdapter.clearDataList();
        loadNewRepositories();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public int getContentView() {
        return R.layout.activity_main;
    }
}
