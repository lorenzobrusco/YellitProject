package unical.master.computerscience.yellit.graphic.Fragments;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.hookedonplay.decoviewlib.DecoView;
import com.hookedonplay.decoviewlib.charts.DecoDrawEffect;
import com.hookedonplay.decoviewlib.charts.SeriesItem;
import com.hookedonplay.decoviewlib.events.DecoEvent;
import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import butterknife.Bind;
import butterknife.ButterKnife;
import im.dacer.androidcharts.LineView;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.utilities.RemoteFetch;
import unical.master.computerscience.yellit.utilities.UpdateGoogleInfo;


/**
 * Show a set of views for the fitness
 */
public class FitnessFragment extends Fragment {

    @Bind(R.id.dynamicArcView)
    protected DecoView mDecoView;
    @Bind(R.id.textPercentage)
    protected TextView textPercentage;
    @Bind(R.id.textRemaining)
    protected TextView textToGo;
    @Bind(R.id.textActivity1)
    protected TextView textActivity1;
    @Bind(R.id.textActivity2)
    protected TextView textActivity2;
    @Bind(R.id.textActivity3)
    protected TextView textActivity3;
    @Bind(R.id.chart_repo_lines)
    protected LineView lines;
    @Bind(R.id.type_weather)
    protected TextView mTypeWeatherTextView;
    @Bind(R.id.temperature_weather)
    protected TextView mTemperature;
    @Bind(R.id.icon_weather)
    protected ImageView mIconWeather;
    @Bind(R.id.day_weather)
    protected TextView mDay;
    @Bind(R.id.image_weather)
    protected ImageView mImageWeather;
    @Bind(R.id.progressBar_weather)
    protected ProgressBar mWeatherProgressBar;
    @Bind(R.id.coming_soon_fitness)
    protected Button mComingSoon;
    @Bind(R.id.fitness_info_more_one_day)
    protected LinearLayout mInfoMoreOneDay;

    private Animation mAnimationDown;
    private int mBackIndex;
    private int mSeries1Index;
    private final float mStepsMax = 10000f;
    private int randomint = 31;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fitness, container, false);
        ButterKnife.bind(this, view);
        /** Getting weather */
        new AsyncTask<String, Void, JSONObject>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                mWeatherProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected JSONObject doInBackground(String... params) {
                Toast.makeText(getContext(), "Do in back ground", Toast.LENGTH_LONG).show();
                final JSONObject json = RemoteFetch.getJSON(getActivity(), params[0]);
                final JSONObject data = RemoteFetch.getData(json);
                return data;
            }

            @Override
            protected void onPostExecute(JSONObject jsonObject) {
                super.onPostExecute(jsonObject);
                if (jsonObject == null)
                    Toast.makeText(getActivity(), "City not found", Toast.LENGTH_LONG).show();
                else
                    initWeather(jsonObject);
                mWeatherProgressBar.setVisibility(View.GONE);

            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "Cosenza");
        UpdateGoogleInfo.update((AppCompatActivity) getActivity());
        createBackSeries();
        createDataSeries1();
        createEvents();
        initLineView(lines);
        randomSet(lines);
        mAnimationDown = AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_down);
        textActivity1.setText(String.format("%.0f Steps", (float) InfoManager.getInstance().getmFitnessSessionData().steps));
        textActivity2.setText(String.format("%.2f Kcal", InfoManager.getInstance().getmFitnessSessionData().calories));
        textActivity3.setText(String.format("%.0f Km/h", InfoManager.getInstance().getmFitnessSessionData().speed));
        mComingSoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAnimationDown.reset();
                mInfoMoreOneDay.clearAnimation();
                mInfoMoreOneDay.startAnimation(mAnimationDown);
                mInfoMoreOneDay.setVisibility(mInfoMoreOneDay.isShown()
                        ? View.GONE : View.VISIBLE);
            }
        });
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

    }

    /**
     * Initialize weather
     * @param data from asyntask
     */
    private void initWeather(JSONObject data) {
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
            String formattedDate = df.format(c.getTime());
            mDay.setText(formattedDate);
            mTypeWeatherTextView.setText(data.getString("weather"));
            mTemperature.setText(data.getString("temperature"));
            String weather = data.getString("weather");
            setWeatherIcon(weather);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Change icon according the weather
     * @param weather
     */
    private void setWeatherIcon(String weather) {
        if (weather.contains("thunderstorm")) {
            mIconWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_storm));
            mImageWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.lightning));
        } else if (weather.contains("snow")) {
            mIconWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_snow));
            mImageWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.snow));
        } else if (weather.contains("cloud")) {
            mIconWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_cloudy));
            mImageWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cloud));
        } else if (weather.contains("clear")) {
            mIconWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_clear));
            mImageWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.sun));
        } else if (weather.contains("rain")) {
            mIconWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.ic_rain));
            mImageWeather.setImageDrawable(getContext().getResources().getDrawable(R.drawable.rain));
        }
    }


    /**
     *
     * @param lineView
     */
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


    /**
     *
     * @param lineViewFloat
     */
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

    /**
     * Used to create gray background to progress bar circular
     */
    private void createBackSeries() {
        SeriesItem seriesItem = new SeriesItem.Builder(Color.parseColor("#FFE2E2E2"))
                .setRange(0, mStepsMax, 0)
                .setInitialVisibility(true)
                .build();

        mBackIndex = mDecoView.addSeries(seriesItem);
    }

    /**
     * Create a real series of data from google fitness
     */
    private void createDataSeries1() {
        final SeriesItem seriesItem = new SeriesItem.Builder(getResources().getColor(R.color.walk))
                .setRange(0, mStepsMax, 0)
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

        mSeries1Index = mDecoView.addSeries(seriesItem);
    }

    /**
     *  Start the animation
     */
    private void createEvents() {
        mDecoView.executeReset();
        mDecoView.addEvent(new DecoEvent.Builder(mStepsMax)
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
        mDecoView.addEvent(new DecoEvent.Builder(InfoManager.getInstance().getmFitnessSessionData().steps)
                .setIndex(mSeries1Index)
                .setDuration(1000)
                .setDelay(2000)
                .build());
    }


}
