package edu.brown.cs32.API.server;

import static spark.Spark.after;

import edu.brown.cs32.API.database.Firebase;
import edu.brown.cs32.API.server.PostHandling.PostDeletionHandler;
import edu.brown.cs32.API.server.PostHandling.PostHandler;
import edu.brown.cs32.API.server.PostHandling.PostRecsHandler;
import edu.brown.cs32.API.server.UserHandling.FriendingHandler;
import edu.brown.cs32.API.server.UserHandling.GetUserFriendsHandler;
import edu.brown.cs32.API.server.UserHandling.GetUserPostsHandler;
import edu.brown.cs32.API.server.UserHandling.UserLoginHandler;
import spark.Spark;

/**
 * Top-level class for this demo. Contains the main() method which starts Spark and runs the various
 * handlers.
 *
 * <p>We have two endpoints in this demo. They need to share state (a menu). This is a great chance
 * to use dependency injection, as we do here with the menu set. If we needed more endpoints, more
 * functionality classes, etc. we could make sure they all had the same shared state.
 */
public class Server {
  public static void main(String[] args) {
//    System.out.println("initializing...");
    Firebase.FirebaseInitializer(false);

    Spark.port(3232);
    after(
        (request, response) -> {
          response.header("Access-Control-Allow-Origin", "*");
          response.header("Access-Control-Allow-Methods", "*");
        });

    Spark.get("userinitialization", new UserLoginHandler());
    Spark.get("newpost", new PostHandler());
    Spark.get("postrecs", new PostRecsHandler());
    Spark.get("personalposts", new GetUserPostsHandler());
    Spark.get("getfriends", new GetUserFriendsHandler());
    Spark.get("deletepost", new PostDeletionHandler());
    Spark.get("newfriend", new FriendingHandler());
    Spark.get("clearDB", new ClearDBHandler());
    Spark.init();

    try {
      Spark.awaitInitialization();
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("Server started.");
  }
}
