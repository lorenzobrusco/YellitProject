package unical.master.computerscience.yellit.logic;

import android.Manifest;
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
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import unical.master.computerscience.yellit.utiliies.PrefManager;

public class GoogleApiClient implements com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "GoogleApiClient";
    private static final String STEPS = "step";
    private static final String CALORIES = "calories";
    private static final String SPEED = "average";
    private static GoogleApiClient mGoogleApiClient = null;
    private com.google.android.gms.common.api.GoogleApiClient mClientFitness;
    private com.google.android.gms.common.api.GoogleApiClient mClientPlace;
    private com.google.android.gms.common.api.GoogleApiClient mClientLocation;

    private GoogleApiClient(final AppCompatActivity appCompatActivity) {
        mClientFitness = new com.google.android.gms.common.api.GoogleApiClient.Builder(appCompatActivity)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Fitness.GOALS_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_BODY_READ_WRITE))
                .addScope(new Scope(Scopes.FITNESS_NUTRITION_READ_WRITE))
                .addConnectionCallbacks(new com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(@Nullable Bundle bundle) {
                        if(new PrefManager(appCompatActivity).isFirstTimeLaunch())
                        subscribeAllFitnessRecord(appCompatActivity);
                    }

                    @Override
                    public void onConnectionSuspended(int i) {

                    }
                })
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
     *                subscribe all datatype for a session to fitness
     */
    private void subscribeAllFitnessRecord(final Context context) {
        if (mClientFitness.isConnected()) {
            this.subscribeFitnessRecord(context, DataType.TYPE_STEP_COUNT_DELTA);
            this.subscribeFitnessRecord(context, DataType.TYPE_CALORIES_EXPENDED);
            this.subscribeFitnessRecord(context, DataType.TYPE_SPEED);
        }
    }

    /**
     * @param context context of activity that call it
     *                unsubscribe all datatype for a session to fitness
     */
    public void unsubscribeAllFitnessRecord(final Context context) {
        if (mClientFitness.isConnected()) {
            this.unsubscribeFitnessRecord(context, DataType.TYPE_STEP_COUNT_DELTA);
            this.unsubscribeFitnessRecord(context, DataType.TYPE_CALORIES_EXPENDED);
            this.unsubscribeFitnessRecord(context, DataType.TYPE_SPEED);
        }
    }

    /**
     * @param context  context of activity that call it
     * @param dataType type
     *                 subscribe a single datatype for a session to fitness
     */
    private void subscribeFitnessRecord(final Context context, DataType dataType) {
        Fitness.RecordingApi.subscribe(mClientFitness, dataType)
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
     * @param context  context of activity that call it
     * @param dataType type
     *                 unsubscribe a single datatype for a session to fitness
     */
    private void unsubscribeFitnessRecord(final Context context, final DataType dataType) {
        Fitness.RecordingApi.unsubscribe(mClientFitness, dataType)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Successfully unsubscribed for data type: " + dataType.getName().toString());
                        } else {
                            // Subscription not removed
                            Log.i(TAG, "Failed to unsubscribe for data type: " + dataType.getName().toString());
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
                .aggregate(DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_CALORIES_EXPENDED)
                .aggregate(DataType.TYPE_SPEED, DataType.AGGREGATE_SPEED_SUMMARY)
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
            for (Field field : dataPoint.getDataType().getFields()) {
                if(field.getName().contains(STEPS))
                        InfoManager.getInstance().getmFitnessSessionData().steps = Integer.parseInt(dataPoint.getValue(field) + "");
                if(field.getName().contains(CALORIES))
                        InfoManager.getInstance().getmFitnessSessionData().calories = Float.parseFloat(dataPoint.getValue(field) + "");
                if(field.getName().contains(SPEED) && !(dataPoint.getValue(field)+"").equals("NaN") )
                    InfoManager.getInstance().getmFitnessSessionData().speed = Float.parseFloat(dataPoint.getValue(field) + "");
                Log.i(TAG, "\tField: " + field.getName());
                Log.i(TAG, "\tValue: " + dataPoint.getValue(field));
            }
        }

    }

}
