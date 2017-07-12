package unical.master.computerscience.yellit.graphic.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import unical.master.computerscience.yellit.connection.PostGestureService;
import unical.master.computerscience.yellit.graphic.custom.PaddingItemDecoration;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Adapters.PostAdapter;
import unical.master.computerscience.yellit.utilities.BaseURL;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class PostFragment extends Fragment {

    @Bind(R.id.recycleview_posts)
    protected RecyclerView mPosts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_posts, container, false);
        ButterKnife.bind(this, view);

        /**
         * Decomment to have actual posts from database
         */

        this.initList();
        /**
         * Decomment to have fake posts for debugging purpose
         * final PostAdapter mPostAdapter = new PostAdapter(PostFragment.this.getContext(), FAKE_initList());
         * mPosts.setAdapter(mPostAdapter);
         */

        mPosts.addItemDecoration(new PaddingItemDecoration(170));
        mPosts.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }


    private List<Post> initList() {

        final List<Post> postsToShow = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseURL.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        PostGestureService postService = retrofit.create(PostGestureService.class);
        Call<Post[]> call = postService.getAllPosts("getAll");
        call.enqueue(new Callback<Post[]>() {
            @Override
            public void onResponse(Call<Post[]> call, Response<Post[]> response) {

                Post[] posts = response.body();

                if (posts != null)
                    for (Post p : posts) {
                        postsToShow.add(p);
                    }

                Log.d("Posters",posts.length+"");
                InfoManager.getInstance().setmPostList(postsToShow);
                InfoManager.getInstance().setmPostFilteredList(postsToShow);
                final PostAdapter mPostAdapter = new PostAdapter(PostFragment.this.getContext(), postsToShow);
                InfoManager.getInstance().setmPostAdapter(mPostAdapter);
                mPosts.setAdapter(mPostAdapter);

            }

            @Override
            public void onFailure(Call<Post[]> call, Throwable t) {

                Log.e("On failure", "Post looking for");
                t.printStackTrace();
            }
        });

        return postsToShow;
    }

    private List<Post> FAKE_initList() {

        List<Post> posts = new ArrayList<Post>();

        posts.add(new Post("Lorenzo Brusco"));
        posts.add(new Post("Salvatore Isabella"));
        posts.add(new Post("Francesco Cosco"));
        posts.add(new Post("Francesca Tassoni"));
        posts.add(new Post("Eliana Cannella"));
        posts.add(new Post("Paola Arcuri"));

        InfoManager.getInstance().setmPostList(posts);
        InfoManager.getInstance().setmPostFilteredList(posts);
        final PostAdapter mPostAdapter = new PostAdapter(PostFragment.this.getContext(), posts);
        InfoManager.getInstance().setmPostAdapter(mPostAdapter);
        mPosts.setAdapter(mPostAdapter);

        return posts;
    }

}
