package models;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductivityAppTest {

    private ProductivityApp prAppBelowBoundary, prAppOnBoundary, prAppAboveBoundary, prAppInvalidData;

    private Developer developerLego = new Developer("Lego", "www.lego.com");
    private Developer developerSphero = new Developer("Sphero", "www.sphero.com");

    @BeforeEach
    void setUp()
    {
        //Validation: appSize(1-1000), appVersion(>=1.0), ageRating (0-18), appCost(>=0), level(1-10).
        prAppBelowBoundary = new ProductivityApp( developerLego, "WeDo", 1, 1.0, 0);
        prAppOnBoundary = new ProductivityApp(developerLego, "Spike", 1000, 2.0, 1.99);
        prAppAboveBoundary = new ProductivityApp(developerLego, "EV3", 1001, 3.5, 2.99);
        prAppInvalidData = new ProductivityApp(developerLego, "", -1, 0, -1.00);
    }

    @AfterEach
    void tearDown() {
        prAppBelowBoundary = prAppOnBoundary = prAppAboveBoundary = prAppInvalidData = null;
        developerLego = developerSphero = null;
    }


    @Nested
    class Getters {

        @Test
        void getDeveloper() {
            assertEquals(developerLego, prAppBelowBoundary.getDeveloper());
            assertEquals(developerLego, prAppOnBoundary.getDeveloper());
            assertEquals(developerLego, prAppAboveBoundary.getDeveloper());
            assertEquals(developerLego, prAppInvalidData.getDeveloper());
        }

        @Test
        void getAppName() {
            assertEquals("WeDo", prAppBelowBoundary.getAppName());
            assertEquals("Spike", prAppOnBoundary.getAppName());
            assertEquals("EV3", prAppAboveBoundary.getAppName());
            assertEquals("", prAppInvalidData.getAppName());
        }

        @Test
        void getAppSize() {
            assertEquals(1, prAppBelowBoundary.getAppSize());
            assertEquals(1000, prAppOnBoundary.getAppSize());
            assertEquals(0, prAppAboveBoundary.getAppSize());
            assertEquals(0, prAppInvalidData.getAppSize());
        }

        @Test
        void getAppVersion() {
            assertEquals(1.0, prAppBelowBoundary.getAppVersion());
            assertEquals(2.0, prAppOnBoundary.getAppVersion());
            assertEquals(3.5, prAppAboveBoundary.getAppVersion());
            assertEquals(1.0, prAppInvalidData.getAppVersion());
        }

        @Test
        void getAppCost() {
            assertEquals(0, prAppBelowBoundary.getAppCost());
            assertEquals(1.99, prAppOnBoundary.getAppCost());
            assertEquals(2.99, prAppAboveBoundary.getAppCost());
            assertEquals(0, prAppInvalidData.getAppCost());
        }



    }

    @Nested
    class Setters {

        @Test
        void setDeveloper() {
            //no validation in models
            assertEquals(developerLego, prAppBelowBoundary.getDeveloper());
            prAppBelowBoundary.setDeveloper(developerSphero);
            assertEquals(developerSphero, prAppBelowBoundary.getDeveloper());
        }

        @Test
        void setAppName() {
            //no validation in models
            assertEquals("WeDo", prAppBelowBoundary.getAppName());
            prAppBelowBoundary.setAppName("Mindstorms");
            assertEquals("Mindstorms", prAppBelowBoundary.getAppName());
        }

        @Test
        void setAppSize() {
            //Validation: appSize(1-1000)
            assertEquals(1, prAppBelowBoundary.getAppSize());

            prAppBelowBoundary.setAppSize(1000);
            assertEquals(1000, prAppBelowBoundary.getAppSize()); //update

            prAppBelowBoundary.setAppSize(1001);
            assertEquals(1000, prAppBelowBoundary.getAppSize()); //no update

            prAppBelowBoundary.setAppSize(2);
            assertEquals(2, prAppBelowBoundary.getAppSize()); //update

            prAppBelowBoundary.setAppSize(0);
            assertEquals(2, prAppBelowBoundary.getAppSize()); //no update
        }

        @Test
        void setAppVersion() {
            //Validation: appVersion(>=1.0)
            assertEquals(1.0, prAppBelowBoundary.getAppVersion());

            prAppBelowBoundary.setAppVersion(2.0);
            assertEquals(2.0, prAppBelowBoundary.getAppVersion()); //update

            prAppBelowBoundary.setAppVersion(0.0);
            assertEquals(2.0, prAppBelowBoundary.getAppVersion()); //no update

            prAppBelowBoundary.setAppVersion(1.0);
            assertEquals(1.0, prAppBelowBoundary.getAppVersion()); //update
        }

        @Test
        void setAppCost() {
            //Validation: appCost(>=0)
            assertEquals(0.0, prAppBelowBoundary.getAppCost());

            prAppBelowBoundary.setAppCost(1.0);
            assertEquals(1.0, prAppBelowBoundary.getAppCost()); //update

            prAppBelowBoundary.setAppCost(-1);
            assertEquals(1.0, prAppBelowBoundary.getAppCost()); //no update

            prAppBelowBoundary.setAppCost(0.0);
            assertEquals(0.0, prAppBelowBoundary.getAppCost()); //update
        }



    }



    @Nested // mutiple test in the one class.
    class ObjectStateMethods {

        @Test
        void appSummaryReturnsCorrectString() {
            ProductivityApp prApp = setupProductivityAppWithRating(3, 4);
            String stringContents = prApp.appSummary();

            assertTrue(stringContents.contains(prApp.getAppName() + "(V" + prApp.getAppVersion()));
            assertTrue(stringContents.contains(prApp.getDeveloper().toString()));
            assertTrue(stringContents.contains("€" + prApp.getAppCost()));
            assertTrue(stringContents.contains("Rating: " + prApp.calculateRating()));
        }

        @Test
        void toStringReturnsCorrectString() {
            ProductivityApp prApp = setupProductivityAppWithRating(3, 4);
            String stringContents = prApp.toString();

            assertTrue(stringContents.contains(prApp.getAppName()));
            assertTrue(stringContents.contains("(Version " + prApp.getAppVersion()));
            assertTrue(stringContents.contains(prApp.getDeveloper().toString()));
            assertTrue(stringContents.contains(prApp.getAppSize() + "MB"));
            assertTrue(stringContents.contains("Cost: " + prApp.getAppCost()));
            assertTrue(stringContents.contains("Ratings (" + prApp.calculateRating()));

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
        void appIsNotRecommendedWhenInAppCostIs99c() {
            //setting all conditions to true with ratings of 3 and 4 (i.e. 3.5)
            ProductivityApp prApp = setupProductivityAppWithRating(3, 4);

            //now setting appCost to 0.99 so app should not be recommended now
            prApp.setAppCost(0.99);
            assertFalse(prApp.isRecommendedApp());
        }

        @Test
        void appIsNotRecommendedWhenRatingIsLessThan1() {
            //setting all conditions to true with ratings of 3 and 3 (i.e. 3.0)
            ProductivityApp prApp = setupProductivityAppWithRating(1, 1);
            //verifying recommended app returns false (rating not high enough
            assertFalse(prApp.isRecommendedApp());
        }

        @Test
        void appIsNotRecommendedWhenNoRatingsExist() {
            //setting all conditions to true with no ratings
            ProductivityApp prApp = new ProductivityApp(developerLego, "WeDo", 1,
                    1.0, 1.00);
            //verifying recommended app returns true
            assertFalse(prApp.isRecommendedApp());
        }

        @Test
        void appIsRecommendedWhenAllOfTheConditionsAreTrue() {
            //setting all conditions to true with ratings of 3 and 4 (i.e. 3.5)
            ProductivityApp prApp = setupProductivityAppWithRating(3, 4);

            //verifying recommended app returns true
            assertTrue(prApp.isRecommendedApp());
        }

    }

    ProductivityApp setupProductivityAppWithRating(int rating1, int rating2) {
        //setting all conditions to true
        ProductivityApp prApp = new ProductivityApp(developerLego, "WeDo", 1,
                1.0, 2.00);
        prApp.addRating(new Rating(rating1, "John Doe", "Very Good"));
        prApp.addRating(new Rating(rating2, "Jane Doe", "Excellent"));

        //verifying all conditions are true for a recommended educational app]
        assertEquals(2, prApp.getRatings().size());  //two ratings are added
        assertEquals(2.0, prApp.getAppCost(), 0.01);
        assertEquals(((rating1 + rating2) / 2.0), prApp.calculateRating(), 0.01);
        return prApp;
    }

}
