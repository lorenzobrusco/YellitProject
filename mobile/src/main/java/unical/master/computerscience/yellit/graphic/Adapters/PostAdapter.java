package unical.master.computerscience.yellit.graphic.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceylonlabs.imageviewpopup.ImagePopup;
import com.like.LikeButton;
import com.like.OnLikeListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unical.master.computerscience.yellit.connection.LikeService;
import unical.master.computerscience.yellit.graphic.Activities.LoginActivity;
import unical.master.computerscience.yellit.graphic.custom.Rotate3dAnimation;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.Like;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.logic.objects.User;
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
    public void onBindViewHolder(PostViewHolder holder, final int position) {
        holder.personName.setText(mPosts.get(position).getUserName());
        holder.mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
                Toast.makeText(mContext, position+" liked something", Toast.LENGTH_LONG
                ).show();
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseURL.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                LikeService likeService = retrofit.create(LikeService.class);
                Call<Like> call = likeService.addLike(InfoManager.getInstance().getmUser().getEmail(), mPosts.get(position).getIdPost() );
                call.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {

                        final Like like = response.body();

                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        Log.d("retrofit", "errore di like");
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseURL.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                LikeService likeService = retrofit.create(LikeService.class);
                Call<Like> call = likeService.removeLike(InfoManager.getInstance().getmUser().getEmail(), mPosts.get(position).getIdPost() );
                call.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {

                        Like like = response.body();

                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        Log.d("retrofit", "errore di like");
                    }
                });
            }
        });
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
        //        @Bind(R.id.like_post)
//        ImageView like;
        @Bind(R.id.like_post)
        LikeButton mLikeButton;
        private boolean isLike = false;

        public PostViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Glide.with(mContext)
//                    .load(BaseURL.URL + "Images/user.jpg")
                    .load("http://static.wixstatic.com/media/9ee4ab_b0c4a4d968c64d74b20415843218d895.png")
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
//                    .error(mContext.getResources().getDrawable(R.mipmap.ic_launcher))
                    .into(userImage);
            Glide.with(mContext)
//                    .load(BaseURL.URL + "Images/pizza.jpg")
                    .load("https://upload.wikimedia.org/wikipedia/commons/a/a3/Eq_it-na_pizza-margherita_sep2005_sml.jpg")
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

        }

    }

}
