package edu.brown.cs32.API.server;

import edu.brown.cs32.API.database.Firebase;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.brown.cs32.API.server.UserHandling.GetUserPostsHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

/**
 * this testing class was written before the firebase testing was implemented; injection testing
 * framework is featured, but this suite isn't currently compatible with firebase testing procedures.
 */
public class TestGetUserPostsHandler {
  /** Initializing the server before running tests */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(111);
    Logger.getLogger("").setLevel(Level.WARNING);
    System.out.println("testing server started.");
  }

  /**
   * Before each test, clears the contents of the csvContents and initializing commands and handlers
   */
  @BeforeEach
  public void setup() {
    Spark.get("personalposts", new GetUserPostsHandler());
    Spark.init();
    Spark.awaitInitialization();
  }

  /** Tears down the API server by unmapping the commands and stopping the server */
  @AfterEach
  public void teardown() {
    Spark.unmap("personalposts");
    Spark.stop();
    Spark.awaitStop();
  }

  /** wheat for making a post with unspecified type (should make car post) */
  @Test
  public void testhelpme() throws Exception {
//    Post myPost = new CarPost("molly_mchenry", "xmas travel plans!", "Providence",
//       "Greenville", new Date(2022-1900, 11, 21), new Date(2023-1900, 0, 20)));

      Firebase.getPost("1");
//    String param = "personalposts?userID=molly_mchenry";
//    Assertions.assertEquals(200, tryRequestConnection(param).getResponseCode());
//    System.out.println("API RESULT: "+tryRequestBody(param));
//
//    URI uri = new URI("http://localhost:111/" + param);
//    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
//    HttpResponse<String> response =
//        HttpClient.newBuilder().build().send(request, BodyHandlers.ofString());
//    System.out.println("RESPONSE: "+response.body());
//    Assertions.assertTrue(tryRequestBody().contains("success"));
  }
}
