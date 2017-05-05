package unical.master.computerscience.yellit.graphic.Fragments;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IRadarDataSet;
import com.github.paolorotolo.expandableheightlistview.ExpandableHeightListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.Adapters.PostProfileAdapter;
import unical.master.computerscience.yellit.graphic.custom.SelectorImageView;
import unical.master.computerscience.yellit.logic.GoogleApiClient;
import unical.master.computerscience.yellit.logic.objects.Friend;
import unical.master.computerscience.yellit.logic.objects.Post;

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

    @Bind(R.id.my_posts_profile)
    protected ExpandableHeightListView mPosts;

    @Bind(R.id.radar_chart_profile)
    RadarChart mRadarInfo;

    @Bind(R.id.friendsGridView)
    GridView mFriendsGridView;

    private List<Friend> mFriends;

    private Animation mAnimationDown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        mAnimationDown = AnimationUtils.loadAnimation(ProfileFragment.this.getContext(), R.anim.slide_down);
        setupButtonExpandLayout();
        final PostProfileAdapter mAdapter = new PostProfileAdapter(initList(), getContext());
        mPosts.setAdapter(mAdapter);
        mPosts.setExpanded(true);
        this.setupRadar();
        this.setupFriendGridView();
        GoogleApiClient.getInstance((AppCompatActivity) this.getActivity()).getLocation(this.getContext());
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

    private List<Friend> initFriends() {
        final List<Friend> friends = new ArrayList<>();
        friends.add(new Friend("Lorenzo Brusco", ""));
        friends.add(new Friend("Salvatore Isabella", ""));
        friends.add(new Friend("Francesco Cosco", ""));
        friends.add(new Friend("Francesca Tassoni", ""));
        friends.add(new Friend("Eliana Cannella", ""));
        friends.add(new Friend("Paola Arcuri", ""));
        friends.add(new Friend("Lorenzo Brusco", ""));
        friends.add(new Friend("Salvatore Isabella", ""));
        friends.add(new Friend("Francesco Cosco", ""));
        friends.add(new Friend("Francesca Tassoni", ""));
        friends.add(new Friend("Eliana Cannella", ""));
        friends.add(new Friend("Paola Arcuri", ""));
        friends.add(new Friend("Lorenzo Brusco", ""));
        friends.add(new Friend("Salvatore Isabella", ""));
        friends.add(new Friend("Francesco Cosco", ""));
        friends.add(new Friend("Francesca Tassoni", ""));
        friends.add(new Friend("Eliana Cannella", ""));
        friends.add(new Friend("Paola Arcuri", ""));
        friends.add(new Friend("Lorenzo Brusco", ""));
        friends.add(new Friend("Salvatore Isabella", ""));
        friends.add(new Friend("Francesco Cosco", ""));
        friends.add(new Friend("Francesca Tassoni", ""));
        friends.add(new Friend("Eliana Cannella", ""));
        friends.add(new Friend("Paola Arcuri", ""));
        return friends;
    }

    private void setupRadar() {
        mRadarInfo.getDescription().setEnabled(false);

        mRadarInfo.setWebLineWidth(1f);
        mRadarInfo.setWebColor(Color.LTGRAY);
        mRadarInfo.setWebLineWidthInner(1f);
        mRadarInfo.setWebColorInner(Color.LTGRAY);
        mRadarInfo.setWebAlpha(100);

        // create a custom MarkerView (extend MarkerView) and specify the layout
        // to use for it

        setData();

        mRadarInfo.animateXY(
                1400, 1400,
                Easing.EasingOption.EaseInOutQuad,
                Easing.EasingOption.EaseInOutQuad);

        XAxis xAxis = mRadarInfo.getXAxis();
        xAxis.setTextSize(9f);
        xAxis.setYOffset(0f);
        xAxis.setXOffset(0f);
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            private String[] mActivities = new String[]{"FITNESS", "INSIDE", "OUTSIDE", "TRAVEL", "FOOD&DRINK"};

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return mActivities[(int) value % mActivities.length];
            }
        });
        xAxis.setTextColor(Color.BLACK);

        YAxis yAxis = mRadarInfo.getYAxis();
        yAxis.setLabelCount(5, false);
        yAxis.setTextSize(9f);
        yAxis.setAxisMinimum(0f);
        yAxis.setAxisMaximum(80f);
        yAxis.setDrawLabels(false);

        Legend l = mRadarInfo.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setEnabled(false);
        l.setTextColor(Color.BLACK);
    }

    public void setData() {

        float mult = 80;
        float min = 20;
        int cnt = 5;

        ArrayList<RadarEntry> entries1 = new ArrayList<RadarEntry>();
        ArrayList<RadarEntry> entries2 = new ArrayList<RadarEntry>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; i < cnt; i++) {
            float val1 = (float) (Math.random() * mult) + min;
            entries1.add(new RadarEntry(val1));

            float val2 = (float) (Math.random() * mult) + min;
            entries2.add(new RadarEntry(val2));
        }

        RadarDataSet set1 = new RadarDataSet(entries1, "THIS WEEK");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(true);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mRadarInfo.setData(data);
        mRadarInfo.invalidate();

    }


    private void setupFriendGridView() {
        this.mFriends = this.initFriends();
        mFriendsGridView.setAdapter(new ImageAdapter(getActivity()));
        this.gridViewSetting(this.mFriendsGridView);
    }

    private void gridViewSetting(GridView gridview) {

        int size = mFriends.size();
        // Calculated single Item Layout Width for each grid element ....
        int width = 70;

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        float density = dm.density;

        int totalWidth = (int) (width * size * density);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                totalWidth, LinearLayout.LayoutParams.MATCH_PARENT);

        gridview.setLayoutParams(params);
        gridview.setStretchMode(GridView.STRETCH_SPACING);
        gridview.setNumColumns(size);
    }

    /**
     * The Class ImageAdapter.
     */
    private class ImageAdapter extends BaseAdapter {

        /**
         * The context.
         */
        private Activity context;

        /**
         * Instantiates a new image adapter.
         *
         * @param localContext the local context
         */
        public ImageAdapter(Activity localContext) {
            context = localContext;
        }

        public int getCount() {
            return mFriends.size();
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(final int position, final View convertView,
                            final ViewGroup parent) {
            String nameFriend = mFriends.get(position).getEmail();
            if (nameFriend.length() > 20)
                nameFriend = nameFriend.substring(0, 13) + "...";
            final LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            final CircleImageView picturesView = new CircleImageView(context);
            final TextView name = new TextView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.gravity = Gravity.CENTER_HORIZONTAL;
            name.setLayoutParams(lp);
            name.setText(nameFriend);
            name.setTextSize(9f);
            name.setTextColor(getResources().getColor(R.color.black));
            layout.setLayoutParams(new GridView.LayoutParams(200, 200));
            final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            picturesView.setLayoutParams(layoutParams);
            Glide.with(context).load(mFriends.get(position).getPath())
                    .centerCrop()
                    .error(getResources().getDrawable(R.mipmap.ic_launcher))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(picturesView);
            layout.addView(picturesView);
            layout.addView(name);
            return layout;
        }
    }


}
