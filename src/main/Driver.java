package main;

import controllers.AppStoreAPI;
import controllers.DeveloperAPI;
import models.*;
import utils.ScannerInput;
import utils.Utilities;

import javax.swing.*;

public class Driver {

    //TODO Some skeleton code has been given to you.
    //     Familiarise yourself with the skeleton code...run the menu and then review the skeleton code.
    //     Then start working through the spec.

    private DeveloperAPI developerAPI = new DeveloperAPI();
    private AppStoreAPI appStoreAPI = new AppStoreAPI();
    private JPanel mainPanel;
    private JTextField appStoreTextField;

    public static void main(String[] args) {
        new Driver().start();
    }


    // helper method

    private boolean appInSystem()
    {
        if(appStoreAPI.numberOfApps() == 0)
        {
            System.out.println("no apps in system");
            return   false;

        }
        else
        {
            return true;
        }

    }

    public void start() {
        loadAllData();
        runMainMenu();
    }

    private int mainMenu() {
        System.out.println("""
                 -------------App Store------------
                |  1) Developer - Management MENU  |
                |  2) App - Management MENU        |
                |  3) Reports MENU                 |
                |----------------------------------|
                |  4) Search                       |
                |  5) Sort                         |
                |----------------------------------|
                |  6) Recommended Apps             |
                |  7) Random App of the Day        |
                |  8) Simulate ratings             |
                |----------------------------------|
                |  20) Save all                    |
                |  21) Load all                    |
                |----------------------------------|
                |  0) Exit                         |
                 ----------------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }

    private void runMainMenu() {
        int option = mainMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> runDeveloperMenu();
                case 2 -> runAppManagementMenu();
                case 3 -> runReportsMenu();
                case 4 -> searchAppsBySpecificCriteria();
                case 5 ->  sortAppByName();
                case 6 ->  recommendedApps();
                case 7 ->  randomAppOfDay();
                case 8 ->  simulateRatings();
                case 20 -> saveAllData();
                case 21 -> loadAllData();
                case 22 -> updateGameApp();
                default -> System.out.println("Invalid option entered: " + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = mainMenu();
        }
        exitApp();
    }

    private void exitApp() {
        saveAllData();
        System.out.println("Exiting....");
        System.exit(0);
    }
    //--------------------------------------------------
    //  App Management - Menu Items
    //--------------------------------------------------
    private  int addAppMenu()
    {
        System.out.println("""
                 -----------App App-----------
                |   1) Add a education app   |
                |   2) Add a game app        |
                |   3) Add a productivty app |
                |   0) RETURN to sub menu    |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");

    }


    private  int AppManagementMenu()
    {
        System.out.println("""
                 -----------App App-----------
                |   1) Add a app             |
                |   2) Update a app          |
                |   3) Delete an app         |
                |   0) RETURN to main menu   |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");

    }

    private void runAppManagementMenu()
    {
        int index =  AppManagementMenu();
        while (index != 0) {
            switch (index) {
                case 1 -> runAddAppMenu();
                case 2 -> runUpdateMenu();
                case 3 -> deleteApp();
                default -> System.out.println("Invalid option entered" + index);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            index = AppManagementMenu();
        }

    }

    private  int appUpdateMenu()
    {
        System.out.println("""
                 -----------App App-----------
                |   1) Update game app       |
                |   2) Update education app  |
                |   3) update productivty app|
                |   0) RETURN to sub menu    |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");

    }
    private void runUpdateMenu()
    {
        if(appInSystem())
        {
            int index = appUpdateMenu();
            while (index != 0) {
                switch (index) {
                    case 1 -> updateGameApp();
                    case 2 -> updateEducationApp();
                    case 3 -> updateProductivtyApp();
                    default -> System.out.println("Invalid option entered" + index);
                }
                ScannerInput.validNextLine("\n Press the enter key to continue");
                index = appUpdateMenu();
            }
        }
    }
    private void runAddAppMenu()
    {
        if(!developerAPI.getDevelopers().isEmpty()) {
            int index = addAppMenu();
            while (index != 0) {
                switch (index) {
                    case 1 -> addEducationApp();
                    case 2 -> addGameApp();
                    case 3 -> addProductivtyApp();
                    default -> System.out.println("Invalid option entered" + index);
                }
                ScannerInput.validNextLine("\n Press the enter key to continue");
                index = addAppMenu();
            }

        }
        else
        {
            System.out.println("please add developers first");
        }

    }

    //________________________
    // super App Attributes Input & validation for them
    //________________________

    private Developer  developerInput()
    {

        System.out.println(developerAPI.listDevelopers()+"\n");
        Developer developer  = readValidDeveloperByName();

        while (developer == null)
        {
            developer = readValidDeveloperByName();
        }

        return developer;

    }
    private String  appNameInput()
    {
        String appName = ScannerInput.validNextLine("Please enter the app name: ");
        while(appStoreAPI.isValidAppName(appName))
        {
            appName = ScannerInput.validNextLine("Please enter a valid app name: ");
        }

        return appName;
    }

    private int  appSizeInput()
    {
        int appSize = ScannerInput.validNextInt("Please enter the app size: ");
        Utilities.validRange(appSize, 1, 1000);
        while(!Utilities.validRange(appSize, 1, 1000))
        {
            appSize = ScannerInput.validNextInt("Please enter a valid app size (1-1000 MB): ");
        }

        return appSize;
    }

    private double  appVersionInput()
    {
        double appVersion = ScannerInput.validNextDouble("Please enter the version : ");

        while(!Utilities.greaterThanOrEqualTo(appVersion, 1))
        {
            appVersion = ScannerInput.validNextInt("Please enter a valid version greater than one : ");
        }

        return appVersion;
    }

    private double  appCostInput()
    {
        double appCost = ScannerInput.validNextDouble("Please enter the cost €");

        while(!Utilities.greaterThanOrEqualTo(appCost, 0))
        {
            appCost = ScannerInput.validNextInt("Please enter a valid cost greater than zero € ");
        }

        return appCost;
    }


    private void addEducationApp()
    {

        Developer developer = developerInput();
        String name =appNameInput();
        int size = appSizeInput();
        double version = appVersionInput();
        double cost = appCostInput();

        int level = ScannerInput.validNextInt("Please enter the app level: ");
        Utilities.validRange(level, 1, 1000);
        while(!Utilities.validRange(level, 0, 10))
        {
            level = ScannerInput.validNextInt("Please enter a valid app size (1-1000 MB): ");
        }

        boolean test  =appStoreAPI.addApp(new EducationApp(developer, name, size, version, cost, level));

        System.out.println(Utilities.WasItSuccessful(test));
    }

    private void addGameApp()
    {
        Developer developer = developerInput();
        String name =appNameInput();
        int size = appSizeInput();
        double version = appVersionInput();
        double cost = appCostInput();

        char multiplayerChar = ScannerInput.validNextChar("Please enter (Y or N) for multiplayer: ");

        while(!Utilities.YNValidationChar(multiplayerChar))
        {
            multiplayerChar = ScannerInput.validNextChar("Please try again (Y or N) for multiplayer: ");
        }

        boolean test  =appStoreAPI.addApp(new GameApp(developer, name, size, version, cost, Utilities.YNtoBoolean(multiplayerChar)));

        System.out.println(Utilities.WasItSuccessful(test));


    }

    private void addProductivtyApp()
    {
        Developer developer = developerInput();
        String name =appNameInput();
        int size = appSizeInput();
        double version = appVersionInput();
        double cost = appCostInput();
        boolean test  =appStoreAPI.addApp(new ProductivityApp(developer, name, size, version, cost));
        System.out.println(Utilities.WasItSuccessful(test));
    }
    private void updateGameApp()
    {
        System.out.println(appStoreAPI.listAllGameApps());
        String oldAppName = ScannerInput.validNextLine("Please enter the app name to update: ");
        while(!appStoreAPI.isValidAppName(oldAppName))
        {
            oldAppName = ScannerInput.validNextLine("Please enter a valid app name to update: ");
        }
        Developer developer = developerInput();
        String name = appNameInput();
        double size = appSizeInput();
        double version = appVersionInput();
        double cost = appCostInput();
        char multiplayerChar = ScannerInput.validNextChar("Please enter (Y or N) for multiplayer: ");

        while(!Utilities.YNValidationChar(multiplayerChar))
        {
            multiplayerChar = ScannerInput.validNextChar("Please try again (Y or N) for multiplayer: ");
        }

        boolean test =appStoreAPI.updateGameApp(oldAppName,developer,name,size,version,cost,Utilities.YNtoBoolean(multiplayerChar));
        System.out.println(Utilities.WasItSuccessful(test));
    }

    private void updateEducationApp()
    {
        System.out.println(appStoreAPI.listAllEducationApps());
        String oldAppName = ScannerInput.validNextLine("Please enter the app name to update: ");
        while(!appStoreAPI.isValidAppName(oldAppName))
        {
            oldAppName = ScannerInput.validNextLine("Please enter a valid app name to update: ");
        }
        Developer developer = developerInput();
        String name = appNameInput();
        double size = appSizeInput();
        double version = appVersionInput();
        double cost = appCostInput();
        int level = ScannerInput.validNextInt("Please enter the app level: ");
        Utilities.validRange(level, 1, 1000);
        while(!Utilities.validRange(level, 0, 10))
        {
            level = ScannerInput.validNextInt("Please enter a valid app size (1-1000 MB): ");
        }
        boolean test =appStoreAPI.updateEducationApp(oldAppName,developer,name,size,version,cost,level);
        System.out.println(Utilities.WasItSuccessful(test));
    }

    private void updateProductivtyApp()
    {
        System.out.println(appStoreAPI.listAllProductivityApps());
        String oldAppName = ScannerInput.validNextLine("Please enter the app name to update: ");
        while(!appStoreAPI.isValidAppName(oldAppName))
        {
            oldAppName = ScannerInput.validNextLine("Please enter a valid app name to update: ");
        }
        Developer developer = developerInput();
        String name = appNameInput();
        double size = appSizeInput();
        double version = appVersionInput();
        double cost = appCostInput();

        boolean test =appStoreAPI.updateProductivityApp(oldAppName,developer,name,size,version,cost);
        System.out.println(Utilities.WasItSuccessful(test));
    }


    private void  deleteApp()
    {
        if(appInSystem())
        {
            System.out.println(appStoreAPI.listAllApps());
            String appName = ScannerInput.validNextLine("Please enter app name like to delete: ");

            while(!appStoreAPI.isValidAppName(appName))
            {
                appName = ScannerInput.validNextLine("Sorry this "+appName +"is not valid please try again : ");
            }


            App app  = appStoreAPI.deleteAppByName(appName);

            if(app == null)
            {
                System.out.println("not successful");
            }
            else
            {
                System.out.println("successful");
            }
        }

    }

    //reports menu mangment
    private int ReportsMenu() {
        System.out.println("""
                 -------Developer Menu-------
                |   1) Apps overview         |
                |   2) Developers overview   |
                |   0) RETURN to main menu   |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }


    private void runReportsMenu() {
        int option = ReportsMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> appsOverview();
                case 2 -> appsDevlopers();
                default -> System.out.println("Invalid option entered" + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = ReportsMenu();
        }
    }

    public void  appsOverview()
    {
        if(appInSystem())
        {
            System.out.println(appStoreAPI.listSummaryOfAllApps());
        }
    }

    public void  appsDevlopers()
    {
        if(appInSystem())
        {
            System.out.println(developerAPI.listDevelopers());
        }
    }


    //--------------------------------------------------
    //  Developer Management - Menu Items
    //--------------------------------------------------
    private int developerMenu() {
        System.out.println("""
                 -------Developer Menu-------
                |   1) Add a developer       |
                |   2) List developer        |
                |   3) Update developer      |
                |   4) Delete developer      |
                |   0) RETURN to main menu   |
                 ----------------------------""");
        return ScannerInput.validNextInt("==>> ");
    }


    private void runDeveloperMenu() {
        int option = developerMenu();
        while (option != 0) {
            switch (option) {
                case 1 -> addDeveloper();
                case 2 -> System.out.println(developerAPI.listDevelopers());
                case 3 -> updateDeveloper();
                case 4 -> deleteDeveloper();
                default -> System.out.println("Invalid option entered" + option);
            }
            ScannerInput.validNextLine("\n Press the enter key to continue");
            option = developerMenu();
        }
    }

    private void addDeveloper() {
        String developerName = ScannerInput.validNextLine("Please enter the developer name: ");
        String developerWebsite = ScannerInput.validNextLine("Please enter the developer website: ");

        if (developerAPI.addDeveloper(new Developer(developerName, developerWebsite))) {
            System.out.println("Add successful");
        } else {
            System.out.println("Add not successful");
        }
    }

    private void updateDeveloper() {
        System.out.println(developerAPI.listDevelopers());
        Developer developer = readValidDeveloperByName();
        if (developer != null) {
            String developerWebsite = ScannerInput.validNextLine("Please enter new website: ");
            if (developerAPI.updateDeveloperWebsite(developer.getDeveloperName(), developerWebsite))
                System.out.println("Developer Website Updated");
            else
                System.out.println("Developer Website NOT Updated");
        } else
            System.out.println("Developer name is NOT valid");
    }

    private void deleteDeveloper() {
        String developerName = ScannerInput.validNextLine("Please enter the developer name: ");
        if (developerAPI.removeDeveloper(developerName) != null) {
            System.out.println("Delete successful");
        } else {
            System.out.println("Delete not successful");
        }
    }

    private Developer readValidDeveloperByName() {
        String developerName = ScannerInput.validNextLine("Please enter the developer's name: ");
        while (!developerAPI.isValidDeveloper(developerName)) {
            developerName = ScannerInput.validNextLine("Please enter the a valid developer's name: ");
        }
        return developerAPI.getDeveloperByName(developerName);
    }




    private void searchAppsBySpecificCriteria() {
        System.out.println("""
                What criteria would you like to search apps by:
                  1) App Name
                  2) Developer Name
                  3) Rating (all apps of that rating or above)""");
        int option = ScannerInput.validNextInt("==>> ");
        switch (option) {

            case 1 -> searchAppsByName();
            case 2 -> searchAppsByDeveloper(readValidDeveloperByName());
            case 3 -> searchAppsEqualOrAboveAStarRating();
            default -> System.out.println("Invalid option");
        }
    }



    private void searchAppsEqualOrAboveAStarRating()
    {
        if(appInSystem())
        {
            int star = ScannerInput.validNextInt("Please enter star rating: ");

            while (!Utilities.validRange(star, 1, 5))
            {
                star = ScannerInput.validNextInt("Please enter star rating: ");
            }

            System.out.println(appStoreAPI.listAllAppsAboveOrEqualAGivenStarRating(star));
        }
    }
    private void searchAppsByDeveloper(Developer developer)
    {

        System.out.println(appStoreAPI.listAllAppsByChosenDeveloper(developer));
    }


    private void sortAppByName()
    {
        if(appInSystem())
        {
            appStoreAPI.sortAppsByNameAscending();
            appStoreAPI.listAllApps();
        }
    }
    private void recommendedApps()
    {
        if(appInSystem())
        {
            System.out.println(appStoreAPI.listAllRecommendedApps());
        }
    }

    private void searchAppsByName()
    {
        if(appInSystem())
        {
            String appName = ScannerInput.validNextLine("Please enter app name like to find: ");

            while(!appStoreAPI.isValidAppName(appName))
            {
                appName = ScannerInput.validNextLine("Sorry this "+appName +" is not valid please try again : ");
            }

            System.out.println(appStoreAPI.getAppByName(appName).toString());
        }


    }

    private void randomAppOfDay()
    {
        if(appInSystem())
        {
            System.out.println(appStoreAPI.randomApp().appSummary());
        }
    }
    //--------------------------------------------------
    //simulateRatings
    //--------------------------------------------------
    private void simulateRatings() {
        //simulate random ratings for all apps (to give data for recommended apps and reports etc).
        if (appStoreAPI.numberOfApps() > 0) {
            System.out.println("Simulating ratings...");
            appStoreAPI.simulateRatings();
            System.out.println(appStoreAPI.listSummaryOfAllApps());
        } else {
            System.out.println("No apps in system");
        }
    }

    //--------------------------------------------------
    //  Persistence Menu Items
    //--------------------------------------------------

    private void saveAllData() {

        try {
            System.out.println("saving from file: " + appStoreAPI.fileName());
            appStoreAPI.save();
            System.out.println("saving from file: " + developerAPI.fileName());
            developerAPI.save();
        } catch (Exception e) {
            System.err.println("Error reading from file: " + e);
        }
    }

    private void loadAllData() {
        try {
            System.out.println("Loading from file: " + appStoreAPI.fileName());
            appStoreAPI.load();
            System.out.println("Loading from file: " + developerAPI.fileName());
            developerAPI.load();
        }catch (Exception e) {
            System.err.println("Error writing to file: " + e);
        }


    }

}