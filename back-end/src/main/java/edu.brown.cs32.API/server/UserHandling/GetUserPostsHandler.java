package edu.brown.cs32.API.server.UserHandling;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs32.API.database.Firebase;
import edu.brown.cs32.API.posts.Post;

import java.util.*;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * gets all posts from a user (given the user's UID)
 *
 * http://localhost:3232/personalposts?userID=molly_mchenry
 */
public class GetUserPostsHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    try {
      QueryParamsMap qm = request.queryMap();
      String userID = qm.value("userID");

      // check to see if the user has made a post before
      try{Firebase.getUser(userID).postIDS.size();} catch (Exception e) {
        return new SendUserPostsFailureResponse("error_no_posts_yet").serialize();
      }
      // get user's posts
      Collection<Post> userPosts = Firebase.getPostsforUser(userID).values();

      return new SendUserPostsSuccessResponse("success", createSuccessMap(userPosts)).serialize();
    } catch (Exception e) {
      return new SendUserPostsFailureResponse("error").serialize();
    }
  }

  /**
   * Creating a success hashmap to eventually be serialized
   *
   * @param posts - ArrayList of Post objects
   * @return returns a hashmap that will be serialized and is the response of the API
   */
  public Set<Map<String, Object>> createSuccessMap(Collection<Post> posts) {
    Set<Map<String, Object>> postSet = new HashSet<>();
    for (Post post : posts) {
      postSet.add(post.generateMap());
      System.out.println("POST MAP: " + post.generateMap());
    }
    return postSet;
  }

  public record SendUserPostsSuccessResponse(String response, Set<Map<String, Object>> posts) {
    String serialize() {
      try {
        System.out.println("attempting serialization");
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<GetUserPostsHandler.SendUserPostsSuccessResponse> adapter =
            moshi.adapter(GetUserPostsHandler.SendUserPostsSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /** Response object to send if someone requested soup before any recipes were loaded */
  public record SendUserPostsFailureResponse(String response) {
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(GetUserPostsHandler.SendUserPostsFailureResponse.class).toJson(this);
    }
  }

  /**
   * Response object to send if a unique ID hasn't been provided from Google OAuth through the
   * frontend
   */
  public record SendAuthFailureResponse(String response) {
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(GetUserPostsHandler.SendAuthFailureResponse.class).toJson(this);
    }
  }
}