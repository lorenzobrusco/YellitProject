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
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.R;

/**
    Created by Salvatore on the 17/03/2017
 */
public class AddPostFragment extends Fragment implements OnChartValueSelectedListener {

    /* if (circLayout != null) {

       circLayout.setOnCircularItemClickListener(new CircularLayout.OnCircularItemClickListener() {
         @Override
         public void onCircularItemClick(int index) {
               Toast.makeText(getContext(), "Item " + index + " clicked", Toast.LENGTH_SHORT).show();
          }
         });
    } */
    //@Bind(R.id.circular_layout)
    //CircularLayout circLayout;

    @Bind(R.id.pie_menu)
    PieChart mainMenu;

    //@Bind(R.id.sub_menu)
    PieChart subMenu;

    protected Typeface mTfRegular;
    protected Typeface mTfLight;
    protected String[] mParties = new String[] {
            "Party A", "Party B", "Party C", "Party D", "Party E", "Party F", "Party G", "Party H",
            "Party I", "Party J", "Party K", "Party L", "Party M", "Party N", "Party O", "Party P",
            "Party Q", "Party R", "Party S", "Party T", "Party U", "Party V", "Party W", "Party X",
            "Party Y", "Party Z"
    };

    Animation expandIn;
    Animation expandOut;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        ButterKnife.bind(this, view);
        Log.d("post", "creating AddPostFragment");

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        expandIn = AnimationUtils.loadAnimation(getContext(), R.anim.expand_in);
        expandOut = AnimationUtils.loadAnimation(getContext(), R.anim.expand_out);

        Animation.AnimationListener animation1Listener = new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mainMenu.startAnimation(expandOut);
            }
        };

        expandIn.setAnimationListener(animation1Listener);

        buildMainMenu();

        //buildSubMenu();

        return view;
    }

    public void buildMainMenu() {

        mainMenu.setBackgroundColor(Color.TRANSPARENT);

        mainMenu.setUsePercentValues(true);
        mainMenu.getDescription().setEnabled(false);
        mainMenu.setExtraOffsets(5, 10, 5, 5);

        mainMenu.setDragDecelerationFrictionCoef(0.9f);

        mainMenu.setCenterTextTypeface(mTfLight);
        mainMenu.setCenterText(generateCenterSpannableText());

        mainMenu.setDrawHoleEnabled(true);
        mainMenu.setHoleColor(Color.WHITE);

        mainMenu.setTransparentCircleColor(Color.WHITE);
        mainMenu.setTransparentCircleAlpha(110);

        mainMenu.setHoleRadius(42f);
        mainMenu.setTransparentCircleRadius(45f);

        mainMenu.setDrawCenterText(true);

        mainMenu.setRotationAngle(0);
        // enable rotation of the chart by touch
        mainMenu.setRotationEnabled(true);
        mainMenu.setHighlightPerTapEnabled(true);

        // mainMenu.setUnit(" €");
        // mainMenu.setDrawUnitsInChart(true);

        // add a selection listener
        mainMenu.setOnChartValueSelectedListener(this);

        setData(5, mainMenu, ColorTemplate.MATERIAL_COLORS);

        //mainMenu.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        // mainMenu.spin(2000, 0, 360);

        mainMenu.getLegend().setEnabled(false);

        // entry label styling
        mainMenu.setEntryLabelColor(Color.WHITE);
        mainMenu.setEntryLabelTypeface(mTfRegular);
        mainMenu.setEntryLabelTextSize(12f);
    }

    public void buildSubMenu() {

        subMenu.setBackgroundColor(Color.BLUE);

        moveOffScreen();

        subMenu.setUsePercentValues(true);
        subMenu.getDescription().setEnabled(false);

        subMenu.setCenterTextTypeface(mTfLight);
        subMenu.setCenterText(generateCenterSpannableText());

        subMenu.setDrawHoleEnabled(true);
        subMenu.setHoleColor(Color.WHITE);

        subMenu.setTransparentCircleColor(Color.WHITE);
        subMenu.setTransparentCircleAlpha(110);

        subMenu.setHoleRadius(58f);
        subMenu.setTransparentCircleRadius(61f);

        subMenu.setDrawCenterText(true);

        subMenu.setRotationEnabled(false);
        subMenu.setHighlightPerTapEnabled(true);

        subMenu.setMaxAngle(180f); // HALF CHART
        subMenu.setRotationAngle(180f);
        subMenu.setCenterTextOffset(0, -20);

        setData(4, subMenu, ColorTemplate.VORDIPLOM_COLORS);

        subMenu.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        subMenu.getLegend().setEnabled(false);

        // entry label styling
        subMenu.setEntryLabelColor(Color.WHITE);
        subMenu.setEntryLabelTypeface(mTfRegular);
        subMenu.setEntryLabelTextSize(12f);

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

    private void setData(int count, PieChart chart, int[] colorz) {

        float mult = 100;   //range

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
        dataSet.setSelectionShift(10f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : colorz)
            colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(16f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    private void moveOffScreen() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int height = display.getHeight();  // deprecated

        int offset = (int)(height * 0.65); /* percent to move */

        RelativeLayout.LayoutParams rlParams =
                (RelativeLayout.LayoutParams) subMenu.getLayoutParams();

        rlParams.setMargins(0, 0, 0, -offset);
        subMenu.setLayoutParams(rlParams);
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        mainMenu.startAnimation(expandIn);

        setData(5, mainMenu, ColorTemplate.LIBERTY_COLORS);

        int index = (int) h.getX();

        switch(index)
        {
            case 0:
                Log.e("VAL ", "" + index);

                // /mainMenu.animateX(1000, Easing.EasingOption.EaseOutCirc);
                //mainMenu.spin(2000, 0, 360);
                break;
            case 1:
                Log.e("VAL ", "" + index);

                // mainMenu.animateX(500);
                break;
            case 2:
                //mainMenu.animateX(1000, Easing.EasingOption.EaseInOutBack);
                break;
            case 3:
                //mainMenu.animateX(1000, Easing.EasingOption.EaseInOutQuart);
                break;
            case 4:
                //mainMenu.animateX(1000, Easing.EasingOption.EaseInOutExpo);
                break;

        }

        /* Log.i("VAL SELECTED",
                "Value: " + e.getX() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex()); */
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
