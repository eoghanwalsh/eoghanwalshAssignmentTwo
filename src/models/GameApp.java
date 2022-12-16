package models;

public class GameApp extends App{
    //field
    private boolean isMultiplayer = false;
    //constructor
    public GameApp(Developer developer,String appName,double appSize,double appVersion,double appCost,boolean isMultiplayer){
        super(developer, appName, appSize, appVersion, appCost);
        this.isMultiplayer = isMultiplayer;
    }

    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        isMultiplayer = multiplayer;
    }

    @Override
    public boolean isRecommendedApp() {
        if (isMultiplayer && calculateRating()>=4.0){
            return true;
        }
        return false;
    }

    @Override
    public String appSummary() {
        return super.appSummary() + "Multiplayer= " + isMultiplayer();
    }

    @Override
    public String toString() {
        return "GameApp{" +
                super.toString()  +
                "Multiplayer= " + isMultiplayer() +
                '}';
    }
}
