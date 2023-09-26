package edu.brown.cs32.API.posts;

import edu.brown.cs32.API.database.Firebase;
import edu.brown.cs32.API.users.User;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Post Recommendation Algorithm. Takes a user and recommends which of their friends' posts should
 * be shown to them and in what order. Based on the user's own posts.
 */
public class RecommendPosts {
  User user;

  Integer OVERLAP_DATES_SCORE = 10;
  Integer ORIGIN_MATCH_SCORE = 100;
  Integer DEST_MATCH_SCORE = 80;
  Integer FLIGHT_MATCH_SCORE = 150;
  Integer TRAINLINE_MATCH_SCORE = 80;
  Integer BOTH_CAR_SCORE = 100;

  /**
   * primary constructor -- incorporates mocks to be used for testing, and isolated back-end
   * performance
   *
   * @param user -- the User who wants the posts recommended to them (out of their friends' posts)
   */
  public RecommendPosts(User user) {
    this.user = user;
  }

  /**
   * checks if a given date is within the range given
   *
   * @param date -- Date object of the date being checked
   * @param startDate -- Date corresponding to the start of the range
   * @param endDate -- Date corresponding to the cutoff of the range
   * @return true if the date is in the range
   */
  public boolean isWithinDateRange(Date date, Date startDate, Date endDate) {
    if(endDate == null){
      return !(date.before(startDate));
    } else {
      return (!(date.before(startDate)) && !(date.after(endDate)));
    }
  }

  /**
   * makes rec score for how similar post2 is to post1 (the score is -for- post 2 in relation to
   * post 1) scores boosted by: 1. origin location + start date (veto factor) --> if trips don't
   * overlap 2. destination location proximity between posts (only if overlaps in the timeframes) 3.
   * if flight numbers match, factor that in!!!**
   *
   * @param post1 -- the Post for being compared to
   * @param post2 -- the Post for which the score is being calculated
   * @return the score of how similar the posts are
   */
  public Integer makeRecScore(Post post1, Post post2) {
    System.out.println("\n");
    System.out.println("making score for: " + post1.text + " & " + post2.text);
    int score = 0;

    Date post1EndDate = null;
    try {
      post1EndDate = post1.endDate;
    } catch (Exception e) {
      e.printStackTrace();
    }

    //recommendation algorithm only kicks in when the trips overlap!
    if (isWithinDateRange(post2.startDate, post1.startDate, post1EndDate)) {
      System.out.println("within date range!");
      score = score + OVERLAP_DATES_SCORE;

      // if same origin location--> if extra time add check for proximity so that origins don't
      // have to be exactly the same to accrue points
      try {
        if (post2.fromLocation.equals(post1.fromLocation)) {
          System.out.println("from Location match!");
          score = score + ORIGIN_MATCH_SCORE;
        }
      } catch (Exception ignored) {
        //just means that one of the posts doesn't have a fromLocation
      }

      // if same destination (within window)
      if (post2.toLocation.equals(post1.toLocation)) {
        System.out.println("to Location match!");
        score = score + DEST_MATCH_SCORE;
      }

      if (post1 instanceof PlanePost && post2 instanceof PlanePost) {
        System.out.println("both PLANE posts!");
        // if posts list the same flight numbers!
        try {
          for (String f : ((PlanePost) post1).toFlights) {
            if (((PlanePost) post2).toFlights.contains(f)) {
              System.out.println("matched toFLights!");
              score = score + FLIGHT_MATCH_SCORE;
            }
          }
        } catch (Exception ignored) {
        }
      }

      try {
        for (String f : ((PlanePost) post1).returnFlights) {
          if (((PlanePost) post2).returnFlights.contains(f)) {
            System.out.println("matched return Flights!");
            score = score + FLIGHT_MATCH_SCORE;
          }
        }
      } catch (Exception ignored) {
      }

      if (post1 instanceof TrainPost && post2 instanceof TrainPost) {
        System.out.println("both TRAIN posts!");
        // if posts list the same flight numbers!
        try {
          for (String t : ((TrainPost) post1).linesFromDest) {
            if (((TrainPost) post2).linesFromDest.contains(t)) {
              System.out.println("matched linesFromDest!");

              score = score + TRAINLINE_MATCH_SCORE;
            }
          }
        } catch (Exception ignored) {
        }

        try {
          for (String t : ((TrainPost) post1).linesToDest) {
            if (((TrainPost) post2).linesToDest.contains(t)) {
              System.out.println("matched linesToDest!");
              score = score + TRAINLINE_MATCH_SCORE;
            }
          }
        } catch (Exception ignored) {
        }
      }

      if (post1 instanceof CarPost && post2 instanceof CarPost) {
        score = score + BOTH_CAR_SCORE;
        System.out.println("both CAR posts!");
      }
    }
    System.out.println("*******SCORE: " + score + "*******");
    return score;
  }

  /**
   * Helper method to handle the case that a null argument is passed in as post in generatePostRecs.
   * This method looks at the posts in the Database and sorts them so that posts that are most
   * imminent or currently happening are at the front of the returned list while posts that have
   * already occurred are at the back. The sorting occurs in two different avenues: the
   * current/future posts are sorted from closest to most far out start dates, while the past posts
   * are sorted with the most recently completed posts being at the front and the older posts being
   * at the back.
   *
   * @param postsDB -- The database of posts to be sorted
   * @return List<Post> of sorted Post objects by date.
   */
  public static List<List<Object>> defaultPostSorter(List<List<Object>> postsDB) {
    String instantNowString = Instant.now().atZone(ZoneId.of("America/New_York")).toString();
    Integer nowYear = Integer.parseInt(instantNowString.substring(0, 4));
    Integer nowMonth = Integer.parseInt(instantNowString.substring(5, 7));
    Integer nowDay = Integer.parseInt(instantNowString.substring(8, 10));
    Date now = new Date(nowYear - 1900, nowMonth - 1, nowDay);

    System.out.println("\ndefaultPostSorter entered!!!!");

    // sort posts into past and future/preset
    List<List<Object>> pastPosts = new ArrayList<>();
    List<List<Object>> futurePosts = new ArrayList<>(); // includes present posts (on same day as now
    int i = 0;
    System.out.println(postsDB.toString());
    for (List<Object> pPair : postsDB) {
      Post p = (Post) pPair.get(1);
      i = i + 1;
      System.out.println("\n");
      System.out.println("iteration #" + i + "---------------------------------------------");
      System.out.println(">>>>>" + p.text);
      System.out.println("end date: " + p.endDate);
      System.out.println("start date: " + p.startDate);
      System.out.println("now date: " + now);

      // if endDate is null, mark as ongoing trip --> sort by who left first to end
      if (p.endDate == null) {
        futurePosts.add(pPair);
      } else {
        if (p.endDate.before(now)) {
          pastPosts.add(pPair);
        } else {
          futurePosts.add(pPair);
        }
      }
    }
    System.out.println("pastPosts:  " + pastPosts);

    // sort each list in order of proximity to the current date
    Collections.sort(pastPosts, (o1, o2) -> {
      Post p1 = (Post) o1.get(1);
      Post p2 = (Post) o2.get(1);
      Date d1 = p1.endDate;
      Date d2 = p2.endDate; // TODO i don't think the posts are being pulled out correctly
       if (d1.after(d2)) {
         return -1;
       } else {
         return 1;
       }
    });
    System.out.println("sorted pastPosts:  " + pastPosts);

    System.out.println("futurePosts:  " + pastPosts);

    futurePosts.sort(
        (o1, o2) -> {
          Post p1 = (Post) o1.get(1);
          Post p2 = (Post) o2.get(1);
          Date d1 = p1.startDate;
          Date d2 = p2.startDate; // TODO i don't think the posts are being pulled out correctly
          if (d1.after(d2)) {
            return 1;
          } else {
            return -1;
          }
        });

    System.out.println("sorted futurePosts:  " + pastPosts.size());

    // combine with future/present being first part, appending past posts
    futurePosts.addAll(pastPosts);
    return futurePosts;
  }

  /**
   * Takes in a post and generates which posts in the database have the highest recommendation
   * scores based on the information in the given post.
   *
   * @param post -- the Post for which the recommendations are generated
   * @param numRecs -- the number of Posts to be recommended in the final ArrayList
   * @return an 2D List of the top 10 posts (in order) recommended, formatted as smaller pairs of
   *  the user who made the post at index 0 and the post object itself at index 1
   */
  public List<List<Object>> generatePostRecs(Post post, Integer numRecs) throws Exception {
    System.out.println("entered post rec generators");
    List<List<Object>> orderedRecs = new ArrayList<>();
    ArrayList<Integer> postsScores = new ArrayList<>(); //scores of posts in orderedRecs in order

    // if num of recommended posts to be generated is 0 or negative, just return the empty list
    if (!(numRecs > 0)) {
      return Collections.unmodifiableList(orderedRecs);
    }


    // look at fields of the post that was passed in to generate recommendations from the post db
    ArrayList<List<Object>> postsDB = Firebase.getFriendsPosts(this.user.username);
    System.out.println("got all friends' posts!!");

    // if user has no previous posts, default sort the friend database posts
    if (post == null) {
      return defaultPostSorter(postsDB);
    }

    // generates score for each post in the DB
    for (List<Object> pPair : postsDB) {
      Post p = (Post) pPair.get(1);
      Integer score = makeRecScore(post, p);
      // maintain ordered array of post recs
      if (orderedRecs.isEmpty()) {
        orderedRecs.add(pPair);
        postsScores.add(score);
      } else {
        System.out.println("insertion sort algo entered");

        for (int i = 0; i < postsScores.size(); i++) { //TODO -- bug if duplicate scores
          Integer s = postsScores.get(i);
          System.out.println(">>>>LOOP ITERATION I: " + i);
          if (score > s) {
            System.out.println("insert before!");
            postsScores.add(i, score);
            System.out.println("POST SCORES: " + postsScores);
            orderedRecs.add(i, pPair);
            System.out.println("inserted at: " + i);
            // if list is already at capacity, remove the least relevant post from the end
            if (postsScores.size() > numRecs) {
              System.out.println("max scored case exceeded");
              postsScores.remove(postsScores.size() - 1); // remove last element
              orderedRecs.remove(orderedRecs.size() - 1);
            }
            break;
          }

          System.out.println("line 382: score not greater than s --> not added before");

          // look into continuously generating posts --> diff API calls (functionality over
          // efficiency)
          if ((postsScores.size() < numRecs) && ((i + 1) == postsScores.size())) {
            postsScores.add(score);
            orderedRecs.add(pPair);
            System.out.println("ADDED AT END");
//            i = i + 1;
            break;
          } else if (postsScores.size() < numRecs){
            postsScores.add(score);
            orderedRecs.add(pPair);
            System.out.println("ADDED BEFORE DUPLICATE SCORE");
//            i = i + 1;
            break;
          }
          i = i + 1;
          System.out.println("after adding....");
          System.out.println("postsScores size: " + postsScores.size());
          System.out.println("numRecs: " + numRecs);
          System.out.println("postsScores: " + postsScores);
          System.out.println("postsScores index of next: " + i);
        }
      }
      System.out.println("\n");
      System.out.println("postsScores size: " + postsScores.size());
      System.out.println("numRecs: " + numRecs);
      System.out.println("----------------------------------------------------------------------");

    }
    System.out.println("REC SCORES: " + postsScores);
    return Collections.unmodifiableList(orderedRecs);
  }
}
