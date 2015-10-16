package com.abdelhalim.nazzelhatask.ui.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.abdelhalim.nazzelhatask.R;
import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;

import java.util.List;

/**
 * Created by Abd El-Halim on 10/14/2015.
 */
public class RepositoryRecyclerViewAdapter extends RecyclerView.Adapter<RepositoryViewHolder> {
    private List<AbstractDataModule> dataModules;

    public RepositoryRecyclerViewAdapter(List<AbstractDataModule> dataModules) {
        this.dataModules = dataModules;
    }

    public void updateDataList(List<AbstractDataModule> dataModules) {
        notifyItemRangeInserted(this.dataModules.size() - 1, dataModules.size());
        this.dataModules.addAll(dataModules);
    }

    public void clearDataList() {
        notifyItemRangeRemoved(0, dataModules.size());
        dataModules.clear();
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RepositoryViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recycle_view_row_repository, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder repositoryViewHolder, int i) {
        repositoryViewHolder.bindViewHolder(dataModules.get(i));
    }

    @Override
    public int getItemCount() {
        return dataModules.size();
    }
}
