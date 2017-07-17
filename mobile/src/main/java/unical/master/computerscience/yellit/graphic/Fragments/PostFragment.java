package unical.master.computerscience.yellit.graphic.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import unical.master.computerscience.yellit.graphic.Activities.LoadActivity;
import unical.master.computerscience.yellit.graphic.Activities.LoginSignupActivity;
import unical.master.computerscience.yellit.graphic.custom.PaddingItemDecoration;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Adapters.PostAdapter;
import unical.master.computerscience.yellit.utilities.BaseURL;
import unical.master.computerscience.yellit.utilities.PrefManager;

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
        mPosts.setAdapter(InfoManager.getInstance().getmPostAdapter());
        mPosts.addItemDecoration(new PaddingItemDecoration(170));
        mPosts.setLayoutManager(new LinearLayoutManager(this.getContext()));
        return view;
    }




}
