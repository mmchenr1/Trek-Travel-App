package edu.brown.cs32.API.server.UserHandling;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs32.API.database.Firebase;
import edu.brown.cs32.API.users.User;
import java.util.Map;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler that takes a Google OAuth UID and check to see if that user already exists in the DB. If
 * the user object doesn't exist, creates a new user using the UID + the username as the gmail
 * username used to log in (the characters located before the @ symbol in the email address).
 *
 * <p>http://localhost:3232/userinitialization?userID=molly_mchenry&&name=Molly
 */
public class UserLoginHandler implements Route {
  String TEXT_invalid_username_creation_failure = "error_illegal_username_characters";
  String TEXT_username_taken = "error_username_taken";

  @Override
  public Object handle(Request request, Response response) {
    try {
      QueryParamsMap qm = request.queryMap();
      String userUID = qm.value("userID");
      String name = qm.value("name");
      try {
        if (Firebase.getUser(userUID) == null) {
          Firebase.setUser(new User(userUID, name));
        } else {
          return new UserLoginFailureResponse(TEXT_username_taken).serialize();
        }
      } catch (IllegalArgumentException e) {
        return new UserLoginFailureResponse(TEXT_invalid_username_creation_failure).serialize();
      }
      return new UserLoginSuccessResponse().serialize();
    } catch (Exception e) {
      return new UserLoginFailureResponse("error_userID_and_name_fields_required");
    }
  }

  public record UserLoginSuccessResponse(String response, Map<String, String> responseMap) {
    public UserLoginSuccessResponse() {
      this("success_login", null);
    }

    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<UserLoginSuccessResponse> adapter =
            moshi.adapter(UserLoginHandler.UserLoginSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /** Response object to send if someone requested soup before any recipes were loaded */
  public record UserLoginFailureResponse(String response) {
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(UserLoginHandler.UserLoginFailureResponse.class).toJson(this);
    }
  }
}
