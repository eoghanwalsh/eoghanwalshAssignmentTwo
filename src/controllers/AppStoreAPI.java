package controllers;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import models.*;
import utils.*;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static utils.RatingUtility.generateRandomRating;

public class AppStoreAPI implements ISerializer {

    private List<App> apps = new ArrayList<App>();

    public void simulateRatings(){
        for (App app :apps) {
            app.addRating(generateRandomRating());
        }
    }

    public App getAppByName(String appName)
    {
        if (isValidAppName(appName))
        {
            for (App app : apps)
            {
                if (app.getAppName().toLowerCase().contains(appName.toLowerCase())) {
                    return app;
                }
            }
        }

        return null;
    }

    public App getAppByIndex(int index)
    {
        if(isValidIndex(index)) {return  apps.get(index);}
        else {return null;}
    }


    public boolean isValidIndex(int index) {
        return Utilities.isValidIndex(apps, index);

    }

    public boolean isValidAppName(String appName)
    {
        for (App app : apps)
        {
            if (app.getAppName().toLowerCase().contains(appName.toLowerCase()))
            {
                return true;
            }
        }

        return false;
    }

    public int numberOfApps()
    {
        return apps.size();
    }

    public boolean addApp(App app)
    {
        return apps.add(app);
    }

    public App deleteAppByIndex(int index)
    {
        if (isValidIndex(index))
        {
            return  apps.remove(index);
        }
        else
        {
            return null;
        }
    }

    public App deleteAppByName(String name)
    {
        if(isValidAppName(name))
        {
            int index =  apps.indexOf(getAppByName(name));
            if (isValidIndex(index))
            {
                return  apps.remove(index);
            }
        }

        return null;
    }

    public String listAllApps()
    {
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            String listofApps="";
            {
                for (int i = 0; i < apps.size(); i++)
                {
                    listofApps += i+ ": "+ apps.get(i);
                }

                return listofApps;
            }
        }
    }

    public String listSummaryOfAllApps()
    {
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            String summaryOfApps="";
            for (App app : apps)
            {
                summaryOfApps += app.appSummary()+"\n";
            }

            return summaryOfApps;
        }
    }

    public String  listAllGameApps()
    {
        String listOfGameApps ="";
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            for (App app : apps)
            {
                if(app instanceof GameApp)
                {
                    int index = apps.indexOf(app);
                    listOfGameApps += index +": " + app.toString()+"\n";
                }
            }
        }

        if(listOfGameApps.equals(""))
        {
            return "no game apps in the system";
        }
        else
        {
            return listOfGameApps;
        }
    }

    public String  listAllEducationApps()
    {
        String listOfEducationApps ="";
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            for (App app : apps)
            {
                if(app instanceof EducationApp)
                {
                    int index = apps.indexOf(app);
                    listOfEducationApps += index +": " + app.toString();
                }
            }
        }

        if(listOfEducationApps.equals(""))
        {
            return "no education apps in the system";
        }
        else
        {
            return listOfEducationApps;
        }
    }

    public String  listAllProductivityApps()
    {
        String listOfProductivityApps ="";
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            for (App app : apps)
            {
                if(app instanceof ProductivityApp)
                {
                    int index = apps.indexOf(app);
                    listOfProductivityApps += index +": " + app.toString();
                }
            }
        }

        if(listOfProductivityApps.equals(""))
        {
            return "no productivity apps in the system";
        }
        else
        {
            return listOfProductivityApps;
        }
    }

    public String listAllAppsByName(String searchString)
    {
        String foundApp = "";
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            for (App app : apps)
            {
                if(app.getAppName().toLowerCase().contains(searchString.toLowerCase()))
                {
                    foundApp += app.toString()+"\n";
                }
            }
        }

        if (foundApp.equals("")) {
            return "No apps found for" + ": " + searchString;
        }
        else
        {
            return foundApp;
        }

    }

    public  String listAllAppsAboveOrEqualAGivenStarRating( int rating )
    {
        String ratingList="";
        if(apps.isEmpty())
        {
            return "No Apps in the system";
        }
        else if (!(rating >=0))
        {
            return "Sorry please input number bigger than or equal to 0";
        }
        else
        {
            for (App app : apps)
            {
                if(app.calculateRating()>= rating )
                {

                    ratingList += app.toString()+"\n";
                }
            }
        }

        if (ratingList.equals(""))
        {
            return "No apps have a rating of " +rating+" or above";
        }
        else
        {
            return ratingList;
        }
    }

    public boolean updateApp(String oldAppName, Developer developer, String name,double size, double version, double cost)
    {
        if (isValidAppName(oldAppName))
        {
            App app = getAppByName(oldAppName);
            app.setDeveloper(developer);
            app.setAppName(name);
            app.setAppSize(size);
            app.setAppVersion(version);
            app.setAppCost(cost);
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean updateEducationApp(String oldAppName, Developer developer, String name,double size, double version, double cost, int level)
    {
        App app = getAppByName(oldAppName);
        ((EducationApp)app).setLevel(level);
        boolean test = updateApp(oldAppName,developer,name,size,version,cost);
        if(test){return true;}
        else{return false;}
    }

    public boolean updateGameApp(String oldAppName, Developer developer, String name,double size, double version, double cost, boolean multiplayer)
    {
        App app = getAppByName(oldAppName);
        ((GameApp) app).setMultiplayer(multiplayer);
        boolean test = updateApp(oldAppName,developer,name,size,version,cost);
        if(test){return true;}
        else{return false;}
    }

    public boolean updateProductivityApp(String oldAppName, Developer developer, String name,double size, double version, double cost)
    {
        boolean  test = updateApp(oldAppName,developer,name,size,version,cost);

        if(test){return true;}
        else{return false;}
    }

    public String listAllRecommendedApps()
    {
        String allRecommendedApps="";
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            for (App app : apps)
            {
                if(app.isRecommendedApp())
                {
                    int index = apps.indexOf(app);
                    allRecommendedApps += index +": " + app.toString();
                }
            }
        }

        if(allRecommendedApps.equals(""))
        {
            return "No recommended apps";
        }
        else
        {
            return allRecommendedApps;
        }

    }

    public String  listAllAppsByChosenDeveloper(Developer developer)
    {
        String appsByDev="";
        if(apps.isEmpty())
        {
            return "no apps in the system";
        }
        else
        {
            for (App app : apps)
            {
                if (app.getDeveloper().equals(developer))
                {
                    int index = apps.indexOf(app);
                    appsByDev += index +": " + app.toString()+"\n";
                }
            }
        }

        if (appsByDev.equals(""))
        {
            return "No apps for developer: "+ developer;
        }
        else
        {
            return appsByDev;
        }

    }

    public int numberOfAppsByChosenDeveloper(Developer developer)
    {
        int count = 0;
        if(apps.isEmpty())
        {
            return 0;
        }
        else
        {
            for (App app : apps)
            {
                if (app.getDeveloper().equals(developer))
                {
                    count++;
                }
            }
        }
        return count;
    }

    public App randomApp()
    {
        if(apps.isEmpty())
        {
            return null;
        }
        else
        {
            Random randIndex = new Random();
            int randomIndex = randIndex.ints(0, apps.size()).findAny().getAsInt();
            return apps.get(randomIndex);
        }
    }

    public void sortAppsByNameAscending()
    {
        for (int i = apps.size() -1; i > 0; i--)
        {
            int highestIndex = 0;
            for (int j = 0; j <= i; j++)
            {
                if (apps.get(j).getAppName().compareTo(apps.get(highestIndex).getAppName()) > 0) {
                    highestIndex = j;
                }
            }
            swapApps((ArrayList<App>) apps, i, highestIndex);



        }
    }

    private void swapApps (ArrayList<App> apps, int i, int j)
    {
        App smaller = apps.get(i);
        App bigger = apps.get(j);

        apps.set(i,bigger);
        apps.set(j, smaller);

    }

    //---------------------
    // Persistence methods
    //---------------------
    @SuppressWarnings("unchecked")
    public void load() throws Exception {
        //list of classes that you wish to include in the serialisation, separated by a comma
        Class<?>[] classes = new Class[]{App.class, EducationApp.class, GameApp.class, ProductivityApp.class, Rating.class};

        //setting up the xstream object with default security and the above classes
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        //doing the actual serialisation to an XML file
        ObjectInputStream in = xstream.createObjectInputStream(new FileReader(fileName()));
        apps = (ArrayList<App>) in.readObject();
        in.close();
    }


    public void save() throws Exception {
        XStream xstream = new XStream(new DomDriver());
        ObjectOutputStream out = xstream.createObjectOutputStream(new FileWriter(fileName()));
        out.writeObject(apps);
        out.close();
    }

    public String fileName(){
        return "apps.xml";
    }
}
