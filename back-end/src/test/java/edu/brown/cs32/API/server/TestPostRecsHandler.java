package edu.brown.cs32.API.server;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.brown.cs32.API.server.PostHandling.PostRecsHandler;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Spark;

// TODO

/** tests post recommendation handler in the server folder (API handler) -- INTEGRATION TESTING */
public class TestPostRecsHandler {
  /** Initializing the server before running tests */
  @BeforeAll
  public static void setup_before_everything() {
    Spark.port(111);
    Logger.getLogger("").setLevel(Level.WARNING);
    System.out.println("testing server started.");

    // setup objects
  }

  /**
   * Before each test, clears the contents of the csvContents and initializing commands and handlers
   */
  @BeforeEach
  public void setup() {
    Spark.get("postrecs", new PostRecsHandler());
    Spark.init();
    Spark.awaitInitialization();
  }

  /** Tears down the API server by unmapping the commands and stopping the server */
  @AfterEach
  public void teardown() {
    Spark.unmap("postrecs");
    Spark.stop();
    Spark.awaitStop();
  }

  /** */
  @Test
  public void getRecs() {
    // TODO
  }

  /** */
  @Test
  public void getMoreRecs() {
    // TODO
  }

  /** */
  @Test
  public void getRecsEmptyDB() {
    // TODO
  }
}
