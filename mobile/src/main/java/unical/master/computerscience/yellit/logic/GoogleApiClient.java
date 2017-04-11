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
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GoogleApiClient implements com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener,
        com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = "GoogleApiClient";
    private static GoogleApiClient mGoogleApiClient = null;
    private com.google.android.gms.common.api.GoogleApiClient mClientFitness;
    private com.google.android.gms.common.api.GoogleApiClient mClientPlace;
    private com.google.android.gms.common.api.GoogleApiClient mClientLocation;

    private GoogleApiClient(final Context context) {
        mClientFitness = new com.google.android.gms.common.api.GoogleApiClient.Builder(context)
                .addApi(Fitness.SENSORS_API)
                .addApi(Fitness.RECORDING_API)
                .addApi(Fitness.HISTORY_API)
                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ_WRITE))
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClientPlace = new com.google.android.gms.common.api.GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClientLocation = new com.google.android.gms.common.api.GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mClientFitness.connect();
        mClientPlace.connect();
        mClientLocation.connect();
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


    /**
     *  disconnect all GoogleApiClient
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
     * @return get current location
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
     * @param lat latidute
     * @param lon longitute
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


    private void subscribeFitnessData() {
        Fitness.RecordingApi.subscribe(mClientFitness, DataType.TYPE_ACTIVITY_SAMPLE)
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
        Fitness.RecordingApi.unsubscribe(mClientFitness, DataType.TYPE_ACTIVITY_SAMPLE)
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
