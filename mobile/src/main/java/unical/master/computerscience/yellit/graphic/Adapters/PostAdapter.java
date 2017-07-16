package unical.master.computerscience.yellit.graphic.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.GsonBuilder;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

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
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.Like;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.GenerateMainCategories;

/**
 * Post adapter
 */
public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private Context mContext;
    private List<Post> mPosts;

    /**
     * Costructor
     *
     * @param context of activity
     * @param posts   list of posts
     */
    public PostAdapter(final Context context, final List<Post> posts) {
        this.mContext = context;
        this.mPosts = posts;
    }

    /**
     * @param viewGroup
     * @param viewType
     * @return
     */
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, null);
        PostViewHolder mPost = new PostViewHolder(mView);
        return mPost;
    }

    /**
     * @param holder
     * @param position of post in the list
     */
    @Override
    public void onBindViewHolder(final PostViewHolder holder, final int position) {
        final Post currPost = mPosts.get(position);
        final String likes = currPost.getLikes() + " Like";
        holder.personName.setText(currPost.getUserName());
        holder.commentText.setText(currPost.getComment());
        if (currPost.getComment() == null || currPost.getComment().equals(""))
            holder.commentText.setVisibility(View.GONE);
        holder.setImagePost(currPost.getPostImagePost());
        holder.setUserImage(currPost.getUserImagePath());
        holder.mType.setText(GenerateMainCategories.getMacro(mContext, currPost.getType()));
        holder.mLikeContent.setText(likes);
        holder.mDataPost.setText(currPost.getDate());
        holder.mPosition.setText(currPost.getLocation());
        this.isLike(InfoManager.getInstance().getmUser().getEmail(), mPosts.get(position).getIdPost(), holder);
        holder.mLikeButton.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseURL.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final LikeService likeService = retrofit.create(LikeService.class);
                final Call<Like> call = likeService.addLike(InfoManager.getInstance().getmUser().getEmail(), mPosts.get(position).getIdPost() + "", mPosts.get(position).getUserName(), "1");
                call.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {
                        final Like like = response.body();
                        holder.setNumberOfLikes(like.getCount());
                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        Toast.makeText(mContext, "Error during add a like", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl(BaseURL.URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
                final LikeService likeService = retrofit.create(LikeService.class);
                final Call<Like> call = likeService.addLike(InfoManager.getInstance().getmUser().getEmail(), mPosts.get(position).getIdPost() + "", mPosts.get(position).getUserName(), "0");
                call.enqueue(new Callback<Like>() {
                    @Override
                    public void onResponse(Call<Like> call, Response<Like> response) {
                        final Like like = response.body();
                        holder.setNumberOfLikes(like.getCount());
                    }

                    @Override
                    public void onFailure(Call<Like> call, Throwable t) {
                        Toast.makeText(mContext, "Error during remove a like", Toast.LENGTH_SHORT).show();
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

    /**
     * change the list of posts
     *
     * @param posts
     */
    public void changeList(List<Post> posts) {
        this.mPosts = posts;
    }

    /**
     * Check if user likes this post
     *
     * @param email
     * @param id
     * @return
     */
    private boolean isLike(final String email, final Integer id, final PostViewHolder holder) {
        final boolean[] isLike = {false};
        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        final LikeService likeService = retrofit.create(LikeService.class);
        final Call<Like> call = likeService.isLike(email, id + "", "2");
        call.enqueue(new Callback<Like>() {
            @Override
            public void onResponse(Call<Like> call, Response<Like> response) {
                final Like like = response.body();
                if (like != null) {
                    if (like.getIsLike() == 1) {
                        holder.setLike(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<Like> call, Throwable t) {
                Toast.makeText(mContext, "Error during add a like", Toast.LENGTH_SHORT).show();
            }
        });
        return isLike[0];
    }

    /**
     * Adpter
     */
    class PostViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.user_info_post)
        LinearLayout userInfo;
        @Bind(R.id.current_action_post)
        RelativeLayout actionUser;
        @Bind(R.id.comment_content_post)
        RelativeLayout commentUser;
        @Bind(R.id.comment_value_post)
        TextView commentText;
        @Bind(R.id.cardview_post)
        CardView mCardView;
        @Bind(R.id.full_name_post)
        TextView personName;
        @Bind(R.id.image_profile_post)
        CircleImageView userImage;
        @Bind(R.id.image_value_post)
        ImageView imagePost;
        @Bind(R.id.load_post)
        ProgressBar progressBar;
        @Bind(R.id.like_post)
        LikeButton mLikeButton;
        @Bind(R.id.like_post_content)
        TextView mLikeContent;
        @Bind(R.id.type_post)
        TextView mType;
        @Bind(R.id.data_post)
        TextView mDataPost;
        @Bind(R.id.position_post_text)
        TextView mPosition;

        PostViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void setLike(boolean isLike) {
            mLikeButton.setLiked(isLike);
        }

        void setNumberOfLikes(int nLikes) {
            mLikeContent.setText(nLikes + " Likes");
        }

        void setUserImage(String userImgPath) {
            Glide.with(mContext)
                    .load(userImgPath)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(userImage);
        }

        void setImagePost(String imagePath) {
            hideAll();
            Glide.with(mContext)
                    .load(imagePath)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            showAll();
                            return false;
                        }
                    })
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imagePost);
        }

        /**
         * Show progress bar and hide all
         */
        public void hideAll() {
            progressBar.setVisibility(View.VISIBLE);
            personName.setVisibility(View.INVISIBLE);
            mDataPost.setVisibility(View.INVISIBLE);
            mLikeContent.setVisibility(View.INVISIBLE);
            mLikeButton.setVisibility(View.INVISIBLE);
            actionUser.setVisibility(View.INVISIBLE);
            userImage.setVisibility(View.INVISIBLE);
            commentUser.setVisibility(View.INVISIBLE);
        }

        /**
         * Hide progress bar and show all
         */
        public void showAll() {
            progressBar.setVisibility(View.GONE);
            personName.setVisibility(View.VISIBLE);
            mDataPost.setVisibility(View.VISIBLE);
            mLikeContent.setVisibility(View.VISIBLE);
            mLikeButton.setVisibility(View.VISIBLE);
            actionUser.setVisibility(View.VISIBLE);
            userImage.setVisibility(View.VISIBLE);
            commentUser.setVisibility(View.VISIBLE);
        }
    }

}
