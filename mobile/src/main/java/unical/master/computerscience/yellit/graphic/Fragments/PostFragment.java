package unical.master.computerscience.yellit.graphic.Fragments;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import unical.master.computerscience.yellit.graphic.custom.PaddingItemDecoration;
import unical.master.computerscience.yellit.logic.InfoManager;
import unical.master.computerscience.yellit.R;
import unical.master.computerscience.yellit.utilities.ShakeDetection;
import unical.master.computerscience.yellit.utilities.UpdatePosts;
import static android.content.Context.SENSOR_SERVICE;

/**
 * Created by Lorenzo on 14/03/2017.
 */

public class PostFragment extends Fragment implements SensorEventListener {

    @Bind(R.id.recycleview_posts)
    protected RecyclerView mPosts;
    @Bind(R.id.swipeRefreshLayout)
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    /**
     * Sensor to detection shake
     */
    private SensorManager mSensorManager;
    private ShakeDetection mShakeDetector;
    private Sensor mAcceleration;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_posts, container, false);
        ButterKnife.bind(this, view);
        mSensorManager = (SensorManager) getContext().getSystemService(SENSOR_SERVICE);
        mPosts.setAdapter(InfoManager.getInstance().getmPostAdapter());
        mPosts.addItemDecoration(new PaddingItemDecoration(170));
        mPosts.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mSwipeRefreshLayout.setProgressViewOffset(false,
                getResources().getDimensionPixelSize(R.dimen.refresher_offset),
                getResources().getDimensionPixelSize(R.dimen.refresher_offset_end));
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorFacebook,R.color.colorGoogle,R.color.colorConfirm);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshItems();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mSensorManager.unregisterListener(mShakeDetector);
        this.mSensorManager.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        this.setupShakeToUpdate();
    }

    @Override
    public void onStart() {
        super.onStart();
        this.setupShakeToUpdate();
    }

    /**
     * Refresh post
     */
    private void refreshItems() {
        UpdatePosts.loadAllPost(getContext());
        onItemsLoadComplete();
    }

    /**
     * close update
     */
    private void onItemsLoadComplete() {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            public void run() {
                InfoManager.getInstance().getmPostAdapter().notifyDataSetChanged();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        };
        handler.postDelayed(runnable, 2500);
    }


    private void setupShakeToUpdate() {
        this.mAcceleration = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.mShakeDetector = new ShakeDetection(new ShakeDetection.OnShakeListener() {
            @Override
            public void onShake() {
                /**
                 * enable to shake
                 */
                UpdatePosts.loadAllPost(getContext());

            }
        });
        this.mSensorManager.registerListener(this.mShakeDetector, this.mAcceleration, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
