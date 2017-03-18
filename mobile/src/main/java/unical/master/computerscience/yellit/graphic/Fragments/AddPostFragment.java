package unical.master.computerscience.yellit.graphic.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.CircularLayout;

/**
    Created by Salvatore on the 17/03/2017
 */
public class AddPostFragment extends Fragment {

    @Bind(R.id.circular_layout)
    CircularLayout circLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        ButterKnife.bind(this, view);
        Log.d("post", "creating AddPostFragment");

        if (circLayout != null) {

            circLayout.setOnCircularItemClickListener(new CircularLayout.OnCircularItemClickListener() {
                @Override
                public void onCircularItemClick(int index) {
                    Toast.makeText(getContext(), "Item " + index + " clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}
