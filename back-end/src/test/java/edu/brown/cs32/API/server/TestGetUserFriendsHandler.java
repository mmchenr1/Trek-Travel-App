package edu.brown.cs32.API.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.brown.cs32.API.server.PostHandling.PostHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import spark.Spark;

// TODO
public class TestGetUserFriendsHandler {

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
    Spark.get("getfriends", new PostHandler());
    Spark.init();
    Spark.awaitInitialization();
  }

  /** Tears down the API server by unmapping the commands and stopping the server */
  @AfterEach
  public void teardown() {
    Spark.unmap("getfriends");
    Spark.stop();
    Spark.awaitStop();
  }
}
