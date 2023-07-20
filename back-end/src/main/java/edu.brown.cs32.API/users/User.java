package edu.brown.cs32.API.users;

import com.squareup.moshi.Moshi;

import java.util.HashSet;
import java.util.Set;

/**
 * class to represent a User Account on the platform
 *
 * <p>This class stores all the user information that is displayed on their profile page including:
 * - username - name of the user - count of the number of trips that have been taken (based on post
 * data) - count of the number of upcoming trips (based on post data) - all the posts of the user -
 * the friend network of the user
 */

// TODO -- consider making a defensive wrapper of a User object (think defensive programming from
// CSVParser in Sprint 2)

public class User {
  public final String username; // Unique ID from Google OAuth
  public final String name; // The actual name of the user
  //  public Integer numTripsTaken;
  //  public Integer numUpcomingTrips;
  public Set<String> postIDS;
  public Set<String> friends;

  /**
   * constructor for User Account: makes new account (how does this relate to Google OAuth??)
   *
   * @param username -- String of the username
   */
  public User(String username, String name) {
    String allowedUsernameCharacters = "[a-z_.0-9]"; // allows lowercase alphabet, _, and .
    if (!username.matches(allowedUsernameCharacters)) {
      this.username = username; // TODO do we need a way to make sure that the username isn't already taken? -->
      // this is an issue with looking up in the DB (Mithi)
    } else {
      throw new IllegalArgumentException("illegal characters passed in with username: " + username);
    }
    System.out.println("User being initialised");
    this.name = name;
    this.postIDS = new HashSet<>();
    this.friends = new HashSet<>();
    // Setting the user in the Database
//    Firebase.setUser(this);
  }

  /**
   * adds a Friend (another User object) to the user's friend network, keyed by the friend's
   * username with the value being the location of the correspoding User object in the heap.
   *
   * @param friendID -- the username of the friend to be added to this user's network
   */
  public void addFriend(String friendID) {
    if (this.friends == null) {
      // Had no friends, now has 1
      this.friends = Set.of(friendID);
      System.out.println("user friends (constructor): " + this.friends);
    } else {
      this.friends.add(friendID);
    }
    // always succeeds if a User has been constructed.
  }

  /**
   * adds a Friend (another User object) to the user's friend network, keyed by the friend's
   * username with the value being the location of the correspoding User object in the heap.
   *
   * @param postID -- the unique ID of the post to be added to this user's list of posts
   */
  public void addPost(String postID) {
    if (this.postIDS == null) {
      // Had no posts, now has 1
      Set<String> postSet = new HashSet<>();
      postSet.add(postID);
      this.postIDS = postSet;
    } else {
      this.postIDS.add(postID);
    }
    // always succeeds if a User has been constructed.
  }

  public String serialize() {
    Moshi moshi = new Moshi.Builder().build();
    return moshi.adapter(User.class).toJson(this);
  }
}
