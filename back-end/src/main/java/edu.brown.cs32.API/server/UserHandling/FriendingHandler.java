package edu.brown.cs32.API.server.UserHandling;

import com.squareup.moshi.Moshi;
import edu.brown.cs32.API.database.Firebase;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * endpoint for adding a friend to a user account!
 *
 * http://localhost:3232/newfriend?userID=molly_mchenry&&friendID=phoebe_bridgers
 */
public class FriendingHandler implements Route {
  String textUIDNotFound = "error_user/friend_id_not_found";
  String textCannotFriendYourself = "error_cannot_friend_yourself";

  public FriendingHandler() {}

  @Override
  public Object handle(Request request, Response response) throws Exception {
    // get the existing QueryParam values
    QueryParamsMap qm = request.queryMap();
    String userID = qm.value("userID");
    String friendID = qm.value("friendID");
    if (userID == null || friendID == null) {
      return new FriendFailureResponse(textUIDNotFound).serialize();
    }
    else if (userID.equals(friendID)) {
      return new FriendFailureResponse(textCannotFriendYourself).serialize();
    }
    Firebase.setFriend(userID, friendID);
    return new FriendSuccessResponse().serialize();
  }

  public record FriendSuccessResponse(String response, Map<String, String> friending) {
    public FriendSuccessResponse() {
      this("success_friending", null);
    }

    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(FriendingHandler.FriendSuccessResponse.class).toJson(this);
    }
  }

  public record FriendFailureResponse(String response) {
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(FriendingHandler.FriendFailureResponse.class).toJson(this);
    }
  }
}
