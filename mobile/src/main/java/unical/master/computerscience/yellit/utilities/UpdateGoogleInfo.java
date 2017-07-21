package unical.master.computerscience.yellit.utilities;

import android.support.v7.app.AppCompatActivity;

import unical.master.computerscience.yellit.logic.GoogleApiClient;

/**
 * Used to update values from google
 */

public class UpdateGoogleInfo {


    /**
     * Update any values from google client
     * @param appCompatActivity
     */
    public static void update(AppCompatActivity appCompatActivity){
        GoogleApiClient.getInstance(appCompatActivity);
        GoogleApiClient.getInstance(appCompatActivity).getPlaceDetection(appCompatActivity.getBaseContext());
        GoogleApiClient.getInstance(appCompatActivity).readFitnessHistory(appCompatActivity.getBaseContext());
        GoogleApiClient.getInstance(appCompatActivity).readFitnessGoal(appCompatActivity.getBaseContext());
    }
}
