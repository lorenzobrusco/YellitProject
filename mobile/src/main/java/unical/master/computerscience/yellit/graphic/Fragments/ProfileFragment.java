package unical.master.computerscience.yellit.graphic.Fragments;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Adapters.PostAdapter;
import unical.master.computerscience.yellit.logic.objects.Post;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Lorenzo on 16/03/2017.
 */

public class ProfileFragment extends Fragment {

    @Bind(R.id.expand_fitness_button)
    protected Button mFitnessButton;

    @Bind(R.id.expand_info_button)
    protected Button mInfoButton;

    @Bind(R.id.expand_more_info_button)
    protected Button mMoreInfoButton;

    @Bind(R.id.fitness_info_layout_profile)
    protected LinearLayout mLayoutFitnessInfo;

    @Bind(R.id.info_layout_profile)
    protected LinearLayout mLayoutInfo;

    @Bind(R.id.more_info_layout_profile)
    protected LinearLayout mLayoutMoreInfo;

    private Animation mAnimationDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        mAnimationDown = AnimationUtils.loadAnimation(ProfileFragment.this.getContext(), R.anim.slide_down);
        setupButtonExpandLayout();
        return view;
    }

    private void setupButtonExpandLayout() {

        mFitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAnimationDown.reset();
                mLayoutFitnessInfo.clearAnimation();
                mLayoutFitnessInfo.startAnimation(mAnimationDown);
                mLayoutFitnessInfo.setVisibility(mLayoutFitnessInfo.isShown()
                        ? View.GONE : View.VISIBLE);
                mLayoutInfo.setVisibility(View.GONE);
                mLayoutMoreInfo.setVisibility(View.GONE);

            }
        });
        mInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAnimationDown.reset();
                mLayoutInfo.clearAnimation();
                mLayoutInfo.startAnimation(mAnimationDown);
                mLayoutInfo.setVisibility(mLayoutInfo.isShown()
                        ? View.GONE : View.VISIBLE);
                mLayoutFitnessInfo.setVisibility(View.GONE);
                mLayoutMoreInfo.setVisibility(View.GONE);

            }
        });
        mMoreInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mAnimationDown.reset();
                mLayoutMoreInfo.clearAnimation();
                mLayoutMoreInfo.startAnimation(mAnimationDown);
                mLayoutMoreInfo.setVisibility(mLayoutMoreInfo.isShown()
                        ? View.GONE : View.VISIBLE);
                mLayoutFitnessInfo.setVisibility(View.GONE);
                mLayoutInfo.setVisibility(View.GONE);

            }
        });

    }

}
