package unical.master.computerscience.yellit.graphic.Fragments;

import android.content.Context;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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

import android.os.Vibrator;
import android.widget.Toast;


/**
 * Created by Salvatore on the 17/03/2017
 */
public class AddPostFragment extends Fragment implements OnChartValueSelectedListener {

    @Bind(R.id.pie_menu)
    protected PieChart mainMenu;

    private Typeface mTfRegular;
    private Typeface mTfLight;
    private String[] mainCategoryLabels;
    private String[] subCategoryLabels;
    private int[] mainCategoryColors;
    private int[] subCategoryColors;

    private Animation expandIn;
    private Animation expandOut;

    private boolean isSubMenu;
    private int lastSubMenu;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post, container, false);
        ButterKnife.bind(this, view);
        Log.d("post", "creating AddPostFragment");

        mainCategoryLabels = getResources().getStringArray(R.array.main_categories);
        mainCategoryColors = getResources().getIntArray(R.array.pressed_colors);

        mTfRegular = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Regular.ttf");
        mTfLight = Typeface.createFromAsset(getActivity().getAssets(), "OpenSans-Light.ttf");

        expandIn = AnimationUtils.loadAnimation(getContext(), R.anim.expand_in);
        expandOut = AnimationUtils.loadAnimation(getContext(), R.anim.expand_out);

        isSubMenu = false;
        lastSubMenu = -1;

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

                if(isSubMenu)
                {
                    mainMenu.setCenterText(mainCategoryLabels[lastSubMenu]);
                    setData(subCategoryLabels, mainCategoryColors);
                }
                else
                {
                    mainMenu.setCenterText(generateCenterSpannableText());
                    setData(mainCategoryLabels, ColorTemplate.MATERIAL_COLORS);
                }

                mainMenu.startAnimation(expandOut);
            }
        };

        expandIn.setAnimationListener(animation1Listener);

        buildMainMenu();

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

        // add a selection listener
        mainMenu.setOnChartValueSelectedListener(this);

        setData(mainCategoryLabels, ColorTemplate.MATERIAL_COLORS);

        mainMenu.getLegend().setEnabled(false);

        // entry label styling
        mainMenu.setEntryLabelColor(Color.WHITE);
        mainMenu.setEntryLabelTypeface(mTfRegular);
        mainMenu.setEntryLabelTextSize(11f);
    }

    private SpannableString generateCenterSpannableText() {

        SpannableString s = new SpannableString("What are you doing?\npowered by Yellit");
        s.setSpan(new RelativeSizeSpan(1.7f), 0, 17, 0);
        s.setSpan(new StyleSpan(Typeface.NORMAL), 17, s.length() - 18, 0);
        s.setSpan(new ForegroundColorSpan(Color.GRAY), 17, s.length() - 18, 0);
        s.setSpan(new RelativeSizeSpan(.8f), 17, s.length() - 17, 0);
        s.setSpan(new StyleSpan(Typeface.ITALIC), s.length() - 17, s.length(), 0);
        s.setSpan(new ForegroundColorSpan(ColorTemplate.getHoloBlue()), s.length() - 17, s.length(), 0);
        return s;
    }

    private void setData(String[] labels, int[] colorz) {

        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        for (int i = 0; i < labels.length; i++) {

            entries.add(
                    new PieEntry(20,
                            labels[i],
                            getResources().getDrawable(R.drawable.blui)));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawValues(false);

        dataSet.setSliceSpace(3f);
        //dataSet.setsetIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(11f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : colorz)
            colors.add(c);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(40f);
        data.setValueTextColor(Color.WHITE);
        data.setValueTypeface(mTfLight);
        mainMenu.setData(data);

        // undo all highlights
        mainMenu.highlightValues(null);

        mainMenu.invalidate();
    }

    /*
        In base all'index del bottone premuto carichiamo uno specifico sub menù, secondo l'ordine:

        Category 1 = Index 0
        Category 2 = Index 1
        etc
     */
    @Override
    public void onValueSelected(Entry e, Highlight h) {

        if (e == null)
            return;

        if (isSubMenu) {

            switch(lastSubMenu)
            {
                case 0:
                    handleSubMenu1(e, h);
                    break;
                case 1:
                    handleSubMenu2(e, h);
                    break;
                case 2:
                    handleSubMenu3(e, h);
                    break;
                case 3:
                    handleSubMenu4(e, h);
                    break;
                case 4:
                    handleSubMenu5(e, h);
                    break;
            }

        } else {

            // Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            // v.vibrate(80); // milliseconds

            int index = (int) h.getX();

            switch (index) {
                case 0:
                    Log.i("VAL SELECTED",
                            "Value: " + e.getData() + ", index: " + h.getY()
                                    + ", DataSet index: " + h.getDataSetIndex());
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_1);
                    break;
                case 1:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_2);
                    break;
                case 2:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_3);
                    break;
                case 3:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_4);
                    break;
                case 4:
                    subCategoryLabels = getResources().getStringArray(R.array.sub_categories_5);
                    break;
            }

            isSubMenu = true;
            lastSubMenu = index;
            mainMenu.startAnimation(expandIn);
        }
        /* Log.i("VAL SELECTED",
                "Value: " + e.getX() + ", index: " + h.getX()
                        + ", DataSet index: " + h.getDataSetIndex()); */
    }

    private void handleSubMenu1(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            case 0:
            case 1:
            case 2:
            case 3:
                Toast.makeText(getContext(), e.getY() + "", Toast.LENGTH_SHORT);
                break;

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu2(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu3(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu4(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    private void handleSubMenu5(Entry e, Highlight h) {

        int index = (int) h.getX();

        switch (index) {

            default:
                isSubMenu = false;
                lastSubMenu = -1;
                mainMenu.startAnimation(expandIn);
                break;
        }
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

}
