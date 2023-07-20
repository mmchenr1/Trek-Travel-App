package edu.brown.cs32.API.server.PostHandling;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import java.util.HashMap;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler that deletes a post in the database
 *
 * <p>http://localhost:3232/deletepost?userID=xxx&&postID=xxx
 */
public class PostDeletionHandler implements Route {
  /** text objects to change here! */
  String textEmptyPostFields = "error_required_fields_left_empty";

  String textFirebaseDeletionError = "error_Firebase";

  public PostDeletionHandler() {}

  /** Delete the post from the db and in the given user object */
  @Override
  public Object handle(Request request, Response response) throws Exception {
    try {
      // get the existing QueryParam values
      QueryParamsMap qm = request.queryMap();
      String userID = qm.value("userID");
      String postID = qm.value("postID");

      //      Firebase.deletePost(userID, postID); //TODO Mithi -- implement post deletion!

      return new PostDeletionSuccessResponse().serialize();
    } catch (NullPointerException e) {
      e.printStackTrace();
      return new PostDeletionFailureResponse(textEmptyPostFields).serialize();
    } catch (Exception e) {
      e.printStackTrace();
      return new PostDeletionFailureResponse(textFirebaseDeletionError).serialize();
    }
  }

  /**
   * creates output with appropriate error message in case of post failure
   *
   * @param result -- specific error message
   * @return hashmap with error as result (key)
   */
  public Map<String, String> createFailureMap(String result) {
    Map<String, String> responseMap = new HashMap<>();
    responseMap.put("result", result);
    return responseMap;
  }

  public record PostDeletionSuccessResponse(String response) {
    public PostDeletionSuccessResponse() {
      this("success");
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<PostDeletionSuccessResponse> adapter =
            moshi.adapter(PostDeletionSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /** Response object to send if someone requested soup before any recipes were loaded */
  public record PostDeletionFailureResponse(String response) {
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(PostDeletionHandler.PostDeletionFailureResponse.class).toJson(this);
    }
  }
}
