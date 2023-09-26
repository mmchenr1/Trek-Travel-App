# <strong>Welcome to Trek!</strong>
Developers: Molly McHenry (mmchenr1), Mithi Jethwa (mjethwa1), and Sandra Martinez (smarti86) <br>
*Link to Repo: https://github.com/cs0320-f2022/term-project-mjethwa1-mmchenr1-smarti86* 

## How to Use Trek
<strong>Getting Started:</strong><br>
Login to Trek using any Google Account. If you don't already have a Trek account, don't worry, an account will automatically be created with the Google information used to log in.
<br>  
<strong>Making a Post:</strong><br> 
Now that you've logged in, it's time to start posting! Navigate to the ***Post*** panel to create a post. Simply fill out the post fields with the information about your travel plans and hit Submit. Now your post has been created!
 <br>  
<strong>Viewing Your Posts:</strong><br> 
To view all of your posts, navigate to the ***Account*** panel. Here you can see all of your posts of both past and upcoming trips. Additionally, your profile information is viewable here, which matches your Google Account information.
 <br>  
<strong>Friending:</strong><br> 
Use Trek to keep up with your friends' travel plans by friending them! On the ***Friends*** panel, search up friends and send a friend request. Once another user has sent you a friend request, you can accept or reject it on the ***Friends*** page. Once you accept this request, you will be able to see each other's posts on the ***Posts*** page.
 <br>  
<strong>Viewing Friends' Posts:</strong><br> 
The ***Posts*** page features a custom order of recommended posts from accounts you have friended. For more information on this recommendation algorithm, see the description of the algorithm [here](#recalgo).

Citation: https://www.youtube.com/watch?v=Ka3OQpwqxXA for idea and explanation behind the logic of loading more posts
 <br>  
<strong>Editing Your Account</strong> <br>
Edit Your Account Information in the ***Settings*** Panel. Here you can change your profile picture, account name, personal information, and delete your account. 
 <br>  
 <strong>Deleting a Post</strong><br> 
Deleting a post is mocked and the functionality is supported in the backend; however, not in the front-end
 <br>  
<strong>Logging Out:</strong><br> 
Log out at any time by pressing the Logout Button at the top right of your screen.
 <br>  
<strong>Deleting Your Account</strong> <br> 
As of right now, account deletion has not been implemented to be possible through the User Interface of Trek. If you want to delete your Trek account, email one of the developers and we'll be happy to remove your account. <br>

----

## Accessibility<br>
Created an 'About Us' page to let the user know how all about what Trek is about, how they can use it, and what our privacy policy is. We also added aria-labels to the major components of the app, such as the navigation bar and post creation page to guide the users who rely on the Voice Over feature. In our Page Not Found custom pages, we also provide links back to the web application so that they can easily go back to the webapp without having to npm start or type in the url again. 

---

## Division of Labor:
Molly worked mainly on the backend object classes, creating the recommendation algorithm, setting up the API server, and creating all of the API handlers to communicate with the front end. Molly also helped establish the fetch get and post connections between the front end and back end. She also handled post rendering and friend account rendering on the front end. Molly also helped with styling the Account, Friends, and Post pages.

Sandy worked mainly on the frontend with React and Typescript to mock the web application, fetch the data, and add the style and static components. Sandy also handled Google Authentication 2.0 and ensuring that the private information of the user is only accessible when they've logged in. 


Mithi worked with Firebase integration, creating a NoSQL database, integrating it with a Java backend and creating functions for complex queries needed on the backend. She also handled Friending between users by creating the Friending Handler. 

---

## Design choices <br>

<div href=firebase> <strong> Firebase Database </strong>
We chose to use Firebase's Realtime Database for our data storage needs. This is a NoSQL DB which is useful in cases like ours where different kinds of Posts may have different fields and values associated with them. Further, using a NoSQL DB makes it easier to port code when adding new functionalities and tracking new features across classes. We integrate the Firebase DB with the Java backend and create functions to create, read, update and delete various components of this database. These functions, written in Java, provide an accessible way for the Java backend to interact with Firebase. For our purposes, we chose to use Firebase's REST API documentation. We also create functions for more complex queries such as getting posts from all friends of a particular user. 
<br></br>
</div>

<div href=recalgo> <strong> Recommendation Algorithm </strong> 
Our recommendation algorithm displays the posts of a user's friends in a custom order to that user. This is achieved by recursing through the database (comprised of all the friend posts) and generating a score for each post in comparison to the user's most recent post (in terms of start date). Scores are affected by if the dates of posts overlap, if the origin and/or destinations match, if the mode of travel is the same, and if the same flights or train lines are being taken. If a user has not made any posts yet, the algorithm sorts all of the posts in their friend database by dates, giving preference to ongoing trips, then upcoming trips (ordered by closest start date), and finally past trips (ordered by proximity to the current date). <br></br> </div> 

<div><strong>Frontend</strong>: 
TODO Sandy
</div>

---

## Future Improvements
1. improving our testing to have a good foundation tobuild on
2. edit posts
3. delete account
4. functional account settings page
5. searching and adding friends
6. accepting/declining friend requests
7. view friend's personal account pages
8. improved efficiency of post recommendation generation
9. account creation page (instead of automatically creating an account upon first login)
10. pages for each post --> adding post IDs
11. create messaging component to reach out to friends within the webapp
12. Improving the create posts form to handle a greater range of inputs (to improve uiux)

---

## Classes:
<strong>Backend</strong>: <br>
back-end/src/../database - This folder contains the databae infrastructure of the project
- Definitions.md -  This file shows the structure of our database as it currently stands. It also defines terms like uid and pid that are used in Firebase.java
- Firebase.java -  This class contains functions for accessing the database (such as getUser, setUser, deleteUser, etc) and for more complex queries (such as deleteAllPostsforUser, getFriendsPosts, etc) to create, read, update and delete (CRUD) the Firebase Realtime Database <br>

back-end/src/../posts - This folder contains the infrastructure for representing posts in the database and the recommendation algorithm for recommending posts.
- Post.java - abstract class that holds the basic post information necessary for all types of posts
- PlanePost.java - extends Post.java with toFlights and fromFlights field to hold flight information
- PlainPost.java - extends Post.java with linesToDest and linesFromDest field to hold train line information
- CarPost.java - extends Post.java but doesn't add any new fields
- RecommendPost.java - Recommendation algorithm that returns most relevant posts to a user from the posts from they're friends' accounts. Achieves this by calculating a similarity score for each post in a user's database of friend posts by comparing each post to the user's most recent post (determined by start date of the trip in that post). The main method <em>generatePostRecs</em> takes in a userID and number of posts to be generated and returns the recommended posts from that user's friends in the order of recommendation level.

back-end/src/../server - This folder contains the server and all the API handlers used to transfer data between the frontend and backend of the web application.
- Server.java - the server responsible for seding db information between the frontend and the backend, located at Spark Port 3232.
- UserLoginHandler.java - Handler for the endpoint "userinitialization" that checks if a user is in the database and creates a new user with the given uid if it doesn't already exist
- FriendingHandler.java - Handler for the endpoint "newfriend" that takes in two user uids and established a friend connection between their objects in the databse.
- PostDeletionHandler.java - Not currently implemented for consumption by users. Deletes a post in the database given the post uid and user uid.
- PostHandler.java - Handler for the endpoint "newpost" that takes in a user uid and all the fields of the post that needs to be created. It then creates the post for the user and adds it to the database.
- GetUserFriendsHandler.java - Handler for the endpoint "getfriends" that returns a JSON with a list of friend usernames for a given user uid.
- GetUserPostsHandler.java - Handler for the endpoint "personalposts" that returns all of a user's posts in JSON mapped format.
- PostRecsHandler.java - Handler for the enpoint "postrecs" that generates n number of post recommendations for a given user by calling on ../posts/RecommendPost.java 
- ClearDBHander -- This handler is not made for user consumption, but rather to make it easier for developers to reset the database when testing how the site works.

back-end/src/../users/User.java - A class used to represent a user in the back-end. This class stores the user uid, name, HashMap of postIDs, and the uids of the user's friended users.
<br>

<strong>Frontend</strong>: 
front-end/client/src/assets - this folder that stores the images used through the web application

front-end/client/src/components - this folder houses the main components of the web application;
- ...src/components/auth folder contains that functionality for Google Authentication 2.0. This includes a clientID.ts file that holds Sandy's clientID for the Google API and services. Since this is a private key, it's in its own file so that I can be added to the .gitignore. Then there's the Login.tsx file, where Google Authenticaion 2.0 is actually implemented. We choose to only show certain navigation bar components when a user is signed in as a way of defensive programming. 
- ...src/components/failpages - this folder contains our informative 404 pages. The CreateFail.tsx file informs the user that something has gone wrong with their post creation and offers to take them back to the Create page. The PageNotFound.tsx file renders our custom 404 page when an improper route is entered and offers to take them back to the Homepage. 
- AboutUs.tsx - Page that lets the user know what our web application Trek is about. We also share more about our team and our privacy policy to inform the user of how we use their data!
- Account.tsx - Page where the user can look at the posts that they have made. Account.tsx receives information from PostHandler.java to not only display, but to also load more posts for the user to view.
- Create.tsx - Page where the user can create their own posts. Receives information from the user via repls and sends that information to the backend and then Firebase to store and then display on the Account page of the user
- Friends.tsx - Page where the user can see their friends, search for friends, and manage their network. The fetch connection is in place to retrive the friends of the user; however, the search and sidebar features of the page are all static components and would be our next steps if we continued to work on the project
- FriendsSideBar.tsx - holds the mocked sidebar for the Friends page
- Home.tsx - Homepage that welcomes the user to our web application!
- Navbar.tsx - Holds the Settings, Account, Create, Friends, Posts, and About Us pages, as well as the login/logout buttons
- Posts.tsx - Features the post recommendation algorithm, where posts made by a users friend are recommended to the user. 
- Settings.tsc - Page where the user would be able to change the settings of their account. All elements on this page are static and making the functional would be in our next steps

front-end/client/src/graveyard - folder that houses all the files that we didn't end up using. We spent alot of time trying different methods to get the webapp where it is and didn't want to throw all the work away :))

front-end/client/src/tests - hold our feeble testing attempts. We were working very close to the deadline trying to make out webapp functional and gave testing our best shot with the time that we had. Tests would definetly be our immediate next step if we has more time to work on the project to ensure that our current foundation is working properly before we add more on. 

front-end/client/src/utils - folder that holds IPostData.tsx, which stores the types of the fields on the Create page

App.tsx - File that gets rendered for display. It houses the routes for smaller components of the App, such as the navigation bar and the content when each page is shown.

index.css - the styling for the webapplication. We did little styling in html, so index.css is really where the magic happens


<br>
<br>

---

## Errors/Bugs
No Known Errors or Bugs came up in testing
<br>

----

## Testing
### Unit Testing:<br>
To run these tests, run ```firebase emulators:start``` in the root directory of the project. If you want to stop the emulator, you can press ```Ctrl + C``` to kill the program.

This file contains unit tests for the Firebase.java class and the functions in it. We cover setting and getting User and different kinds of Posts when a user has and hasn't been previously set. We look at setting friends and adding posts to pre-existing User objects and the outcome when the User object doesn't already exist. Finally, we look at deleting users and posts to check if the DB is adequately updated after calls to delete.
The TestPostHandler file has unit tests on post creation showing how one can create Train, Car and Plane posts and even posts that do not specify a particular journey type. It tests basic error cases such as not passing in the required parameters, having a start date after the end date, etc. These tests mirror the functionality in the Firebase functions.

### Backend Intermediate/Integration Tests:<br>
The HandlerTestUtilities file contains helpers for other handler test files. It contains 2 functions - tryRequestConnection and tryRequestBody. The first one sets up the connection for the a specific endpoint on the URL used for testing (we use port 111 for testing purposes). The second creates a request to an external API and returns the response body. This response body is undefined for failed requests and may return null values in such a case. Injection testing is managed through these utilities and setup/teardown within the testing suites for the API handlers, located in the server folder within tests.

### Random Testing - Fuzz Testing Boundaries<br>
For random testing, we try multiple setUser and delete User calls. This models how we can carry out random testing for the rest of the testing suite.
It was hard to randomly test the server because we would want to cover cases with each endpoint and pass in none, some or all parameters. However, a random test that has awareness of these criterions would need to loop through multiple arrays or objects to ensure the random testing is of some value to the testing suite quality. Thus, the most appropriate place to randomly test was where we had functions that took in fewer parameters.


