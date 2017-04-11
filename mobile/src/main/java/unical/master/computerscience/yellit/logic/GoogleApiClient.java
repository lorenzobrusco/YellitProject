package unical.master.computerscience.yellit.logic;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Subscription;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.fitness.result.ListSubscriptionsResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class GoogleApiClient implements com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "GoogleApiClient";
    private static GoogleApiClient mGoogleApiClient = null;
    private com.google.android.gms.common.api.GoogleApiClient mClientFitness;
    private com.google.android.gms.common.api.GoogleApiClient mClientPlace;
    private com.google.android.gms.common.api.GoogleApiClient mClientLocation;

    private GoogleApiClient(final AppCompatActivity appCompatActivity) {
        mClientFitness = new com.google.android.gms.common.api.GoogleApiClient.Builder(appCompatActivity)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_NUTRITION_READ_WRITE))
                .addConnectionCallbacks(this)
                .enableAutoManage(appCompatActivity, 0, new com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.i(TAG, "connection failed!");
                    }
                })
                .build();
        mClientPlace = new com.google.android.gms.common.api.GoogleApiClient.Builder(appCompatActivity)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClientLocation = new com.google.android.gms.common.api.GoogleApiClient.Builder(appCompatActivity)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClientFitness.connect();
        mClientPlace.connect();
        mClientLocation.connect();
        subscribeFitnessRecor(appCompatActivity);
    }


    public static GoogleApiClient getInstance(final AppCompatActivity appCompatActivity) {
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient(appCompatActivity);
        return mGoogleApiClient;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed!");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "connection suspended!");
    }


    /**
     * disconnect all GoogleApiClient
     */
    public void disconnect() {
        if (mClientFitness.isConnected() && mClientPlace.isConnected() && mClientLocation.isConnected()) {
            mClientFitness.disconnect();
            mClientPlace.disconnect();
            mClientLocation.disconnect();
        }
        mClientFitness = null;
        mClientPlace = null;
        mClientLocation = null;
        mGoogleApiClient = null;
    }


    /**
     * @param context context of activity
     * @return all place
     */
    public PendingResult<PlaceLikelihoodBuffer> getPlaceDetection(final Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mClientPlace, null);
        result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
            @Override
            public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                    Log.i(TAG, String.format("Place '%s' has likelihood: %g",
                            placeLikelihood.getPlace().getName(),
                            placeLikelihood.getLikelihood()));
                }
                likelyPlaces.release();
            }
        });
        return result;
    }

    /**
     * @param context context of activity
     * @return current location
     */
    public String getLocation(final Context context) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        Location mLocation = LocationServices.FusedLocationApi.getLastLocation(mClientLocation);
        Log.i(TAG, mLocation + "");
        String location = null;
        if (mLocation != null) {
            location = this.getAddress(context, mLocation.getLatitude(), mLocation.getLongitude());
        }
        return location;
    }

    /**
     * @param mContext context of activity that call it
     * @param lat      latidute
     * @param lon      longitute
     * @return city and address
     */
    private String getAddress(final Context mContext, double lat, double lon) {
        try {
            Geocoder geocoder;
            String addressTMP = "";
            List<Address> addresses;
            geocoder = new Geocoder(mContext, Locale.getDefault());
            addresses = geocoder.getFromLocation(lat, lon, 1);
            addressTMP += addresses.get(0).getLocality();
            addressTMP += ", ";
            addressTMP += addresses.get(0).getAddressLine(0);
            return addressTMP;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * @param context context of activity that call it
     *                subscribe a session to fitness
     */
    private void subscribeFitnessRecor(final Context context) {
        Fitness.RecordingApi.subscribe(mClientFitness, DataType.TYPE_STEP_COUNT_DELTA)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected.");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.w(TAG, "There was a problem subscribing.");
                        }
                    }
                });
    }


    /**
     * @param context context of activity that call it
     *                show the history inyo google fit
     */
    public void readFitnessHistory(final Context context) {
        /**
         *  transform day to ms, 1000 ms * 60 s * 60 m * 24 h
         */
        final long DAY_IN_MS = 1000 * 60 * 60 * 24;
        Date now = new Date();
        long endTime = now.getTime();
        long startTime = endTime - DAY_IN_MS;

        final DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByActivityType(1, TimeUnit.SECONDS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();

        final PendingResult<DataReadResult> pendingResult = Fitness.HistoryApi.readData(mClientFitness, readRequest);
        pendingResult.setResultCallback(new ResultCallback<DataReadResult>() {
            @Override
            public void onResult(@NonNull DataReadResult dataReadResult) {
                if (dataReadResult.getBuckets().size() > 0) {
                    for (Bucket bucket : dataReadResult.getBuckets()) {
                        List<DataSet> dataSets = bucket.getDataSets();
                        for (DataSet dataSet : dataSets) {
                            processDataSet(context, dataSet);
                        }
                    }
                }
            }
        });

    }

    /**
     * @param dataSet show the data point
     */
    private void processDataSet(final Context context, final DataSet dataSet) {
        for (DataPoint dataPoint : dataSet.getDataPoints()) {
            long dataPointStart = dataPoint.getStartTime(TimeUnit.MINUTES) / 1000000;
            long dataPointEnd = dataPoint.getEndTime(TimeUnit.MINUTES) / 1000000;
            Log.i(TAG, "Data Point");
            Log.i(TAG, "\tType: " + dataPoint.getDataType().getName());
            Log.i(TAG, "\tStart: " + dataPointStart);
            Log.i(TAG, "\tEnd: " + dataPointEnd);
            for (Field field : dataPoint.getDataType().getFields()) {
                Toast.makeText(context, "\tField: " + field.getName() + "\tValue: " + dataPoint.getValue(field), Toast.LENGTH_SHORT).show();
                Log.i(TAG, "\tField: " + field.getName());
                Log.i(TAG, "\tValue: " + dataPoint.getValue(field));
            }
        }

    }

}
