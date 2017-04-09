package unical.master.computerscience.yellit.graphic.Fragments;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
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
import unical.master.computerscience.yellit.graphic.Adapters.PostAdapter;
import unical.master.computerscience.yellit.graphic.Adapters.PostProfileAdapter;
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

    @Bind(R.id.my_posts_profile)
    protected ExpandableHeightListView mPosts;

    @Bind(R.id.radar_chart_profile)
    RadarChart mRadarInfo;

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

            private String[] mActivities = new String[]{"Fitness", "Inside", "Outside", "Travel", "Food&Drink"};

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

        RadarDataSet set1 = new RadarDataSet(entries1, "Last Week");
        set1.setColor(Color.rgb(103, 110, 129));
        set1.setFillColor(Color.rgb(103, 110, 129));
        set1.setDrawFilled(true);
        set1.setFillAlpha(180);
        set1.setLineWidth(2f);
        set1.setDrawHighlightCircleEnabled(true);
        set1.setDrawHighlightIndicators(true);

        RadarDataSet set2 = new RadarDataSet(entries2, "This Week");
        set2.setColor(Color.rgb(121, 162, 175));
        set2.setFillColor(Color.rgb(121, 162, 175));
        set2.setDrawFilled(true);
        set2.setFillAlpha(180);
        set2.setLineWidth(2f);
        set2.setDrawHighlightCircleEnabled(true);
        set2.setDrawHighlightIndicators(true);

        ArrayList<IRadarDataSet> sets = new ArrayList<IRadarDataSet>();
        sets.add(set1);
        sets.add(set2);

        RadarData data = new RadarData(sets);
        data.setValueTextSize(8f);
        data.setDrawValues(false);
        data.setValueTextColor(Color.WHITE);

        mRadarInfo.setData(data);
        mRadarInfo.invalidate();

    }


}
