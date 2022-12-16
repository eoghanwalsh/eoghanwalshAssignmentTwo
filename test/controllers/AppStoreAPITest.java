package controllers;

import models.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class AppStoreAPITest {

    private EducationApp edAppBelowBoundary, edAppOnBoundary, edAppAboveBoundary, edAppInvalidData;
    private ProductivityApp prodAppBelowBoundary, prodAppOnBoundary, prodAppAboveBoundary, prodAppInvalidData;
    private GameApp gameAppBelowBoundary, gameAppOnBoundary, gameAppAboveBoundary, gameAppInvalidData;

    private Developer developerLego = new Developer("Lego", "www.lego.com");
    private Developer developerSphero = new Developer("Sphero", "www.sphero.com");
    private Developer developerEAGames = new Developer("EA Games", "www.eagames.com");
    private Developer developerKoolGames = new Developer("Kool Games", "www.koolgames.com");
    private Developer developerApple = new Developer("Apple", "www.apple.com");
    private Developer developerMicrosoft = new Developer("Microsoft", "www.microsoft.com");
    private Developer developerNoMatches = new Developer("FromSoftware", "www.FromSoftware.com");

    private AppStoreAPI appStore = new AppStoreAPI();
    private AppStoreAPI emptyAppStore = new AppStoreAPI();

    @BeforeEach
    void setUp() {

        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0), level(1-10).
        edAppBelowBoundary = new EducationApp(developerLego, "WeDo", 1, 1.0, 0,  1);

        edAppOnBoundary = new EducationApp(developerLego, "Spike", 1000, 2.0,
                1.99, 10);

        edAppAboveBoundary = new EducationApp(developerLego, "EV3", 1001, 3.5,  2.99,  11);

        edAppInvalidData = new EducationApp(developerLego, "", -1, 0, -1.00,  0);


        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0),
        prodAppBelowBoundary = new ProductivityApp(developerApple, "NoteKeeper", 1, 1.0, 0.0);

        prodAppOnBoundary = new ProductivityApp(developerMicrosoft, "Outlook", 1000, 2.0, 1.99);

        prodAppAboveBoundary = new ProductivityApp(developerApple, "Pages", 1001, 3.5, 2.99);

        prodAppInvalidData = new ProductivityApp(developerMicrosoft, "", -1, 0, -1.00);


        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0),
        gameAppBelowBoundary = new GameApp(developerEAGames, "Tetris", 1, 1.0, 0.0,  false);

        gameAppOnBoundary = new GameApp(developerKoolGames, "CookOff", 1000, 2.0, 1.99,  true);

        gameAppAboveBoundary = new GameApp(developerEAGames, "Empires", 1001, 3.5,  2.99, false);

        gameAppInvalidData = new GameApp(developerKoolGames, "", -1, 0,  -1.00,  true);



        //not included - edAppOnBoundary, edAppInvalidData, prodAppBelowBoundary, gameAppBelowBoundary, gameAppInvalidData.
        appStore.addApp(edAppBelowBoundary);//0
        appStore.addApp(prodAppOnBoundary);//1
        appStore.addApp(gameAppAboveBoundary);//2
        appStore.addApp(prodAppBelowBoundary);//4
        appStore.addApp(edAppAboveBoundary);//5
        appStore.addApp(prodAppInvalidData);//6
        appStore.addApp(gameAppOnBoundary);//7
        appStore.addApp(edAppOnBoundary);//8
        appStore.addApp(edAppInvalidData);//9
        appStore.addApp(gameAppBelowBoundary);//10
        appStore.addApp(gameAppInvalidData); // 11 app in the list


    }

    @AfterEach
    void tearDown() {
        edAppBelowBoundary = edAppOnBoundary = edAppAboveBoundary = edAppInvalidData = null;
        gameAppBelowBoundary = gameAppOnBoundary = gameAppAboveBoundary = gameAppInvalidData = null;
        prodAppBelowBoundary = prodAppOnBoundary = prodAppAboveBoundary = prodAppInvalidData = null;
        developerApple = developerEAGames = developerKoolGames = developerLego = developerMicrosoft = null;
        appStore = emptyAppStore = null;
    }

    @Nested
    class GettersAndSetters {
        @Nested
        class getters
        {
            @Test
            void numberOfAppsIfNoAppsInTheSystem()
            {
                assertEquals(0, emptyAppStore.numberOfApps());
            }
            @Test
            void numberOfAppsIfAppsInTheSystem()
            {
                assertEquals(11, appStore.numberOfApps());
            }


            @Test
            void getAppByIndexReturnsNullWhenIndexIsInValid() {
                assertEquals(0, emptyAppStore.numberOfApps());
                assertNull(emptyAppStore.getAppByIndex(0));

                assertEquals(11, appStore.numberOfApps());
                assertNull(appStore.getAppByIndex(-1));
                assertNull(appStore.getAppByIndex(100));

            }
            @Test
            void getAppByIndexReturnsAPPWhenIndexIsValid() {
                assertEquals(11, appStore.numberOfApps());
                assertEquals(edAppBelowBoundary, appStore.getAppByIndex(0));
                assertEquals(edAppOnBoundary, appStore.getAppByIndex(7));
            }
            @Test
            void getAppByNameWhenValid()
            {
                assertEquals(prodAppOnBoundary, appStore.getAppByName("Outlook"));
                assertEquals(edAppOnBoundary, appStore.getAppByName("Spike"));
            }

            @Test
            void getAppByNamWhenWhenNotValid()
            {
                assertEquals(null, appStore.getAppByName("this is not an app"));
                assertEquals(null, appStore.getAppByName("HelloWorldPrinter"));
            }

            @Test
            void getFileName()
            {
                String test =appStore.fileName();
                assertTrue(test.contains("apps.xml"));
            }

            @Test
            void isValidIndex()
            {
                assertEquals(11, appStore.numberOfApps());
                assertFalse(appStore.isValidIndex(-1));//under
                assertTrue(appStore.isValidIndex(1));// on it
                assertFalse(appStore.isValidIndex(12));//over it
            }



        }

    }

    @Nested
    class CRUDMethods {



        @Test
        void addValidApp()
        {
            GameApp app0 = new GameApp(developerKoolGames, "CookOff", 1000, 2.0, 1.99,  true);
            assertTrue(appStore.addApp(app0));
            assertEquals(app0.getAppName().toLowerCase(), (appStore.getAppByName("CookOff")).getAppName().toLowerCase());
            assertEquals(12,appStore.numberOfApps());
            EducationApp app1= new EducationApp(developerLego, "Spike", 1000, 2.0, 1.99, 10);
            assertTrue(appStore.addApp(app1));
            assertEquals(app1, appStore.getAppByIndex(12));

        }

        @Test
        void deletingAppThatDoesNotExistReturnsNull(){
            assertNull(emptyAppStore.deleteAppByIndex(0));
            assertNull(emptyAppStore.deleteAppByIndex(-1));
            assertNull(emptyAppStore.deleteAppByIndex(12));
        }

        @Test
        void deletingAppThatExistsDeletesAndReturnsDeletedObject(){
            assertEquals(11, appStore.numberOfApps());
            assertEquals(edAppBelowBoundary, appStore.deleteAppByIndex(0));
            assertEquals(10, appStore.numberOfApps());
            assertNull( appStore.getAppByName(edAppBelowBoundary.getAppName()));

        }

        @Test
        void deletingAppByNameThatDoesNotExistReturnsNull()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "cooking", 1000, 2.0, 1.99,  true);
            ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "cooking", 1000, 2.0, 1.99);
            test.addApp(app1);test.addApp(app2);
            assertNull(test.deleteAppByName("HELLOWORLD"));

        }

        @Test
        void deletingAppByNameThatDoesExistReturnsAPP()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "cooking", 1000, 2.0, 1.99,  true);
            ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "cooking", 1000, 2.0, 1.99);
            test.addApp(app1);test.addApp(app2);
            assertEquals(app1 ,test.deleteAppByName("cooking") );

        }


    }

    @Nested
    class RandomGenerate
    {//
        @Test
        void simulateRatingsChecker()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "cooking", 1000, 2.0, 1.99,  true);
            ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "cooking", 1000, 2.0, 1.99);
            test.addApp(app1);test.addApp(app2);
            test.simulateRatings();

        }
    }
    @Nested
    class ListingMethods {

        @Test
        void listAllAppsReturnsNoAppssStoredWhenArrayListIsEmpty() {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertTrue(emptyAppStore.listAllApps().toLowerCase().contains("no apps"));
        }

        @Test
        void listAllAppsReturnsAppsStoredWhenArrayListHasAppsStored() {
            assertEquals(11, appStore.numberOfApps());
            String apps = appStore.listAllApps();
            //checks for objects in the string
            assertTrue(apps.contains("WeDo"));
            assertTrue(apps.contains("Outlook"));
            assertTrue(apps.contains("Empires"));
            assertTrue(apps.contains("NoteKeeper"));
            assertTrue(apps.contains("EV3"));
            assertTrue(apps.contains("CookOff"));
        }

        @Test
        void listRecommendedAppsReturnsNoAppsWhenRecommendedAppsDoNotExist() {
            assertEquals(11, appStore.numberOfApps());

            String apps = appStore.listAllRecommendedApps();
            //checks for the three objects in the string
            assertTrue(apps.contains("No recommended apps"));
        }

        @Test
        void listRecommendedAppsReturnsNoAppsWhenRecommendedAppsWhenEmpty() {
            String apps = emptyAppStore.listAllRecommendedApps();
            assertTrue(apps.contains("no apps in the system"));
        }

        @Test
        void listRecommendedAppsReturnsRecommendedAppsWhenTheyExist() {
            assertEquals(11, appStore.numberOfApps());

            //adding recommended apps to the list
            appStore.addApp(setupGameAppWithRating(5,4));
            appStore.addApp(setupEducationAppWithRating(4,4));
            appStore.addApp(setupProductivityAppWithRating(4,4));
            assertEquals(14, appStore.numberOfApps());

            String apps = appStore.listAllRecommendedApps();
            //checks for the three objects in the string
            assertTrue(apps.contains("MazeRunner"));
            assertTrue(apps.contains("Evernote"));
            assertTrue(apps.contains("WeDo"));
        }


        @Test
        void listAllGameAppsWhenTheyExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "cooking", 1000, 2.0, 1.99,  true);
            ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "cooking", 1000, 2.0, 1.99);
            EducationApp app3 = new EducationApp(developerLego, "cooking", 1000, 2.0, 1.99, 10);
            GameApp app4 = new GameApp(developerKoolGames, "fifia", 1000, 2.0, 1.99,  true);
            test.addApp(app1);test.addApp(app2); test.addApp(app3); test.addApp(app4);

            String testList = test.listAllGameApps();

            assertTrue(test.listAllGameApps().contains(app1.toString()));
            assertTrue(test.listAllGameApps().contains(app4.toString()));
        }

        @Test
        void listAllGameAppsWhenTheyDontExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            EducationApp app1 = new EducationApp(developerLego, "cooking", 1000, 2.0, 1.99, 10);
            test.addApp(app1);
            String testList = test.listAllGameApps();
            assertTrue(test.listAllGameApps().contains("no game apps"));

        }




        @Test
        void listAllGameAppsWhenEmpty()
        {
            String test = emptyAppStore.listAllGameApps();
            assertTrue(test.contains("no apps in the system"));
        }



        @Test
        void listAllGameAppsWhenItExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "GTA", 1000, 2.0, 1.99,  true);
            test.addApp(app1);
            GameApp app2 = new GameApp(developerKoolGames, "FIFIA", 1000, 2.0, 1.99,  true);
            test.addApp(app2);
            String testList = test.listAllGameApps();
            assertTrue(test.listAllGameApps().contains(app1.toString()));
            assertTrue(test.listAllGameApps().contains(app2.toString()));

        }

        @Test
        void listAllEducationAppsWhenItDosentExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "GTA", 1000, 2.0, 1.99,  true);
            test.addApp(app1);

            String testList = test.listAllEducationApps();
            assertTrue(test.listAllEducationApps().contains("no education apps"));

        }

        @Test
        void listAllEducationAppsWhenEmpty()
        {
            String test = emptyAppStore.listAllEducationApps();
            assertTrue(test.contains("no apps in the system"));
        }


        @Test
        void listAllEducationAppsWhenItExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            EducationApp app1 = new EducationApp(developerLego, "cooking", 1000, 2.0, 1.99, 10);
            test.addApp(app1);
            EducationApp app2 = new EducationApp(developerLego, "baking", 1000, 2.0, 1.99, 10);
            test.addApp(app2);
            String testList = test.listAllEducationApps();
            assertTrue(test.listAllEducationApps().contains(app1.toString()));
            assertTrue(test.listAllEducationApps().contains(app2.toString()));

        }

        @Test
        void listAllProductivityAppsWhenItDosentExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            ProductivityApp app1 = new ProductivityApp(developerMicrosoft, "To DO List", 1000, 2.0, 1.99);
            test.addApp(app1);
            ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "Calculator", 1000, 2.0, 1.99);
            test.addApp(app2);

            String testList = test.listAllProductivityApps();
            assertTrue(test.listAllProductivityApps().contains(app1.toString()));
            assertTrue(test.listAllProductivityApps().contains(app2.toString()));

        }

        @Test
        void listAllProductivityAppsWhenEmpty()
        {
            String test = emptyAppStore.listAllProductivityApps();
            assertTrue(test.contains("no apps in the system"));
        }


        @Test
        void listAllProductivityAppsWhenItExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "GTA", 1000, 2.0, 1.99,  true);
            test.addApp(app1);

            String testList = test.listAllProductivityApps();
            assertTrue(test.listAllProductivityApps().contains("no productivity apps"));

        }
        @Test
        void listSummaryOfAllAppsWhenEmpty()
        {
            String test = emptyAppStore.listSummaryOfAllApps();
            assertTrue(test.contains("no apps"));
        }

        @Test
        void listSummaryOfAllAppsWhenItExist()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "GTA", 1000, 2.0, 1.99,  true);
            ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "cooking", 1000, 2.0, 1.99);
            EducationApp app3 = new EducationApp(developerLego, "cooking", 1000, 2.0, 1.99, 10);
            test.addApp(app1);test.addApp(app2); test.addApp(app3);

            String testList = test.listSummaryOfAllApps();
            assertTrue(testList.contains(app1.appSummary()));
            assertTrue(testList.contains(app2.appSummary()));
            assertTrue(testList.contains(app3.appSummary()));
        }


    }

    @Nested
    class ReportingMethods {
        @Test
        void listAllAppByNameWhenNoneExist()
        {
            assertEquals(11, appStore.numberOfApps());
            assertEquals("No apps found for: "+"hello world", appStore.listAllAppsByName("hello world"));

        }

        @Test
        void listAllAppByNameWhenEmpty()
        {
            assertEquals(0, emptyAppStore.numberOfApps());
            assertEquals("no apps in the system", emptyAppStore.listAllAppsByName("hello world"));

        }

        @Test
        void listAllAppsByNameThatExistOne()
        {
            assertEquals(11, appStore.numberOfApps());
            assertEquals(prodAppOnBoundary.toString()+"\n", appStore.listAllAppsByName("Outlook"));
        }

        @Test
        void listAllAppsByNameThatExistMultiple()
        {
            AppStoreAPI test = new AppStoreAPI();
            GameApp app1 = new GameApp(developerKoolGames, "cooking", 1000, 2.0, 1.99,  true);
            ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "cooking", 1000, 2.0, 1.99);
            EducationApp app3 = new EducationApp(developerLego, "cooking", 1000, 2.0, 1.99, 10);
            test.addApp(app1);test.addApp(app2); test.addApp(app3);
            assertEquals(3, test.numberOfApps());
            assertEquals(app1.toString()+"\n"+app2.toString()+"\n"+app3.toString()+"\n", test.listAllAppsByName("cooking"));
        }


        @Test
        void listAllAppsAboveOrEqualAGivenStarRatingWhenEmpty()
        {
            assertEquals("No Apps in the system", emptyAppStore.listAllAppsAboveOrEqualAGivenStarRating(1));
        }

        @Test
        void listAllAppsAboveOrEqualAGivenStarRatingWhenMinus()
        {
            assertEquals("Sorry please input number bigger than or equal to 0", appStore.listAllAppsAboveOrEqualAGivenStarRating(-1));
            assertEquals("Sorry please input number bigger than or equal to 0", appStore.listAllAppsAboveOrEqualAGivenStarRating(-2));
            assertEquals("Sorry please input number bigger than or equal to 0", appStore.listAllAppsAboveOrEqualAGivenStarRating(-3));
        }

        @Test
        void listAllAppsAboveOrEqualAGivenStarRatingWhenNoRating()
        {
            assertEquals("No apps have a rating of " +"10"+ " or above",appStore.listAllAppsAboveOrEqualAGivenStarRating(10));
            assertEquals("No apps have a rating of " +"100"+ " or above",appStore.listAllAppsAboveOrEqualAGivenStarRating(100));
            assertEquals("No apps have a rating of " +"200"+ " or above",appStore.listAllAppsAboveOrEqualAGivenStarRating(200));
        }

        @Test
        void listAllAppsAboveOrEqualAGivenStarRatingWhenValid()
        {
            EducationApp app1 =setupEducationAppWithRating(1, 1);
            GameApp app2=setupGameAppWithRating(3, 2);
            ProductivityApp app3 = setupProductivityAppWithRating(3, 2);
            appStore.addApp(app1); appStore.addApp(app2); appStore.addApp(app3);

            String test1  =appStore.listAllAppsAboveOrEqualAGivenStarRating(1);
            assertTrue(test1.contains(app1.toString()));
            assertTrue(test1.contains(app2.toString()));
            assertTrue(test1.contains(app3.toString()));

            String test2 = appStore.listAllAppsAboveOrEqualAGivenStarRating(2);
            assertFalse(test2.contains(app1.toString()));
            assertTrue(test1.contains(app2.toString()));
            assertTrue(test1.contains(app3.toString()));
        }

        @Test
        void listAllAppsByChosenDeveloperEmpty()
        {
            String test =emptyAppStore.listAllAppsByChosenDeveloper(developerApple);
            assertTrue(test.contains("no apps in the system"));
        }

        @Test
        void listAllAppsByChosenDeveloperNoMatchingDeveloper()
        {
            String test =appStore.listAllAppsByChosenDeveloper(developerNoMatches);
            assertTrue(test.contains("No apps for developer: "));
            assertTrue(test.contains(developerNoMatches.toString()));
        }

        @Test
        void listAllAppsByChosenDeveloperWithMatching()
        {
            String test1 =appStore.listAllAppsByChosenDeveloper(developerMicrosoft);
            assertTrue(test1.contains(prodAppOnBoundary.toString()));
            assertTrue(test1.contains(developerMicrosoft.toString()));

            String test2 = appStore.listAllAppsByChosenDeveloper(developerEAGames);
            assertTrue(test2.contains(gameAppAboveBoundary.toString()));
            assertTrue(test2.contains(gameAppBelowBoundary.toString()));
            assertTrue(test2.contains(developerEAGames.toString()));



        }

        @Test
        void numberOfAppsByChosenDeveloperEmpty()
        {
            assertEquals(0, appStore.numberOfAppsByChosenDeveloper(developerNoMatches));
        }


        @Test
        void numberOfAppsByChosenDeveloperEmptyArray()
        {
            assertEquals(0, emptyAppStore.numberOfAppsByChosenDeveloper(developerNoMatches));
        }

        @Test
        void numberOfAppsByChosenDeveloperMatching()
        {
            assertEquals(4, appStore.numberOfAppsByChosenDeveloper(developerLego));
            assertEquals(2, appStore.numberOfAppsByChosenDeveloper(developerMicrosoft));
        }


    }


    @Nested
    class SortingMethods {



        @Test
        void sortByNameAscendingReOrdersList() {
            appStore.sortAppsByNameAscending();
            // wouldn't work in this test so created a new "AppStoreAPiTestSORTINGMETHODS"w
            //work in that test class.
            //89% line testing overall
        }

        @Test
        void sortByNameAscendingDoesntCrashWhenListIsEmpty() {
            assertEquals(0,emptyAppStore.numberOfApps());
            emptyAppStore.sortAppsByNameAscending();
        }

    }

    @Nested
    class UpdateMethods{



        AppStoreAPI test = new AppStoreAPI();
        GameApp app1 = new GameApp(developerKoolGames, "cooking", 1000, 2.0, 1.99,  true);
        ProductivityApp app2 = new ProductivityApp(developerMicrosoft, "apple", 1000, 2.0, 1.99);
        EducationApp app3 = new EducationApp(developerLego, "banna", 1000, 2.0, 1.99, 10);

        @Test
        void superUpdateAppTest()
        {   test.addApp(app1);
            test.updateApp("cooking",developerKoolGames, "newCooking1", 99, 3.0, 2);
            assertEquals("newCooking1", app1.getAppName());
            assertEquals(developerKoolGames, app1.getDeveloper());
            assertEquals(99, app1.getAppSize());
            assertEquals(3.0, app1.getAppVersion());
            assertEquals(2, app1.getAppCost());


        }

        @Test
        void updateEducationApp()
        {
            test.addApp(app3);
            test.updateEducationApp(app3.getAppName(),developerKoolGames,"world",2,4.0,2.0,8 );
            assertEquals("world", app3.getAppName());
            assertEquals(developerKoolGames, app3.getDeveloper());
            assertEquals(2, app3.getAppSize());
            assertEquals(4.0, app3.getAppVersion());
            assertEquals(2.0, app3.getAppCost());
            assertEquals(8  ,((EducationApp) app3).getLevel());


        }

        @Test
        void updateGameApp()
        {
            test.addApp(app1);test.addApp(app2); test.addApp(app3);
            test.updateGameApp(app1.getAppName(),developerKoolGames,"gta",10,3.0,3.0,false );
            assertEquals("gta", app1.getAppName());
            assertEquals(developerKoolGames, app1.getDeveloper());
            assertEquals(10, app1.getAppSize());
            assertEquals(3.0, app1.getAppVersion());
            assertEquals(3.0, app1.getAppCost());
            assertFalse( ((GameApp) app1).isMultiplayer());

        }

        @Test
        void updateProductivityApp()
        {
            test.addApp(app2);
            test.updateProductivityApp(app2.getAppName(), developerMicrosoft, "apple2", 1000, 2.0, 4);
            assertEquals("apple2", app2.getAppName());
            assertEquals(developerMicrosoft, app2.getDeveloper());
            assertEquals(1000, app2.getAppSize());
            assertEquals(2.0, app1.getAppVersion());
            assertEquals(4, app2.getAppCost());


        }



    }
    //--------------------------------------------
    // Helper Methods
    //--------------------------------------------
    EducationApp setupEducationAppWithRating(int rating1, int rating2) {
        //setting all conditions to true
        EducationApp edApp = new EducationApp(developerLego, "WeDo", 1,
                1.0, 1.00, 3);
        edApp.addRating(new Rating(rating1, "John Doe", "Very Good"));
        edApp.addRating(new Rating(rating2, "Jane Doe", "Excellent"));

        return edApp;
    }

    GameApp setupGameAppWithRating(int rating1, int rating2) {
        GameApp gameApp = new GameApp(developerEAGames, "MazeRunner", 1,
                1.0, 1.00, true);
        gameApp.addRating(new Rating(rating1, "John Soap", "Exciting Game"));
        gameApp.addRating(new Rating(rating2, "Jane Soap", "Nice Game"));
        return gameApp;
    }

    ProductivityApp setupProductivityAppWithRating(int rating1, int rating2) {
        ProductivityApp productivityApp = new ProductivityApp(developerApple, "Evernote", 1,
                1.0, 1.99);

        productivityApp.addRating(new Rating(rating1, "John101", "So easy to add a note"));
        productivityApp.addRating(new Rating(rating2, "Jane202", "So useful"));
        return productivityApp;
    }

}