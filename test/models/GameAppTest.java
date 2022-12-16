package models;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GameAppTest {

    private GameApp gAppBelowBoundary, gAppOnBoundary, gAppAboveBoundary, gAppInvalidData;
    private Developer developerLego = new Developer("Lego", "www.lego.com");
    private Developer developerSphero = new Developer("Sphero", "www.sphero.com");

    @BeforeEach
    void setUp() {
        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0), level(1-10), isMultiplayer(false).
        gAppBelowBoundary = new GameApp(developerLego, "WeDo", 1, 1.0, 0,false);
        gAppOnBoundary = new GameApp(developerLego, "Spike", 1000, 2.0, 1.99,true);
        gAppAboveBoundary = new GameApp(developerLego, "EV3", 1001, 3.5, 2.99,false);
        gAppInvalidData = new GameApp(developerLego, "", -1, 0, -1.00,false);
    }

    @AfterEach
    void tearDown() {
        gAppBelowBoundary = gAppOnBoundary = gAppAboveBoundary = gAppInvalidData = null;
        developerLego = developerSphero = null;
    }

    @Nested
    class Getters {
        @Test
        void isMultiplayer() {
            assertEquals(false, gAppBelowBoundary.isMultiplayer());
            assertEquals(true, gAppOnBoundary.isMultiplayer());
            assertEquals(false, gAppAboveBoundary.isMultiplayer());
            assertEquals(false, gAppInvalidData.isMultiplayer());
        }
    }
    @Nested
    class Setters {
        @Test
        void setMultiplayer() {
            //Validation: appSize(1-1000)
            assertEquals(false, gAppBelowBoundary.isMultiplayer());

            gAppBelowBoundary.setMultiplayer(false);
            assertEquals(false, gAppBelowBoundary.isMultiplayer()); //update

            gAppBelowBoundary.setMultiplayer(false);
            assertEquals(false, gAppBelowBoundary.isMultiplayer()); //no update

            gAppBelowBoundary.setMultiplayer(false);
            assertEquals(false, gAppBelowBoundary.isMultiplayer()); //update

            gAppBelowBoundary.setMultiplayer(false);
            assertEquals(false, gAppBelowBoundary.isMultiplayer()); //no update
        }

    }

    @Nested
    class ObjectStateMethods {

        @Test
        void appSummaryReturnsCorrectString() {
            GameApp gApp = setupGameAppWithRating(3, 3);
            String stringContents = gApp.appSummary();

            assertTrue(stringContents.contains(gApp.getAppName() + "(V" + gApp.getAppVersion()));
            assertTrue(stringContents.contains(gApp.getDeveloper().toString()));
            assertTrue(stringContents.contains("€" + gApp.getAppCost()));
            assertTrue(stringContents.contains("Rating: " + gApp.calculateRating()));
            assertTrue(stringContents.contains("Multiplayer= " + gApp.isMultiplayer()));
        }

        @Test
        void toStringReturnsCorrectString() {
            GameApp gApp = setupGameAppWithRating(3, 3);
            String stringContents = gApp.toString();

            assertTrue(stringContents.contains(gApp.getAppName()));
            assertTrue(stringContents.contains("(Version " + gApp.getAppVersion()));
            assertTrue(stringContents.contains(gApp.getDeveloper().toString()));
            assertTrue(stringContents.contains(gApp.getAppSize() + "MB"));
            assertTrue(stringContents.contains("Cost: " + gApp.getAppCost()));
            assertTrue(stringContents.contains("Ratings (" + gApp.calculateRating()));
            assertTrue(stringContents.contains("Multiplayer= " + gApp.isMultiplayer()));

            //contains list of ratings too
            assertTrue(stringContents.contains("John Doe"));
            assertTrue(stringContents.contains("Very Good"));
            assertTrue(stringContents.contains("Jane Doe"));
            assertTrue(stringContents.contains("Excellent"));
        }
    }
    @Nested
    class RecommendedApp {

        @Test
        void appIsNotRecommendedWhenMultiplayerIsFalse() {
            //setting all conditions to true with ratings of 3 and 4 (i.e. 3.5)
            GameApp gApp = setupGameAppWithRating(3, 3);

            //now setting appCost to 1.99 so app should not be recommended now
            gApp.setMultiplayer(false);
            assertFalse(gApp.isRecommendedApp());
        }

        @Test
        void appIsNotRecommendedWhenRatingIsLessThan4() {
            //setting all conditions to true with ratings of 3 and 3 (i.e. 3.0)
            GameApp gApp = setupGameAppWithRating(3, 3);
            //verifying recommended app returns false (rating not high enough
            assertFalse(gApp.isRecommendedApp());
        }
        @Test
        void appIsRecommendedWhenAllOfTheConditionsAreTrue() {
            //setting all conditions to true with ratings of 3
            GameApp gApp = setupGameAppWithRating(4, 5);

            //verifying recommended app returns true
            assertTrue(gApp.isRecommendedApp());
        }
    }

    GameApp setupGameAppWithRating(int rating1, int rating2) {
        //setting all conditions to true
        GameApp gApp = new GameApp(developerLego, "WeDo", 1, 1.0, 1.00,true);
        gApp.addRating(new Rating(rating1, "John Doe", "Very Good"));
        gApp.addRating(new Rating(rating2, "Jane Doe", "Excellent"));

        //verifying all conditions are true for a recommended product app]
        assertEquals(2, gApp.getRatings().size());  //two ratings are added
        assertEquals(1.0, gApp.getAppCost(), 0.01);
        assertEquals(((rating1 + rating2) / 2.0), gApp.calculateRating(), 0.01);

        return gApp;

    }
}
