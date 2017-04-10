package unical.master.computerscience.yellit.graphic.custom;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.ImageView;

import unical.master.computerscience.yellit.R;

@SuppressLint("AppCompatCustomView")
public class SelectorImageView extends ImageView {

    private Context mContext;
    private boolean mSelected;

    public SelectorImageView(Context context) {
        this(context, null);
        this.mContext = context;
        this.mSelected = false;
    }

    public SelectorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }

    public SelectorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

    }


    public Context getmContext() {
        return mContext;
    }

    public void setmContext(Context mContext) {
        this.mContext = mContext;
    }

    public boolean ismSelected() {
        return mSelected;
    }

    public void setmSelected() {
        this.mSelected = !mSelected;
    }
}