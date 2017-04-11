package unical.master.computerscience.yellit.logic;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;


public class GoogleApiClient implements com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "GoogleApiClient";

    private static GoogleApiClient mGoogleApiClient = null;
    private com.google.android.gms.common.api.GoogleApiClient mClient;
    private Context mContext;

    private GoogleApiClient(final Context context) {
        mClient = new com.google.android.gms.common.api.GoogleApiClient.Builder(context)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        this.mContext = context;
    }


    public static GoogleApiClient getInstance(final Context context) {
        if (mGoogleApiClient == null)
            mGoogleApiClient = new GoogleApiClient(context);
        return mGoogleApiClient;
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "connection failed!");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.i(TAG, "connected!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "connection suspended!");
    }

    public void disconnect() {
        if (mClient.isConnected())
            mClient.disconnect();
        mClient = null;
        mGoogleApiClient = null;
    }


    /**
     *
     *
     *  Return all place
     *
     */
    @Nullable
    public PendingResult<PlaceLikelihoodBuffer> getPlaceDetection() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
          return null;
        }
        Log.i(TAG, "here");
        PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi.getCurrentPlace(mClient, null);
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



    private void subscribeFitnessData() {
        Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_ACTIVITY_SAMPLE)
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
                            Log.i(TAG, "There was a problem subscribing.");
                        }
                    }
                });
    }

    private void unsubscribeFitnessData() {
        Fitness.RecordingApi.unsubscribe(mClient, DataType.TYPE_ACTIVITY_SAMPLE)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Successfully unsubscribed for data type: " + DataType.TYPE_ACTIVITY_SAMPLE);
                        } else {
                            // Subscription not removed
                            Log.i(TAG, "Failed to unsubscribe for data type: " + DataType.TYPE_ACTIVITY_SAMPLE);
                        }
                    }
                });
    }
}
