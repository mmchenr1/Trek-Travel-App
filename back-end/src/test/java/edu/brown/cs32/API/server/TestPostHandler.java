package edu.brown.cs32.API.server;

import static edu.brown.cs32.API.server.HandlerTestUtilities.tryRequestBody;
import static edu.brown.cs32.API.server.HandlerTestUtilities.tryRequestConnection;

import edu.brown.cs32.API.database.Firebase;
import edu.brown.cs32.API.server.PostHandling.PostHandler;
import edu.brown.cs32.API.users.User;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/**
 * tests post creation handler in the server folder (API handler) -- INTEGRATION TESTING test cases:
 * 1. plane post creation with all fields (validly) populated 2. train post creation with all fields
 * (validly) populated 3. car post creation with all fields (validly) populated 4. post of
 * unspecified type creation with all fields (validly) populated 5. attempt to make a post with no
 * fields specified 6. make post with only startDate and toLocation fields specified 7. attempt to
 * make post when start date is after end date 8. duplicate posts are successfully made 9. check
 * that post creation is added to correct user object //TODO -- Firebase emulator
 */
public class TestPostHandler {
  static String wheatNewPost_Plane;
  static String wheatNewPost_Train;
  static String wheatNewPost_Car;
  static String wheatNewPost_Unspecified;

  String textInvalidDates = "error_invalid_date_format";
  String textEmptyPostFields = "error_required_fields_left_empty";

  /** Initializing the server before running tests */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(111);
    Logger.getLogger("").setLevel(Level.WARNING);
    System.out.println("testing server started.");

    // setup objects
    wheatNewPost_Plane =
        "newpost?"
            + "userID=molly_mchenry&&"
            + "type=PLANE&&"
            + "text=hi&&"
            + "fromLocation=PVD&&"
            + "toLocation=LAX&&"
            + "startDate=01/01/2023&&"
            + "endDate=01/20/2023&&"
            + "toFlights=BA2490,BA2491A&&"
            + "returnFlights=BA3991";

    wheatNewPost_Train =
        "newpost?"
            + "userID=molly_mchenry&&"
            + "type=TRAIN&&"
            + "text=hi&&"
            + "fromLocation=PVD&&"
            + "toLocation=LAX&&"
            + "startDate=01/01/2023&&"
            + "endDate=01/20/2023&&"
            + "toFlights=BA2490,BA2491A&&"
            + "returnFlights=BA3991";

    wheatNewPost_Car =
        "newpost?"
            + "userID=molly_mchenry&&"
            + "type=CAR&&"
            + "text=hi&&"
            + "fromLocation=PVD&&"
            + "toLocation=LAX&&"
            + "startDate=01/01/2023&&"
            + "endDate=01/20/2023&&"
            + "toFlights=BA2490,BA2491A&&"
            + "returnFlights=BA3991";

    wheatNewPost_Unspecified =
        "newpost?"
            + "userID=molly_mchenry&&"
            + "text=hi&&"
            + "fromLocation=PVD&&"
            + "toLocation=LAX&&"
            + "startDate=01/01/2023&&"
            + "endDate=01/20/2023&&"
            + "toFlights=BA2490,BA2491A&&"
            + "returnFlights=BA3991";
  }

  /**
   * Before each test, clears the contents of the csvContents and initializing commands and handlers
   */
  @BeforeEach
  public void setup() {
    Spark.get("newpost", new PostHandler());
    Firebase.FirebaseInitializer(true);
    Spark.init();
    Spark.awaitInitialization();
  }

  /** Tears down the API server by unmapping the commands and stopping the server */
  @AfterEach
  public void teardown() {
    Spark.unmap("newpost");
    Spark.stop();
    Spark.awaitStop();
  }

  /** wheat for making a plane post with all fields populated */
  @Test
  public void makePostPlane() throws IOException, URISyntaxException, InterruptedException {
    Assertions.assertEquals(200, tryRequestConnection(wheatNewPost_Plane).getResponseCode());
    Assertions.assertTrue(tryRequestBody(wheatNewPost_Plane).contains("success"));
  }

  /** wheat for making a plane post with all fields populated */
  @Test
  public void makePostTrain() throws IOException, URISyntaxException, InterruptedException {
    Assertions.assertEquals(200, tryRequestConnection(wheatNewPost_Train).getResponseCode());
    Assertions.assertTrue(tryRequestBody(wheatNewPost_Train).contains("success"));
  }

  /** wheat for making a plane post with all fields populated */
  @Test
  public void makePostCar() throws IOException, URISyntaxException, InterruptedException {
    Assertions.assertEquals(200, tryRequestConnection(wheatNewPost_Car).getResponseCode());
    Assertions.assertTrue(tryRequestBody(wheatNewPost_Car).contains("success"));
  }

  /** wheat for making a post with unspecified type (should make car post) */
  @Test
  public void makePostUnspecified() throws IOException, URISyntaxException, InterruptedException {
    Assertions.assertEquals(200, tryRequestConnection(wheatNewPost_Unspecified).getResponseCode());
    Assertions.assertTrue(tryRequestBody(wheatNewPost_Unspecified).contains("success"));
  }

  /** no params should result in an error */
  @Test
  public void makeEmptyPost() throws IOException, URISyntaxException, InterruptedException {
    Assertions.assertEquals(200, tryRequestConnection("newpost?").getResponseCode());
    Assertions.assertTrue(tryRequestBody("newpost?").contains(textEmptyPostFields));
  }

  /** only bare min params for constructor to accept a post */
  @Test
  public void makeBareMinPost() throws IOException, URISyntaxException, InterruptedException {
    String bareMinPost =
        "newpost?" + "userID=molly_mchenry" + "&&startDate=01/01/2001" + "&&toLocation=PBI";
    Assertions.assertEquals(200, tryRequestConnection(bareMinPost).getResponseCode());
    Assertions.assertTrue(tryRequestBody(bareMinPost).contains("success"));
  }

  /** error when start date is after end date */
  @Test
  public void makeInvalidDatesPost() throws IOException, URISyntaxException, InterruptedException {
    String badDatesPost =
        "newpost?"
            + "user=m&&"
            + "toLocation=PBI&&"
            + "startDate=01/10/2001"
            + "&&"
            + "endDate=01/01/2001";
    System.out.println(tryRequestConnection(badDatesPost).getResponseCode());
    Assertions.assertEquals(200, tryRequestConnection(badDatesPost).getResponseCode());
    Assertions.assertTrue(tryRequestBody(badDatesPost).contains(textInvalidDates));
  }

  /** test when the same post is made twice that it goes through both times */
  @Test
  public void makeDuplicatePost() throws IOException, URISyntaxException, InterruptedException {
    Assertions.assertEquals(200, tryRequestConnection(wheatNewPost_Car).getResponseCode());
    Assertions.assertTrue(tryRequestBody(wheatNewPost_Car).contains("success"));

    // second time
    Assertions.assertEquals(200, tryRequestConnection(wheatNewPost_Car).getResponseCode());
    Assertions.assertTrue(tryRequestBody(wheatNewPost_Car).contains("success"));
  }

  /** test that when a post is made its UID is added to that user's posts field */
  @Test
  public void userUpdatesWithPostCreation() throws IOException {
    Firebase.setUser(new User("joe_goldberg", "Joe"));
    Assertions.assertTrue(
        Firebase.getPostsforUser("joe_goldberg").size()
            == 0); // before any posts created, empty posts field

    tryRequestConnection("newpost?userID=joe_goldberg&&startDate=01/01/2001&&toLocation=PBI");
    Assertions.assertTrue(
        Firebase.getPostsforUser("joe_goldberg").size() == 1); // check that post added

    tryRequestConnection("newpost?userID=joe_goldberg&&startDate=01/01/2001&&toLocation=PBI");
    Assertions.assertTrue(
        Firebase.getPostsforUser("joe_goldberg").size() == 2); // check that post added (duplicate)
  }

  /**
   * test that when a post is attempted to be made but fails, its UID is NOT addedto that user's
   * posts field
   */
  @Test
  public void userNoUpdateWithPostFailure() throws IOException {
    Firebase.setUser(new User("phoebe_bridgers", "Phoebe"));
    Assertions.assertTrue(
        Firebase.getPostsforUser("phoebe_bridgers").size()
            == 0); // before any posts created, empty posts field

    tryRequestConnection("newpost?userID=phoebe_bridgers&&toLocation=PBI"); // no start date
    Assertions.assertTrue(
        Firebase.getPostsforUser("phoebe_bridgers").size() == 0); // check that post not added
  }
}
