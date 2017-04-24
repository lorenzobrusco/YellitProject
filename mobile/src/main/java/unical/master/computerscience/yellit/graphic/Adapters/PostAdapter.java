package unical.master.computerscience.yellit.graphic.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import unical.master.computerscience.yellit.graphic.custom.Rotate3dAnimation;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.utilities.BaseURL;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    public PostAdapter(final Context context, final List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, null);
        PostViewHolder mPost = new PostViewHolder(mView);
        return mPost;
    }

    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.personName.setText(mPosts.get(position).getUserName());
    }


    @Override
    public int getItemCount() {
        return this.mPosts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_info_post)
        LinearLayout userInfo;
        @Bind(R.id.current_action_post)
        RelativeLayout actionUser;
        @Bind(R.id.comment_content_post)
        RelativeLayout commentUser;
        @Bind(R.id.cardview_post)
        CardView mCardView;
        @Bind(R.id.full_name_post)
        TextView personName;
        @Bind(R.id.image_profile_post)
        CircleImageView userImage;
        @Bind(R.id.image_value_post)
        ImageView imagePost;
        @Bind(R.id.video_value_post)
        VideoView videoPost;
        @Bind(R.id.load_post)
        ProgressBar progressBar;
        @Bind(R.id.like_post)
        ImageView like;
        private boolean isLike = false;

        public PostViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Glide.with(mContext)
                    .load(BaseURL.URL + "Images/user.jpg")
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            userInfo.setVisibility(View.VISIBLE);
                            actionUser.setVisibility(View.VISIBLE);
                            commentUser.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            userInfo.setVisibility(View.VISIBLE);
                            actionUser.setVisibility(View.VISIBLE);
                            commentUser.setVisibility(View.VISIBLE);
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
                            progressBar.setVisibility(View.GONE);
                            userInfo.setVisibility(View.VISIBLE);
                            actionUser.setVisibility(View.VISIBLE);
                            commentUser.setVisibility(View.VISIBLE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            userInfo.setVisibility(View.VISIBLE);
                            actionUser.setVisibility(View.VISIBLE);
                            commentUser.setVisibility(View.VISIBLE);
                            return false;
                        }
                    })
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(mContext.getResources().getDrawable(R.mipmap.ic_launcher))
                    .into(imagePost);
            final ImagePopup imagePopup = new ImagePopup(mContext);
            imagePopup.setBackgroundColor(Color.TRANSPARENT);
            imagePost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Initiate Popup view **/
                    final Dialog dialog = new Dialog(mContext);
                    dialog.setContentView(R.layout.dialog_image_popup);
                    ImageView imageView = (ImageView) dialog.findViewById(R.id.image_popup);
                    imageView.setImageDrawable(imagePost.getDrawable());
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            });
            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }

            });
            like.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    final float centerX = like.getWidth() / 2.0f;
                    final float centerY = like.getHeight() / 2.0f;
                    final Rotate3dAnimation rotation = new Rotate3dAnimation(0, 360, centerX, centerY, 0f, false);
                    rotation.setDuration(500);
                    rotation.setFillAfter(false);
                    rotation.setInterpolator(new AccelerateInterpolator());
                    rotation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                                like.setImageDrawable(isLike ? mContext.getResources().getDrawable(R.mipmap.ic_no_like)
                                        : mContext.getResources().getDrawable(R.mipmap.ic_like));
                            isLike = !isLike;
                            //WriteFile.getInstance().writenTot(mContext,BaseURL.FILENAME,"1");
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    like.startAnimation(rotation);
                    return false;
                }
            });

        }

    }

}
