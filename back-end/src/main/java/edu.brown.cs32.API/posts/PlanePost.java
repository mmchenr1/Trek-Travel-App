package edu.brown.cs32.API.posts;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import edu.brown.cs32.API.database.Firebase;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class to hold posts of trips where users are travelling by Plane
 *
 * <p>toFlights -- flight numbers of the flights taken from the origin of the trip to the
 * destination returnFlights -- flight numbers of flights taken when returning from destination to
 * the origin
 */
public class PlanePost extends Post implements Serializable {
  public final List<String> toFlights;
  public final List<String> returnFlights;

  /**
   * constructor
   *
   * @param toFlightsArray -- ArrayList of flight numbers of all flights taken to the toLocation
   * @param returnFlightsArray -- ArrayList of flight numbers taken when returning from the
   *     toLocation
   */
  public PlanePost(
      String userUID,
      String text,
      String fromLocation,
      String toLocation,
      Date startDate,
      Date endDate,
      ArrayList<String> toFlightsArray,
      ArrayList<String> returnFlightsArray)
      throws Exception {
    super(userUID, text, fromLocation, toLocation, startDate, endDate);
    this.toFlights = toFlightsArray;
    this.returnFlights = returnFlightsArray;
    this.type = "PLANE";

    // Setting post into db
    Firebase.setPost(this);
  }

  @Override
  public String serialize() {
    Moshi moshi = new Moshi.Builder()
        .add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
        .add(
            // Expect something that's a Post...
            PolymorphicJsonAdapterFactory.of(Post.class, "type")
                // ...with three possibilities for concrete shapes, disambiguated by type:
                .withSubtype(PlanePost.class, "plane")
                .withSubtype(CarPost.class, "car")
                .withSubtype(TrainPost.class, "train"))
        .build();
    return moshi.adapter(PlanePost.class).toJson(this);
  }
}