package unical.master.computerscience.yellit.graphic.custom;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * It used to give a margin top at the list of posts
 */
public class PaddingItemDecoration extends RecyclerView.ItemDecoration {
    private final int size;

    /**
     * @param size the height
     */
    public PaddingItemDecoration(int size) {
        this.size = size;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        /** Apply offset only to first item */
        if (parent.getChildAdapterPosition(view) == 0) {
            outRect.top += size;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
    }
}