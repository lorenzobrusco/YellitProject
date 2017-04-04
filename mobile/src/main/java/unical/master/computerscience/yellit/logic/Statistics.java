package unical.master.computerscience.yellit.logic;

/**
 * Created by Francesco on 04/04/2017.
 */

class Statistics {

    private int mTot, mFoodAndDrink, mFitness, mTravel, mOutSide, mInside;

    public Statistics(int mTot, int mFoodAndDrink, int mFitness, int mTravel, int mOutSide, int mInside) {
        this.mTot = mTot;
        this.mFoodAndDrink = mFoodAndDrink;
        this.mFitness = mFitness;
        this.mTravel = mTravel;
        this.mOutSide = mOutSide;
        this.mInside = mInside;
    }

    public int getmTot() {
        return mTot;
    }

    public void setmTot(int mTot) {
        this.mTot = mTot;
    }

    public int getmFoodAndDrink() {
        return mFoodAndDrink;
    }

    public void setmFoodAndDrink(int mFoodAndDrink) {
        this.mFoodAndDrink = mFoodAndDrink;
    }

    public int getmFitness() {
        return mFitness;
    }

    public void setmFitness(int mFitness) {
        this.mFitness = mFitness;
    }

    public int getmTravel() {
        return mTravel;
    }

    public void setmTravel(int mTravel) {
        this.mTravel = mTravel;
    }

    public int getmOutSide() {
        return mOutSide;
    }

    public void setmOutSide(int mOutSide) {
        this.mOutSide = mOutSide;
    }

    public int getmInside() {
        return mInside;
    }

    public void setmInside(int mInside) {
        this.mInside = mInside;
    }
}
