package edu.brown.cs32.API.server.UserHandling;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs32.API.database.Firebase;
import java.util.List;
import java.util.Set;

import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * handler to get all friends' UIDs for a given user
 *
 * http://localhost:3232/getfriends?userID=molly_mchenry
 */
public class GetUserFriendsHandler implements Route {

  @Override
  public Object handle(Request request, Response response) {
    try {
      QueryParamsMap qm = request.queryMap();
      Set<String> friends = Firebase.getFriends(qm.value("userID"));
      if (friends == null) {
        return new GetUserFriendsFailureResponse("error_no_friends").serialize();
      }
      System.out.print("FRIENDS: " + friends.toString());
      return new GetUserFriendsSuccessResponse(friends.stream().toList()).serialize();
    } catch (Error e){
      return new GetUserFriendsFailureResponse("error").serialize();
    }
  }

  public record GetUserFriendsSuccessResponse(String response, List<String> friends) {
    public GetUserFriendsSuccessResponse(List<String> friends) {
      this("success", friends);
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<GetUserFriendsHandler.GetUserFriendsSuccessResponse> adapter =
            moshi.adapter(GetUserFriendsHandler.GetUserFriendsSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /** Response object to send if someone requested soup before any recipes were loaded */
  public record GetUserFriendsFailureResponse(String response) {
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(GetUserFriendsHandler.GetUserFriendsFailureResponse.class).toJson(this);
    }
  }
}
