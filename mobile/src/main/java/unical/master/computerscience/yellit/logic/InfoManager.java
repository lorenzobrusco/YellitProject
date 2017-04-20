package unical.master.computerscience.yellit.logic;

import java.util.ArrayList;

import unical.master.computerscience.yellit.logic.objects.FitnessSessionData;
import unical.master.computerscience.yellit.logic.objects.PlaceData;
import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Francesco on 27/03/2017.
 */

public class InfoManager {
    private static InfoManager mInfoManager;
    private User mUser;
    private String mToken;
    private Statistics mStatistics;
    private FitnessSessionData mFitnessSessionData;
    private PlaceData mPlaceData;

    private InfoManager() {
        mFitnessSessionData = new FitnessSessionData();
        mPlaceData = new PlaceData();
        mPlaceData.place = new ArrayList<>();
    }

    public static InfoManager getInstance() {
        if (mInfoManager == null)
            mInfoManager = new InfoManager();
        return mInfoManager;
    }

    public String getmToken() {
        return mToken;
    }

    public void setmToken(String mToken) {
        this.mToken = mToken;
    }

    public Statistics getmStatistics() {
        return mStatistics;
    }

    public void setmStatistics(Statistics mStatistics) {
        this.mStatistics = mStatistics;
    }

    public User getmUser() {
        return mUser;
    }

    public void setmUser(User mUser) {
        this.mUser = mUser;
    }

    public FitnessSessionData getmFitnessSessionData() {
        return mFitnessSessionData;
    }

    public void setmFitnessSessionData(FitnessSessionData mFitnessSessionData) {
        this.mFitnessSessionData = mFitnessSessionData;
    }

    public static InfoManager getmInfoManager() {
        return mInfoManager;
    }

    public static void setmInfoManager(InfoManager mInfoManager) {
        InfoManager.mInfoManager = mInfoManager;
    }

    public PlaceData getmPlaceData() {
        return mPlaceData;
    }

    public void setmPlaceData(PlaceData mPlaceData) {
        this.mPlaceData = mPlaceData;
    }
}
