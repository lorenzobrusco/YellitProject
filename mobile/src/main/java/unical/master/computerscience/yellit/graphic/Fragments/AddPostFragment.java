package unical.master.computerscience.yellit.graphic.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.graphic.CircularLayout;

/**
    Created by Salvatore on the 17/03/2017
 */
public class AddPostFragment extends Fragment {

    //@Bind(R.id.circular_layout)
    //CircularLayout circLayout;

    @Bind(R.id.pie_menu)
    PieChart mChart;

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        ButterKnife.bind(this, view);
        Log.d("post", "creating AddPostFragment");

        /* if (circLayout != null) {

            circLayout.setOnCircularItemClickListener(new CircularLayout.OnCircularItemClickListener() {
                @Override
                public void onCircularItemClick(int index) {
                    Toast.makeText(getContext(), "Item " + index + " clicked", Toast.LENGTH_SHORT).show();
                }
            });
        } */

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        mChart.setUsePercentValues(true);
        mChart.getDescription().setEnabled(false);
        mChart.setExtraOffsets(5, 10, 5, 5);

        mChart.setDragDecelerationFrictionCoef(10f);

        mChart.setCenterTextTypeface(mTfLight);
        mChart.setCenterText(generateCenterSpannableText());

        mChart.setDrawHoleEnabled(true);
        mChart.setHoleColor(Color.WHITE);

        mChart.setTransparentCircleColor(Color.WHITE);
        mChart.setTransparentCircleAlpha(110);

        mChart.setHoleRadius(39f);
        mChart.setTransparentCircleRadius(42f);

        mChart.setDrawCenterText(true);

        mChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        mChart.setRotationEnabled(true);
        mChart.setHighlightPerTapEnabled(true);

        // mChart.setUnit(" €");
        // mChart.setDrawUnitsInChart(true);

        // add a selection listener
        //mChart.setOnChartValueSelectedListener(this);

        setData(5, 100);

        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mChart.spin(2000, 0, 360);


        // entry label styling
        mChart.setEntryLabelColor(Color.WHITE);
        mChart.setEntryLabelTypeface(mTfRegular);
        mChart.setEntryLabelTextSize(12f);

        return view;
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("What r you doin'?\npowered by Yellit");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 17, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 17, s.length() - 18, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 17, s.length() - 18, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 17, s.length() - 17, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 17, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 17, s.length(), 0);
        return s;
    }

    private void setData(int count, float range) {

        float mult = range;

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < count ; i++) {
;
            entries.add(
                    new PieEntry(20,
                    mParties[i % mParties.length],
                    getResources().getDrawable(R.drawable.blui)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawValues(false);

        dataSet.setSliceSpace(3f);
        //dataSet.setsetIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();
    }
}
