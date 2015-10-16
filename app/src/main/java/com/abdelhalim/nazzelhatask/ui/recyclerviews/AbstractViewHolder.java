package com.abdelhalim.nazzelhatask.ui.recyclerviews;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abdelhalim.nazzelhatask.data_modules.AbstractDataModule;

/**
 * Created by Abd El-Halim on 10/14/2015.
 */
public abstract class AbstractViewHolder extends RecyclerView.ViewHolder {

    public AbstractViewHolder(View itemView) {
        super(itemView);
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return handleLongPress(v);
            }
        });
    }

    protected abstract void bindViewHolder(AbstractDataModule dataModule);

    protected abstract boolean handleLongPress(View view);
}
