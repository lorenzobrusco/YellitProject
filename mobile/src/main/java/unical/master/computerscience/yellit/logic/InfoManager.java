package unical.master.computerscience.yellit.logic;

import unical.master.computerscience.yellit.logic.objects.FitnessSessionData;
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

    private InfoManager() {
        mFitnessSessionData = new FitnessSessionData();
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
}
