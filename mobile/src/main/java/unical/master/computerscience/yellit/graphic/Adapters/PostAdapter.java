package unical.master.computerscience.yellit.graphic.Adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.logic.Objects.Post;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.utiliies.BaseURL;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class PostAdapter extends RecyclerView.Adapter <PostAdapter.PostViewHolder> {

    private static final String BASEURL = "http://10.0.2.2:8080/HobbiesServer/";
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
        holder.personName.setText(mPosts.get(position).getUser().getFullName());
    }


    @Override
    public int getItemCount() {
        return this.mPosts.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.cardview_post) CardView mCardView;
        @Bind(R.id.full_name_post) TextView personName;

        @Bind(R.id.image_profile_post)ImageView userImage;
        @Bind(R.id.image_value_post) ImageView imagePost;
        @Bind(R.id.video_value_post)VideoView videoPost;

        public PostViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            Glide.with(mContext)
                    .load(BaseURL.URL + "Images/banana.jpg")
                    .into(imagePost);
        }
    }

}
