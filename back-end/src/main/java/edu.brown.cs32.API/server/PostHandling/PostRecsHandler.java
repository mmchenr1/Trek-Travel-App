package edu.brown.cs32.API.server.PostHandling;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs32.API.database.Firebase;
import edu.brown.cs32.API.posts.Post;
import edu.brown.cs32.API.posts.RecommendPosts;
import edu.brown.cs32.API.users.User;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler that gets n number of post recommendations for a given user. Currently, it uses the
 * user's most recent post (measured by the start date in the post) to generate the recommendations
 * using the recommendation algorithm in posts/RecommendPosts. If the user has not yet made any
 * posts, the algorithm returns the n most recent posts in the available database of friend posts.
 *
 * <p>http://localhost:3232/postrecs?userID=molly_mchenry&&numRecs=2
 */
public class PostRecsHandler implements Route {

  /** Constructor accepts some shared state */
  public PostRecsHandler() { // shared state: all the posts we just loaded from the DB
  }

  @Override
  public Object handle(Request request, Response response) {
    try {
      System.out.println("\n");
      System.out.println("REC HANDLER ENTERED!!!");
      QueryParamsMap qm = request.queryMap();
      Integer numRecs = Integer.parseInt(qm.value("numRecs"));
      String userID = qm.value("userID");

      // look for user in DB
      User user = Firebase.getUser(qm.value("userID"));
      if (user == null) {
        return new RecsFailureResponse("error_user_not_in_DB");
      }

      String mostRecentPostID = null;
      Date mostRecentPostDate = null;
      List<List<Object>> recs;
      RecommendPosts postRecommender = new RecommendPosts(user);

      // look for user's most recent post
      HashMap<String, Post> userPosts = Firebase.getPostsforUser(userID);

      //alternate rec algo if no posts for a user yet
      if ((userPosts == null) || userPosts.size() == 0) {
        System.out.println("no user posts yet");
        recs = postRecommender.generatePostRecs(null, numRecs);
      }

      // main part of the algorithm: when user has posts and db is populated
      else {
        System.out.println("user has post(s)");
        for (String postID : user.postIDS) {
          Date pDate = Objects.requireNonNull(Firebase.getPost(postID)).startDate;
          System.out.println("POINT 3: " + Objects.requireNonNull(Firebase.getPost(postID)).text);
          if (mostRecentPostID == null) {
            mostRecentPostID = postID;
            mostRecentPostDate = pDate;
          }
          if (!(pDate.after(mostRecentPostDate))) {
            mostRecentPostID = postID;
            mostRecentPostDate = pDate;
          }
        }

        postRecommender = new RecommendPosts(user);
        recs = postRecommender.generatePostRecs(Firebase.getPost(mostRecentPostID), numRecs);
      }
      return new RecsSuccessResponse(generateSuccessMap(recs)).serialize();
    } catch (Exception e) {
      return new RecsFailureResponse()
          .serialize(); // Ask molly about what should be passed into RecsFailureResponse
    }
  }

  public List<Map> generateSuccessMap(List<List<Object>> recommendedPosts) {
    List<Map> mappedPosts = new ArrayList<>();
    for (List<Object> pPair : recommendedPosts) {
      Post p = (Post) pPair.get(1);
      Map m = p.generateMap();
      m.put("username", pPair.get(0));
      mappedPosts.add(m);
    }
    return mappedPosts;
  }

  public record RecsSuccessResponse(String response, List<Map> post_recommendations) {
    public RecsSuccessResponse(List<Map> post_recommendations) {
      this("success", post_recommendations);
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<RecsSuccessResponse> adapter = moshi.adapter(RecsSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  public record RecsFailureResponse(String response) {
    public RecsFailureResponse() {
      this("error");
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(PostRecsHandler.RecsFailureResponse.class).toJson(this);
    }
  }
}
