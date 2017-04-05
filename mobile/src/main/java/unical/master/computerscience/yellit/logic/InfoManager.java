package unical.master.computerscience.yellit.logic;

import unical.master.computerscience.yellit.logic.objects.User;

/**
 * Created by Francesco on 27/03/2017.
 */

public class InfoManager {
    private static InfoManager mInfoManager;
    private User mUser;
    private String mToken;
    private Statistics mStatistics;

    private InfoManager() {
    }

    public static InfoManager getInstance() {
        if (mInfoManager == null)
            mInfoManager = new InfoManager();
        return mInfoManager;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public User getUser() {
        return mUser;
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
}
