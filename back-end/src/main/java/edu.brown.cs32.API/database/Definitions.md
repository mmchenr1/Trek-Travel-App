## FireBase Terms and Definitions Contract
The Firebase DB we are using is structured as follows:
|
| Users ---- "1" ---->  User Object Molly:
|                       username = "1" // This is simply the Unique User ID provided by Google OAuth
|                       name = Molly
|                       postIDs = ["a", "n", "g"] // List of post IDs by the user
|                       friends = ["3"]  // List of friends denoted by their user IDs
|       ---- "2" ---->  User Object Sandra:
|                       username = "2"
|                       name = Sandra
|                       postIDs = ["b"]
|                       friends = ["3"]
|       ---- "3" ----> User Object Mithi:
|                       username = "3"
|                       name = Mithi
|                       postIDs = ["c", "f"]
|                       friends = ["1", "2"]
|
| Posts ---- "a" ---->  Post Object
|                       postID = "a"
|                       // other Post Object fields based on what Post we use (Car, Train or Plane)
#### UID and PID
UID and PID are simply unique identifiers to associate User/Post objects with. Thus, we always search by these IDs in the database.

