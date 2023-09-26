package edu.brown.cs32.API.server.PostHandling;

import edu.brown.cs32.API.posts.PlanePost;
import edu.brown.cs32.API.posts.Post;
import edu.brown.cs32.API.posts.TrainPost;
import java.util.Map;

public class ResponseUtilities {

  /**
   * Creating a success hashmap to be serialized Cite - asetty1
   *
   * @param post - the post created
   * @return returns a hashmap that will be serialized and is the response of the API
   */
  public static Map<String, Object> createSuccessMap(Post post) {
    Map<String, Object> responseMap = post.generateMap();
    //    responseMap.put("result", "success");
    if (post instanceof PlanePost) {
      responseMap.put("toFlights", ((PlanePost) post).toFlights.toString());
      responseMap.put("returnFlights", ((PlanePost) post).returnFlights.toString());
    } else if (post instanceof TrainPost) {
      responseMap.put("linesToDest", ((TrainPost) post).linesToDest.toString());
      responseMap.put("linesFromDest", ((TrainPost) post).linesFromDest.toString());
    }
    return responseMap;
  }
}
