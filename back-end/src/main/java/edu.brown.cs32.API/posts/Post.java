package edu.brown.cs32.API.posts;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * a class for each post on the platform, created by a user and stored in that user's posts database
 */
public abstract class Post {
  public String postID;
  public String type;
  public String userID;
  public String text;
  public String fromLocation;
  public String toLocation;
  public Date startDate;
  public Date endDate;

  // Make sure everything is public
  /**
   * constructor for a post -- with all fields being manually input
   *
   * @param text -- the text description accompanying the post //TODO -- consider capping the number
   *     of characters allowed in this field (would need to be done in the front-end REPL)
   * @param fromLocation -- start location/origin of the trip
   * @param toLocation -- destination of the trip
   * @param startDate -- Date the trip starts
   * @param endDate -- Date the user returns
   */
  public Post(
      String UID, String text, String fromLocation, String toLocation, Date startDate, Date endDate)
      throws IOException {

    // require certain fields: toLocation and startDate
    if (startDate == null || toLocation == null) {
      throw new IOException("error: posts require a destination and start date");
    }

    // check that endDate is after startDate
    if (endDate != null && startDate.after(endDate)) {
      throw new IllegalArgumentException("error: endDate must be ~after~ startDate");
    }

    this.postID = UUID.randomUUID().toString();

    this.userID = UID;
    this.text = text;
    this.fromLocation = fromLocation;
    this.toLocation = toLocation;
    this.startDate = startDate;
    this.endDate = endDate;

    this.type = "CAR";
  }

  /** generates map of the fields in a post --> to be used in success responses for API handlers */
  public Map<String, Object> generateMap() {
    HashMap<String, Object> map = new HashMap<>();
    String startDateString = "";
    String endDateString = "";
    if (startDate != null) {
      startDateString = startDate.toString();
    }
    if (endDate != null) {
      endDateString = endDate.toString();
    }
    map.put("text", text);
    map.put("fromLocation", fromLocation);
    map.put("toLocation", toLocation);
    map.put("startDate", startDateString);
    map.put("endDate", endDateString);
    map.put("type", "CAR");

    if (this instanceof PlanePost) {
      map.put("type", "PLANE");
      try{map.put("returnFlights", ((PlanePost) this).returnFlights);}  catch (Exception ignored){}
      try{map.put("toFlights", ((PlanePost) this).toFlights);} catch (Exception ignored){}
    }

    if (this instanceof TrainPost) {
      map.put("type", "TRAIN");
      try {map.put("linesToDest", ((TrainPost) this).linesToDest);} catch (Exception ignored){}
      try{map.put("linesFromDest", ((TrainPost) this).linesFromDest);} catch (Exception ignored){}
    }
    return map;
  }

  public abstract String serialize();
}