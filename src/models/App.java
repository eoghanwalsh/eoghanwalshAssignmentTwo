package models;
import models.Rating;
import models.Developer;
import utils.Utilities;

import java.util.ArrayList;
import java.util.List;

public abstract class App {
    private String appName ="No app name";
    private double appSize =0;
    private double appVersion = 1.0;
    private double appCost = 0;
    private Developer developer;
    private List<Rating> ratings = new ArrayList<Rating>();


    public App(Developer developer, String appName, double appSize, double appVersion, double appCost)
    {
        this.developer = developer;
        setAppName(appName);
        setAppSize(appSize);
        setAppVersion(appVersion);
        setAppCost(appCost);
    }


    public abstract boolean isRecommendedApp();


    public String appSummary()
    {
        return getAppName() + "(V" + getAppVersion() + " by "+ developer.toString()+ ", €"+ getAppCost()+ " Rating: " + calculateRating();


    }
    public boolean addRating(Rating rating)
    {
        return ratings.add(rating);
    }

    public String listRatings()
    {
        String text ="";
        for (Rating rating : ratings)
        {
            text+= rating.toString();
        }

        if (text.isEmpty())
        {
            return "No ratings added yet";
        }
        else
        {
            return text;
        }
    }

    public double calculateRating()
    {

        int count = 0;
        double starAmount =0;
        for (Rating rating: ratings)
        {
            double stars = rating.getNumberOfStars();
            if ((stars >0))
            {
                starAmount += stars;
                count++;
            }
        }

        if (ratings.isEmpty())
        {
            return 0;
        }
        else
        {return (starAmount/count);}

    }

    public void setDeveloper(Developer developer) {
        this.developer = developer;
    }

    public Developer getDeveloper() {
        return developer;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public double getAppSize() {
        return appSize;
    }

    public void setAppSize(double appSize) {
        if(Utilities.validRange(appSize, 1, 1000))
        {
            this.appSize = appSize;
        }
    }

    public double getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(double appVersion) {
        if (Utilities.greaterThanOrEqualTo(appVersion, 1))
        {this.appVersion = appVersion;}
    }

    public double getAppCost() {
        return appCost;
    }

    public void setAppCost(double appCost) {
        if (Utilities.greaterThanOrEqualTo(appCost, 0))
        { this.appCost = appCost;}
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    @Override
    public String toString() {
        return "App{" +
                getAppName() +
                "(Version " + getAppVersion() +
                getDeveloper() +
                getAppSize() + "MB" +
                "Cost: " + getAppCost() +
                "Ratings (" + calculateRating() +
                listRatings() +
                '}';
    }


}

