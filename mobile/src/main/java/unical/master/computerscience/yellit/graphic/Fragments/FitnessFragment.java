package unical.master.computerscience.yellit.graphic.Fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.fitness.request.OnDataPointListener;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;

import org.json.JSONObject;

import java.util.ArrayList;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.dacer.androidcharts.LineView;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.logic.GoogleApiClient;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.utilities.RemoteFetch;
import xyz.matteobattilana.library.Common.Constants;
import xyz.matteobattilana.library.WeatherView;


public class FitnessFragment extends Fragment {

    private static final String TAG = "FitnessFragment";
    public static final String ISO_8601_24H_FULL_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX";

    @Bind(R.id.dynamicArcView)
    DecoView mDecoView;
    @Bind(R.id.textPercentage)
    TextView textPercentage;
    @Bind(R.id.textRemaining)
    TextView textToGo;
    @Bind(R.id.textActivity1)
    TextView textActivity1;
    @Bind(R.id.textActivity2)
    TextView textActivity2;
    @Bind(R.id.textActivity3)
    TextView textActivity3;
    @Bind(R.id.chart_repo_lines)
    LineView lines;
    private int mBackIndex;
    private int mSeries1Index;
    private int mSeries2Index;
    private int mSeries3Index;
    private final float mSeriesMax = 50f;
    private OnDataPointListener mListener = null;
    int randomint = 31;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);
        ButterKnife.bind(this, view);
        createBackSeries();
        updateWeatherData("Cosenza");
        GoogleApiClient.getInstance((AppCompatActivity) getActivity()).readFitnessHistory(getContext());
//        GoogleApiClient.getInstance((AppCompatActivity) getActivity()).unsubscribeAllFitnessRecord(getContext());
        createDataSeries1();
        createDataSeries2();
        createDataSeries3();
        createEvents();
        initLineView(lines);
        randomSet(lines);
        return view;
    }

    Handler handler = new Handler();

    private void updateWeatherData(final String city) {
        new Thread() {
            public void run() {
                final JSONObject json = RemoteFetch.getJSON(getActivity(), city);
                if (json == null) {
                    handler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(),
                                    "Città non trovata",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    handler.post(new Runnable() {
                        public void run() {
                            RemoteFetch.renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initLineView(LineView lineView) {
        ArrayList<String> test = new ArrayList<>();
        for (int i = 0; i < randomint; i++) {
            test.add(String.valueOf(i + 1));
        }
        lineView.setBottomTextList(test);
        lineView.setColorArray(new int[]{R.color.walk, R.color.running, R.color.calories});
        lineView.setDrawDotLine(true);
        lineView.setShowPopup(LineView.SHOW_POPUPS_NONE);

    }

    private void randomSet(LineView lineViewFloat) {

        ArrayList<Float> dataListF = new ArrayList<>();
        float randomF = (float) (Math.random() * 9 + 1);
        for (int i = 0; i < randomint; i++) {
            dataListF.add((float) (Math.random() * randomF));
        }

        ArrayList<Float> dataListF2 = new ArrayList<>();
        randomF = (int) (Math.random() * 9 + 1);
        for (int i = 0; i < randomint; i++) {
            dataListF2.add((float) (Math.random() * randomF));
        }

        ArrayList<Float> dataListF3 = new ArrayList<>();
        randomF = (int) (Math.random() * 9 + 1);
        for (int i = 0; i < randomint; i++) {
            dataListF3.add((float) (Math.random() * randomF));
        }

        ArrayList<ArrayList<Float>> dataListFs = new ArrayList<>();
        dataListFs.add(dataListF);
        dataListFs.add(dataListF2);
        dataListFs.add(dataListF3);
        lineViewFloat.setFloatDataList(dataListFs);
    }

    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.walk))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                float percentFilled = ((currentPosition - seriesItem.getMinValue()) / (seriesItem.getMaxValue() - seriesItem.getMinValue()));
                textPercentage.setText(String.format("%.0f%%", percentFilled * 100f));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textToGo.setText(String.format("%.1f Km to goal", seriesItem.getMaxValue() - currentPosition));

            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity1.setText(String.format("%.0f Steps",(float) InfoManager.getInstance().getmFitnessSessionData().steps));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries2() {
        final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.calories))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();


        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity2.setText(String.format("%.2f Kcal",InfoManager.getInstance().getmFitnessSessionData().calories));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries2Index = mDecoView.addSeries(seriesItem);
    }

    private void createDataSeries3() {
        final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.running))
                .setRange(0, mSeriesMax, 0)
                .setInitialVisibility(false)
                .build();

        seriesItem.addArcSeriesItemListener(new SeriesItem.SeriesItemListener() {
            @Override
            public void onSeriesItemAnimationProgress(float percentComplete, float currentPosition) {
                textActivity3.setText(String.format("%.0f Km/h", (float) InfoManager.getInstance().getmFitnessSessionData().speed));
            }

            @Override
            public void onSeriesItemDisplayProgress(float percentComplete) {

            }
        });

        mSeries3Index = mDecoView.addSeries(seriesItem);
    }


    private void createEvents() {
        mDecoView.executeReset();

        mDecoView.addEvent(new DecoEvent.Builder(mSeriesMax)
                .setIndex(mBackIndex)
                .setDuration(1000)
                .setDelay(100)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries1Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(1000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(40.0f)
                .setIndex(mSeries1Index)
                .setDuration(1000)
                .setDelay(2000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(3000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(16.3f)
                .setIndex(mSeries2Index)
                .setDuration(1000)
                .setDelay(4000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(DecoDrawEffect.EffectType.EFFECT_SPIRAL_OUT)
                .setIndex(mSeries3Index)
                .setDuration(1000)
                .setEffectRotations(1)
                .setDelay(5000)
                .build());

        mDecoView.addEvent(new DecoEvent.Builder(10.3f)
                .setIndex(mSeries3Index)
                .setDuration(1000)
                .setDelay(6000)
                .build());

        // resetText();
    }


    private void resetText() {
        textActivity1.setText("");
        textActivity2.setText("");
        textActivity3.setText("");
        textPercentage.setText("");
        textToGo.setText("");
    }


}
