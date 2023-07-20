package edu.brown.cs32.API.database;

import edu.brown.cs32.API.posts.CarPost;
import edu.brown.cs32.API.posts.PlanePost;
import edu.brown.cs32.API.posts.TrainPost;
import edu.brown.cs32.API.users.User;
import org.junit.jupiter.api.*;

import java.util.*;

import static edu.brown.cs32.API.server.PostHandling.PostHandler.convertStringToDate;

public class TestFirebase {

    /**
     * Before each test, clears the contents of the csvContents and initializing commands and handlers
     */
    @BeforeEach
    public void setup() {
        // Using an emulator instead of actual DB
        Firebase.FirebaseInitializer(true);
        // Clean up the emulator
        Firebase.deleteDatabase();
    }

    /** Tears down the API server by unmapping the commands and stopping the server */
    @AfterEach
    public void teardown() {
        // Clean up emulator
        Firebase.deleteDatabase();
    }

    /** basic test of setUser and getUser. GetUser is the only way to test if a user is present in the DB */
    @Test
    public void testSetUserBasic() {
        Firebase.setUser(new User("001", "Tim Nelson"));
        Assertions.assertNotNull(Firebase.getUser("001"));
    }

    /** You can customize a user and set it into the DB **/
    @Test
    public void testSetUserCustom() {
        User tim = new User("001", "Tim Nelson");
        tim.addFriend("002");
        tim.addPost("205");
        tim.addPost("206");
        tim.addPost("207");
        Firebase.setUser(tim);
        User timDB = Firebase.getUser("001");
        HashSet<String> timsFriends = new HashSet<>();
        timsFriends.add("002");
        Assertions.assertEquals(tim.friends, timsFriends);
        Assertions.assertEquals(tim.friends, timDB.friends);
        HashSet<String> timsPosts = new HashSet<>();
        timsPosts.addAll(List.of("205", "206", "207"));
        Assertions.assertEquals(tim.postIDS, timsPosts);
        Assertions.assertEquals(tim.postIDS, timDB.postIDS);
    }

    @Test
    public void testDeleteUserBasic() {
        Firebase.setUser(new User("001", "Tim Nelson"));
        Firebase.deleteUser("001");
        Assertions.assertNull(Firebase.getUser("001"));
    }

    /** This is a randomized fuzz test for setting and deleting users **/
    @Test
    public void testRandomDeleteUserBasic() {
        Random rd = new Random(); // creating Random object
        int numTries = rd.nextInt(5000);
        for(int i = 0; i < numTries; i++) {
            Random uid = new Random();
            int uidNum = rd.nextInt();
            byte[] username = new byte[1000];
            uid.nextBytes(username);
            Firebase.setUser(new User(Integer.toString(uidNum), new String(username)));
        }
        for(int i = 0; i < numTries; i++) {
            Random uid = new Random();
            int uidNum = rd.nextInt();
            byte[] username = new byte[1000];
            Firebase.deleteUser(Integer.toString(uidNum));
        }
    }

    @Test
    public void testDeleteNoUser() {
        Firebase.deleteUser("001");
        Assertions.assertNull(Firebase.getUser("001"));
    }

    /** basic test of setUser and getUser. GetUser is the only way to test if a user is present in the DB */
    @Test
    public void testSetPlanePostBasic() throws Exception {
        Firebase.setUser(new User("001", "Tim Nelson"));
        new PlanePost("001", "", "BOS", "LAX",
                // Duration of trip
                convertStringToDate("12/03/2022"),
                convertStringToDate("12/18/2022"),
                // Flight details not decided yet
                new ArrayList(), new ArrayList());
        User tim = Firebase.getUser("001");
        for(String pid : tim.postIDS) {
            Assertions.assertNotNull(Firebase.getPost(pid));
        }
    }

    /** car post test **/
    @Test
    public void testSetCarPostBasic() throws Exception {
        Firebase.setUser(new User("001", "Tim Nelson"));
        new CarPost("001", "", "BOS", "LAX",
                // Duration of trip
                convertStringToDate("12/03/2022"),
                convertStringToDate("12/18/2022"));
        User tim = Firebase.getUser("001");
        for(String pid : tim.postIDS) {
            Assertions.assertNotNull(Firebase.getPost(pid));
        }
    }

    /** basic train post test **/
    @Test
    public void testSetTrainPostBasic() throws Exception {
        Firebase.setUser(new User("001", "Tim Nelson"));
        new TrainPost("001", "", "BOS", "LAX",
                // Duration of trip
                convertStringToDate("12/03/2022"),
                convertStringToDate("12/18/2022"),
                // Train details not decided yet
                new ArrayList(), new ArrayList());
        User tim = Firebase.getUser("001");
        for(String pid : tim.postIDS) {
            Assertions.assertNotNull(Firebase.getPost(pid));
        }
    }
    @Test
    public void testDeletePostBasic() throws Exception {
        Firebase.setUser(new User("001", "Tim Nelson"));
        new TrainPost("001", "", "BOS", "LAX",
                // Duration of trip
                convertStringToDate("12/03/2022"),
                convertStringToDate("12/18/2022"),
                // Train details not decided yet
                new ArrayList(), new ArrayList());
        User tim = Firebase.getUser("001");
        for(String pid : tim.postIDS) {
            // all posts exist
            Assertions.assertNotNull(Firebase.getPost(pid));
        }
        Firebase.deletePost((String) tim.postIDS.toArray()[0]);
        User timDeleted = Firebase.getUser("001");
        Assertions.assertNull(timDeleted.postIDS);
    }

    /** You can't set a post without the user being set up previously **/
    @Test
    public void testSetPostWithoutUser() throws Exception {
        Assertions.assertThrows(Exception.class,
                () -> new PlanePost("1011", "", "BOS", "LAX",
                // Duration of trip
                convertStringToDate("12/03/2022"),
                convertStringToDate("12/18/2022"),
                // Flight details not decided yet
                new ArrayList(), new ArrayList()));
    }

    /** you can set Friends once a user is already in the DB too. This is the functionality our app uses
     * since all users are initialised and set into the DB without friends or posts. Then, calls to the
     * friending handler can create friends. **/
    @Test
    public void testSetFriendsDuplicate() throws Exception {
        Firebase.setUser(new User("001", "Tim Nelson"));
        Firebase.setUser(new User("002", "Nim Telson"));
        Firebase.setFriend("001", "002");
        Firebase.setFriend("002", "001");
        User tim = Firebase.getUser("001");
        User nim = Firebase.getUser("002");
        Assertions.assertEquals(tim.friends, Set.of("002"));
        Assertions.assertEquals(nim.friends, Set.of("001"));
    }

    /** Exception thrown when the friend isn't in the DB **/
    @Test
    public void testSetFriendsWithoutFriend() throws Exception {
        Firebase.setUser(new User("001", "Nim Telson"));
        Assertions.assertThrows(Exception.class,
                () -> Firebase.setFriend("022", "302"));
    }

    /** If they are already friends, nothing happens. No exception thrown since the
     * request to friend is technically complete. In the database itself, we use
     * Sets to represent friend and post IDs. Thus, nothing is updated.
     * @throws Exception
     */
    @Test
    public void testSetFriends() throws Exception {
        Firebase.setUser(new User("001", "Tim Nelson"));
        Firebase.setUser(new User("002", "Nim Telson"));
        Firebase.setFriend("001", "002");
        User tim = Firebase.getUser("001");
        User nim = Firebase.getUser("002");
        Assertions.assertEquals(tim.friends, Set.of("002"));
        Assertions.assertEquals(nim.friends, Set.of("001"));
    }


    // Delete DB
    // Delete all posts for user -> removes from user too, removes each post
    // getFriendsPosts -> all users are friends, none are friends,
    // setFriends -> all are friends, none are firends,some here some there
    // getuser -> user doesn't exist
    // getpost -> post doesn't exist
}
