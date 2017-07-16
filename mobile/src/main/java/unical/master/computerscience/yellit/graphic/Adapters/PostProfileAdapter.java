package unical.master.computerscience.yellit.graphic.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.like.LikeButton;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.Post;

/**
 *
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

    @Bind(R.id.like_post)
    LikeButton mLike;

    @Bind(R.id.position_post_text)
    TextView mPosition;

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
        final String likes = mMyPposts.get(i).getLikes()+" Likes";
        final String position = mMyPposts.get(i).getLocation()+"";
        personName.setText(InfoManager.getInstance().getmUser().getNickname());
        mComment.setText(mMyPposts.get(i).getComment());
        mData.setText(mMyPposts.get(i).getDate());
        mPosition.setText(position);
        nNumLikes.setText(likes);
        Glide.with(mContext)
                .load(InfoManager.getInstance().getmUser().getPathImg())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(mContext.getResources().getDrawable(R.drawable.default_user))
                .into(userImage);
        Glide.with(mContext)
                .load(mMyPposts.get(i).getPostImagePost())
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imagePost);
        return mView;
    }
}
