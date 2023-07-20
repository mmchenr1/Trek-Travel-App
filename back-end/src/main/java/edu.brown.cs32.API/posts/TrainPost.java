package edu.brown.cs32.API.posts;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import edu.brown.cs32.API.database.Firebase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * class to hold posts of trips where users are travelling by Train
 *
 * <p>linesToDest -- names of the lines taken from the origin of the trip to the destination
 * linesFromDest -- names of the lines taken from destination to the origin
 */
public class TrainPost extends Post {
  public final List<String> linesToDest;
  public final List<String> linesFromDest;

  public TrainPost(
      String userUID,
      String text,
      String fromLocation,
      String toLocation,
      Date startDate,
      Date endDate,
      ArrayList<String> linesToDest,
      ArrayList<String> linesFromDest)
      throws Exception {
    super(userUID, text, fromLocation, toLocation, startDate, endDate);
    this.linesToDest = linesToDest;
    this.linesFromDest = linesFromDest;
    this.type = "TRAIN";
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
    return moshi.adapter(TrainPost.class).toJson(this);
  }
}
