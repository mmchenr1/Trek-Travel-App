package edu.brown.cs32.API.posts;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import edu.brown.cs32.API.database.Firebase;

import java.util.Date;

public class CarPost extends Post {
  public CarPost(
      String userUID,
      String text,
      String fromLocation,
      String toLocation,
      Date startDate,
      Date endDate)
      throws Exception {
    super(userUID, text, fromLocation, toLocation, startDate, endDate);

    System.out.println("firebase about to be called");
    Firebase.setPost(this);
    System.out.println("firebase called in above line");
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
    return moshi.adapter(CarPost.class).toJson(this);
  }
}
