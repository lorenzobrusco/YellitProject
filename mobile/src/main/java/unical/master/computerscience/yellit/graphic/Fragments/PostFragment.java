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
import unical.master.computerscience.yellit.graphic.custom.PaddingItemDecoration;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.logic.objects.User;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Adapters.PostAdapter;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class PostFragment extends Fragment {

    @Bind(R.id.recycleview_posts)
    RecyclerView mPosts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        ButterKnife.bind(this, view);
        Log.d("post", "create");
        final PostAdapter mPostAdapter = new PostAdapter(this.getContext(), initList());
        mPosts.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mPosts.setAdapter(mPostAdapter);
        mPosts.addItemDecoration( new PaddingItemDecoration(170));
        return view;
    }


    private List<Post> initList() {
        final List<Post> posts = new ArrayList<>();
        posts.add(new Post("Lorenzo Brusco"));
        posts.add(new Post("Salvatore Isabella"));
        posts.add(new Post("Francesco Cosco"));
        posts.add(new Post("Francesca Tassoni"));
        posts.add(new Post("Eliana Cannella"));
        posts.add(new Post("Paola Arcuri"));
        return posts;
    }

}
