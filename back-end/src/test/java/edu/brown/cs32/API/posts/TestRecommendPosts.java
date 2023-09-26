package edu.brown.cs32.API.posts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/** tests post recommendation handler in the posts folder (API handler) -- UNIT TESTING */
public class TestRecommendPosts {
  static RecommendPosts rp;
  static Post post1;
  static Post post2;


  @BeforeAll
  static void setup() throws Exception {
        rp = new RecommendPosts(null);
        post1 = new PlanePost("molly_tester", "post 1 text", "PVD", "GVL",
            new Date(2001,1,1), new Date(2010, 1,1),
            null, null);
        post2 = new CarPost("molly_tester", "post 1 text", "PVD", "GVL",
            new Date(2001,1,1), new Date(2010, 1,1));
  }

  /** quick test of isWithinDateRange helper! (probably not even necessary) */
  @Test
  public void testDateRangeChecker() {
    Date newYears = new Date(01, 01, 2000);
    Date xmas = new Date(12, 25, 2000);
    Date halloween = new Date(10, 31, 2000);

    // within range
    Assertions.assertTrue(rp.isWithinDateRange(halloween, newYears, xmas));

    // before range
    Assertions.assertFalse(rp.isWithinDateRange(newYears, xmas, halloween));

    // after range
    Assertions.assertFalse(rp.isWithinDateRange(newYears, xmas, halloween));

    // same date passed in for all
    Assertions.assertTrue(rp.isWithinDateRange(halloween, halloween, halloween));
  }

  /** Tests when same post passed in twice, should generate "perfect" rec score */
  @Test
  public void testScoreIdenticalPost() {
    Assertions.assertTrue(rp.makeRecScore(post1, post1).equals(190));
  }

  /** Tests when almost identical post passed in, except for travel type */
  @Test
  public void testIdenticalPostAlbeitType() {
    Assertions.assertTrue(rp.makeRecScore(post1, post2).equals(190));
  }

  /** Tests when an empty post is passed into the post1 field */
  @Test
  public void testScoreEmptyPost1() throws IOException {
    // TODO
  }

  /** Tests when an empty post is passed into the post2 field */
  @Test
  public void testScoreEmptyPost2() throws IOException {
    // TODO
  }

  /** Tests that when origin matches the ORIGIN_MATCH_SCORE constant is added to the score */
  @Test
  public void testOriginMatchScore() throws IOException {
    // TODO
  }

  /** Tests that when destination matches the DEST_MATCH_SCORE constant is added to the score */
  @Test
  public void testDestMatchScore() throws IOException {
    // TODO
  }

  /** Tests that when toFlight matches the FLIGHT_MATCH_SCORE constant is added to the score */
  @Test
  public void testFlightMatchScore_To() throws IOException {
    // TODO
  }

  /** Tests that when fromFlight matches the FLIGHT_MATCH_SCORE constant is added to the score */
  @Test
  public void testFlightMatchScore_From() throws IOException {
    // TODO
  }

  /**
   * Tests that when toTrainLine matches the TRAINLINE_MATCH_SCORE constant is added to the score
   */
  @Test
  public void testTrainLineMatchScore_To() throws IOException {
    // TODO
  }

  /**
   * Tests that when fromTrainLine matches the TRAINLINE_MATCH_SCORE constant is added to the score
   */
  @Test
  public void testTrainLineMatchScore_From() throws IOException {
    // TODO
  }

  /** Tests that when both posts are CAR type, BOTH_CAR_SCORE constant is added to the score */
  @Test
  public void testBothCarScore() throws IOException {
    // TODO
  }

  /** Tests when zero or negative number passed into generatePostRecs second argument */
  @Test
  public void testGenerateInvalidNumPostRecs() {
    // TODO
  }

  /**
   * Tests if error is thrown if an invalid type is passed to the filterMap fields when some
   * arguments are strings (cannot be parsed into Doubles)
   */
  @Test
  public void testRecommendationPrelim() throws Exception {
    // create RecommendPosts objects that uses mocks!
    List<List<Object>> recs1 = rp.generatePostRecs(post1, 10);
    System.out.println("recs1: " + recs1);

    assertEquals(0, 0);
  }

  /**
   * Tests when number of recommended posts recommended is greater than the size of the post
   * database being traversed
   */
  @Test
  public void testNumRecsGreaterThanDB() {
    // TODO
  }
}
