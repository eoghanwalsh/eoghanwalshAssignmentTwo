package models;

import utils.Utilities;

public class EducationApp extends App {

    int level = 0;

    public EducationApp(Developer developer, String appName, double appSize, double appVersion, double appCost, int level) {
        super(developer, appName, appSize, appVersion, appCost);
        setLevel(level);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (Utilities.validRange(level, 1, 10)) {
            this.level = level;
        }
    }

    @Override
    public boolean isRecommendedApp() {
        if ((super.getAppCost() > 0.99) && (super.calculateRating() >= 3.50) && (getLevel() >= 3)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String appSummary() {
        return super.appSummary() + "level " + getLevel();
    }

    @Override
    public String toString() {
        return "EducationApp{" +
                super.toString() +
                "Level: " + getLevel() +
                '}';

    }
}
