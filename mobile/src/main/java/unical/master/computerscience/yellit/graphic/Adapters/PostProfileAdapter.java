package unical.master.computerscience.yellit.graphic.Adapters;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.util.List;
import java.util.zip.Inflater;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.utiliies.BaseURL;
import unical.master.computerscience.yellit.utiliies.WriteFile;

/**
 * Created by Lorenzo on 09/04/2017.
 */

public class PostProfileAdapter extends BaseAdapter {

    private List<Post> mMyPposts;
    private Context mContext;
    private static LayoutInflater inflater = null;

    @Bind(R.id.data_post)
    TextView mData;

    @Bind(R.id.comment_value_post)
    TextView mComment;

    @Bind(R.id.num_likes_post)
    TextView nNumLikes;

    @Bind(R.id.full_name_post)
    TextView personName;

    @Bind(R.id.image_profile_post)
    CircleImageView userImage;

    @Bind(R.id.image_value_post)
    ImageView imagePost;


    public PostProfileAdapter(List<Post> mMyPposts, Context mContext) {
        this.mMyPposts = mMyPposts;
        this.mContext = mContext;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mMyPposts.size();
    }

    @Override
    public Object getItem(int i) {
        return mMyPposts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return getItem(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View mView = inflater.inflate(R.layout.item_post_profile, null);
        ButterKnife.bind(this, mView);
        Glide.with(mContext)
                .load(BaseURL.URL + "Images/user.jpg")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }

                })
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(mContext.getResources().getDrawable(R.mipmap.ic_launcher))
                .into(userImage);
        Glide.with(mContext)
                .load(BaseURL.URL + "Images/pizza.jpg")
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        return false;
                    }
                })
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(mContext.getResources().getDrawable(R.mipmap.ic_launcher))
                .into(imagePost);
        return mView;
    }
}
