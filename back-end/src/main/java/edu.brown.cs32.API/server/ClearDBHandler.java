package edu.brown.cs32.API.server;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import edu.brown.cs32.API.database.Firebase;
import spark.Request;
import spark.Response;
import spark.Route;

/**
 * Handler that deletes all data in the database
 *
 * http://localhost:3232/clearDB
 */
public class ClearDBHandler implements Route {

  /** constructor */
  public ClearDBHandler() {}

  /** Delete the post from the db and in the given user object */
  @Override
  public Object handle(Request request, Response response) {
    try {
      Firebase.deleteDatabase();
      return new ClearDBSuccessResponse("success_DB_cleared").serialize();
    } catch (Exception e) {
      return new ClearDBFailureResponse("error_clearing_DB").serialize();
    }
  }

  /** success response */
  public record ClearDBSuccessResponse(String response) {
    public ClearDBSuccessResponse() { this("success");
    }
    String serialize() {
      try {
        Moshi moshi = new Moshi.Builder().build();
        JsonAdapter<ClearDBSuccessResponse> adapter =
            moshi.adapter(ClearDBSuccessResponse.class);
        return adapter.toJson(this);
      } catch (Exception e) {
        e.printStackTrace();
        throw e;
      }
    }
  }

  /** failure response */
  public record ClearDBFailureResponse(String response) {
    String serialize() {
      Moshi moshi = new Moshi.Builder().build();
      return moshi.adapter(ClearDBHandler.ClearDBFailureResponse.class).toJson(this);
    }
  }
}

