package com.abdelhalim.nazzelhatask.ui.recyclerviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.abdelhalim.nazzelhatask.R;

/**
 * Created by Abd El-Halim on 10/14/2015.
 */
public class RecyclerViewDividerDecorator extends RecyclerView.ItemDecoration {
    private Drawable dividerDrawable;
    private Context context;

    public RecyclerViewDividerDecorator(Context context) {
        this.context = context;
        dividerDrawable = context.getResources().getDrawable(R.drawable.divider);
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);

        int minMargin = context.getResources().getDimensionPixelSize(R.dimen.divider_margin_left_right);
        int left = parent.getPaddingLeft() + minMargin;
        int right = parent.getWidth() - parent.getPaddingRight() - minMargin;

        for (int i = 0; i < parent.getChildCount(); i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) child.getLayoutParams();

            int top = child.getBottom() + layoutParams.bottomMargin;
            int bottom = top + dividerDrawable.getIntrinsicHeight();

            dividerDrawable.setBounds(left, top, right, bottom);
            dividerDrawable.draw(c);
        }
    }
}
