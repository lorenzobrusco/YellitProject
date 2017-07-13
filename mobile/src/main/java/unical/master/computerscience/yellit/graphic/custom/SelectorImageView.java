package unical.master.computerscience.yellit.graphic.custom;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 *
 */
@SuppressLint("AppCompatCustomView")
public class SelectorImageView extends ImageView {

    private Context mContext;
    private boolean mSelected;

    /**
     * @param context of activity
     */
    public SelectorImageView(Context context) {
        this(context, null);
        this.mContext = context;
        this.mSelected = false;
    }

    /**
     * @param context of activity
     * @param attrs
     */
    public SelectorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.mContext = context;
    }


    /**
     * @param context of activity
     * @param attrs
     * @param defStyleAttr
     */
    public SelectorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

    }

    /**
     * @return if an elemet is selected
     */
    public boolean ismSelected() {
        return mSelected;
    }

    /**
     * set if an elemet is selected
     */
    public void setmSelected() {
        this.mSelected = !mSelected;
    }
}