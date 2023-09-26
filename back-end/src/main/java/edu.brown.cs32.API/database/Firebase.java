package edu.brown.cs32.API.database;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.PolymorphicJsonAdapterFactory;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;
import edu.brown.cs32.API.posts.CarPost;
import edu.brown.cs32.API.posts.PlanePost;
import edu.brown.cs32.API.posts.Post;
import edu.brown.cs32.API.posts.TrainPost;
import edu.brown.cs32.API.users.User;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import javax.servlet.ServletOutputStream;


public class Firebase {
  private static Boolean EMULATOR;
  private static String token;
  private static String url;

  public static User getUser(String uid) {
    // Construct the URL for the user object
    String fetchUserURL = url + "users/" + uid + ".json?access_token=" + token;
    // Create a new HTTP request using the URL
    try {
      HttpRequest request = HttpRequest.newBuilder().uri(new URI(fetchUserURL)).GET().build();
      // Execute the request and retrieve the response
      HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        // Parse the JSON response into a User object
        User deserializedUser = deserializeUser(response);
//        deserializedUser.friends.remove(null);
        return deserializedUser;
      } else {
        // error getting response
        System.out.println("error getting response from getUser");
        return null;
      }
    } catch (URISyntaxException | IOException | InterruptedException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Post getPost(String postid) {
    // Construct the URL for the user object
    String fetchUserURL = url + "posts/" + postid + ".json?access_token=" + token;

    // Create a new HTTP request using the URL
    try {
      HttpRequest request = HttpRequest.newBuilder().uri(new URI(fetchUserURL)).GET().build();
      // Execute the request and retrieve the response
      HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());

      if (response.statusCode() == 200) {
        // Parse the JSON response into a User object
        return deserializePost(response);
      } else {
        // Response errored
        System.out.println("response errored in getPost");
        return null;
      }
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("some catch statement?");
    return null;
  }
  // Function to check if username exists
  public static HashMap<String, Post> getPostsforUser(String uid) {
    // Find the user
    User user = getUser(uid);
    if (user == null) {
      return null;
    }
    if (user.postIDS == null) {
      return null;
    } else {
      HashMap<String, Post> posts_from_user = new HashMap<>();
      // Iterating through each postID, we can find the corresponding posts from the posts db
      for (String pid : user.postIDS) {
        posts_from_user.put(pid, getPost(pid));
      }
      return posts_from_user;
    }
  }

  public static Set<String> getFriends(String uid) {
    // Find the user
    User user = getUser(uid);
    if (user == null) {
      return null;
    }
    System.out.println("getFriends: " + user.friends);
    return user.friends;
  }

  public static ArrayList<List<Object>> getFriendsPosts(String uid) {
    // Find the user's friends
    Set<String> userFriendIDs = getFriends(uid);
    ArrayList<List<Object>> friend_posts = new ArrayList<>();
    // For each friend, get all their posts
    for (String fuid : userFriendIDs) {
      System.out.println("getting " + fuid + " posts");
      HashMap<String, Post> friendPosts = getPostsforUser(fuid);
      if(friendPosts != null) { //make sure friend has posts
        for (String postID : friendPosts.keySet()) {
          Post post = friendPosts.get(postID);
          List<Object> postPair = new ArrayList<>();
          postPair.add(fuid);
          postPair.add(post);
          friend_posts.add(postPair);
        }
      }
    }
    return friend_posts;
  }

  public static void setPost(Post post) throws Exception {
    User user = getUser(post.userID);
    if (user == null) {
      throw new Exception("User not found");
    }
    user.addPost(post.postID); // Update User object
    setUser(user); // Insert it back

    // Insert actual post into posts table
    String sendURL = url + "posts/" + post.postID + ".json?access_token=" + token;
    HttpRequest sendJSON =
        HttpRequest.newBuilder()
            .uri(URI.create(sendURL))
            .PUT(HttpRequest.BodyPublishers.ofString(post.serialize()))
            .build();

    HttpClient client = HttpClient.newHttpClient();

    client
        .sendAsync(sendJSON, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .join();
  }

  /**
   * setUser checks if the uid provided already exists in which case
   *
   * @param user
   */
  public static void setUser(User user) {
    String sendURL = url + "users/" + user.username + ".json?access_token=" + token;
    HttpRequest sendJSON =
        HttpRequest.newBuilder()
            .uri(URI.create(sendURL))
            .PUT(HttpRequest.BodyPublishers.ofString(user.serialize()))
            .build();

    HttpClient client = HttpClient.newHttpClient();

    client
        .sendAsync(sendJSON, HttpResponse.BodyHandlers.ofString())
        .thenApply(HttpResponse::body)
        .join();
  }

  public static void setFriend(String uid, String friendid) throws Exception {
    // Get user
    User user = getUser(uid);
    if (user == null) {
      throw new Exception("User not found!");
    }
    user.addFriend(friendid);
    // Get friend
    User friend = getUser(friendid);
    if (friend == null) {
      throw new Exception("Friend not found!");
    }
    friend.addFriend(uid);
    // Update the database
    setUser(user);
    setUser(friend);
  }

  public static Post deserializePost(HttpResponse<String> response) throws IOException {
    Moshi moshi =
        new Moshi.Builder()
            .add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
            .add(PolymorphicJsonAdapterFactory.of(Post.class, "type")
                .withSubtype(PlanePost.class, "PLANE")
                .withSubtype(CarPost.class, "CAR")
                .withSubtype(TrainPost.class, "TRAIN"))
            .build();
    return moshi.adapter(Post.class).fromJson(response.body());
  }


  public static User deserializeUser(HttpResponse<String> response) throws IOException {
    // exceptions handled above
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(User.class).fromJson(response.body());
  }

  public static void deleteUser(String uid) {
      String sendURL = url + "users/" + uid + ".json?access_token=" + token;
      HttpRequest sendJSON =
              HttpRequest.newBuilder()
                      .uri(URI.create(sendURL))
                      .DELETE()
                      .build();

      HttpClient client = HttpClient.newHttpClient();

      client
              .sendAsync(sendJSON, HttpResponse.BodyHandlers.ofString())
              .thenApply(HttpResponse::body)
              .thenAccept(System.out::println)
              .join();
  }

  public static void deleteAllPostsforUser(String uid) {
    User user = getUser(uid);
    for (String pid : user.postIDS) {
      // This code repetition is essential because it short circuits and doesn't
      // make multiple setPost calls. This is essential to cut down the number of calls
      // being made to Firebase. We can simply update the user at the end. At the same
      // time, the deletePost function is also needed in case a user wants to simply
      // delete a single user. In this case, the database must fully reflect this change
      // by changing the user object accordingly.
      String sendURL = url + "posts/" + pid + ".json?access_token=" + token;
      HttpRequest sendJSON =
              HttpRequest.newBuilder()
                      .uri(URI.create(sendURL))
                      .DELETE()
                      .build();

      HttpClient client = HttpClient.newHttpClient();

      client
              .sendAsync(sendJSON, HttpResponse.BodyHandlers.ofString())
              .thenApply(HttpResponse::body)
              .thenAccept(System.out::println)
              .join();
    }
    // Now we only make one call to setUser
    user.postIDS = null;
    setUser(user);
  }

  public static void deletePost(String pid) {
    Post post = getPost(pid);
    // Update User to have one less post
    User user = getUser(post.userID);
    user.postIDS.remove(pid);
    setUser(user);
    // Delete Post
    String sendURL = url + "posts/" + pid + ".json?access_token=" + token;
    HttpRequest sendJSON =
            HttpRequest.newBuilder()
                    .uri(URI.create(sendURL))
                    .DELETE()
                    .build();

    HttpClient client = HttpClient.newHttpClient();

    client
            .sendAsync(sendJSON, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
  }

  /**
   * deletes all data in our Firebase DB
   * not implemented on the front-end, just for us:)
   */
  public static void deleteDatabase() {
    // Delete DB
    String sendURL = url + ".json?access_token=" + token;
    HttpRequest sendJSON =
            HttpRequest.newBuilder()
                    .uri(URI.create(sendURL))
                    .DELETE()
                    .build();

    HttpClient client = HttpClient.newHttpClient();

    client
            .sendAsync(sendJSON, HttpResponse.BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(System.out::println)
            .join();
  }

  /**
   * Initializes Firebase when the Server starts running. Here, private_key.json is a JSON file
   * containing the private key. This is included in the gitignore so that it isn't accidentally
   * posted to Github allowing anyone to access the database and manipulate it.
   */
  public static void FirebaseInitializer(Boolean emulator) {
    try {
      FileInputStream serviceAccount = new FileInputStream("private_key.json");
      // check out env variables
      // Authenticate a Google credential with the service account
      GoogleCredential googleCred = GoogleCredential.fromStream(serviceAccount);

      // Add the required scopes to the Google credential
      GoogleCredential scoped =
          googleCred.createScoped(
              Arrays.asList(
                  "https://www.googleapis.com/auth/firebase.database",
                  "https://www.googleapis.com/auth/userinfo.email"));

      // Use the Google credential to generate an access token
      scoped.refreshToken();
      token = scoped.getAccessToken();
      if (emulator) {
        url = "http://localhost:9000/travel-e591e/default-rtdb";
      } else {
        url = "https://travel-e591e-default-rtdb.firebaseio.com/";
      }
      System.out.println("Firebase Initialized");
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.out.println("Private Key not found");
    } catch (IOException e) {
      e.printStackTrace();
      System.out.println(
          "Error initializing firebase credentials."
              + "Please check that you have the correct private key and accesses");
    }
  }
}