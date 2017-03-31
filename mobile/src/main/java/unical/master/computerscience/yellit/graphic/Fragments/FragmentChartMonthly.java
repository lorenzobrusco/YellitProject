package unical.master.computerscience.yellit.graphic.Fragments;


import android.support.v4.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import im.dacer.androidcharts.LineView;
import unical.master.computerscience.yellit.R;

import java.util.ArrayList;

/**
 * Created by Lorenzo on 31/03/2017.
 */

public class FragmentChartMonthly extends Fragment {
    int randomint = 9;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_fitness_repo_monthly, container, false);
        final LineView lineViewFloat = (LineView)rootView.findViewById(R.id.chart_repo_lines);
        initLineView(lineViewFloat);
        randomSet(lineViewFloat);
        return rootView;
    }

    private void initLineView(LineView lineView) {
        ArrayList<String> test = new ArrayList<String>();
        for (int i=0; i<randomint; i++){
            test.add(String.valueOf(i+1));
        }
        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[]{Color.parseColor("#F44336"),Color.parseColor("#9C27B0"),Color.parseColor("#2196F3"),Color.parseColor("#009688")});
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);

    }

    private void randomSet( LineView lineViewFloat){

        ArrayList<Float> dataListF = new ArrayList<>();
        float randomF = (float)(Math.random()*9+1);
        for (int i=0; i<randomint; i++){
            dataListF.add((float)(Math.random()*randomF));
        }

        ArrayList<Float> dataListF2 = new ArrayList<>();
        randomF = (int)(Math.random()*9+1);
        for (int i=0; i<randomint; i++){
            dataListF2.add((float)(Math.random()*randomF));
        }

        ArrayList<Float> dataListF3 = new ArrayList<>();
        randomF = (int)(Math.random()*9+1);
        for (int i=0; i<randomint; i++){
            dataListF3.add((float)(Math.random()*randomF));
        }

        ArrayList<ArrayList<Float>> dataListFs = new ArrayList<>();
        dataListFs.add(dataListF);
        dataListFs.add(dataListF2);
        dataListFs.add(dataListF3);

        lineViewFloat.setFloatDataList(dataListFs);

    }
}
