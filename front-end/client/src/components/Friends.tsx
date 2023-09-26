import { useEffect } from "react"
import { useState } from "react"
import { userFamilyName, userGivenName } from "./auth/Login"
import Sidebar from "./FriendsSideBar"

// ex apiCall: http://localhost:3232/getfriends?userID=sandra_martinez
/**
 * The 'Friends' page of the web application. Has static button where the user could look for a certain 
 * username and send them a friend request. Also contained a static side bar where the user could accept
 * and decline friend requests, as well as recommended friends section. Therse are all static elements
 * and adding this functionality would be the next steps of the project. 
 * @returns the 'Friends' page of the web application
 */
const Friends = () => {
  const [friends, setFriends] = useState([])
  const [isPending, setPendingStatus] = useState(false);
  const [noFriendsStatus, setNoFriendsStatus] = useState(false);
  const [errorStatus, setErrorStatus] = useState(false);
  
  useEffect(() => {
    const apiCall = 'http://localhost:3232/getfriends?userID=' + userGivenName.toLowerCase() + '_' + userFamilyName.toLowerCase()
    console.log(apiCall)

    /*  fetching the posts of the user from the backend */
    fetch(apiCall)
    .then(response => 
    response.json())
    .then(json =>{
        if(json.response === "success"){
          let friends = json.friends
          setFriends(friends) 
          setPendingStatus(false)
        }
        else if (json.response == "error_no_friends") {
          setNoFriendsStatus(true);
        }
        else {
          console.log(json.response)
          setErrorStatus(true);
        }
      })
  }, []);
    

  function FriendIcons() {
    if (noFriendsStatus){
      return <div className="friends-panel" style={{alignContent:"flex-left"}} >
         Oh no! Looks like you haven't added any friends yet. <br></br>
      </div> 
    }
    else if(errorStatus){
      return <div className="friends-panel" style={{alignContent:"flex-left"}} >
         Oops! There's been an error loading your friends. <br></br>
      </div> 
    }
      else{
      return  <div className="friends-panel" style={{alignContent:"flex-left"}} >  
      {/* <h2 style={{color:"navy", fontSize:30, paddingTop:20}}> Your Friends: </h2> */}
        <>{friends ? friends.map((friend: String) => (
         <div className="friends" > 
              
              <img className="profile-picture" aria-label="Profile Picture" src="../public/user_icon.png"></img> <br></br>
              <div className="user-info-box-friends">
                <div className="friend-username"> {friend}</div>
              </div>
         </div>
        )): null }
        </>
        </div> 
    } 
  }

  //not yet implemented --> future improvememt!
  const handleSubmit = (e: { preventDefault: () => void }) => {
    e.preventDefault();
    const friend = { friends };
    setPendingStatus(true)
  }

  return (
    <div className="friends-page">
      <h2 className="page-header">Friends</h2>

    <div className="create" aria-label="Friends">
      <h2 className="add-friends" style={{color:"navy", fontSize:30}}>Add Friends: </h2>
      <form className="create-form" onSubmit={handleSubmit}>

        <label>Search by Email:</label>
        <input ></input>
        {/* <input
          required
          value={friends}
          // onChange={(e) => setFriends(e.target.value)} //TODO fix this!!!
          onSubmit={handleSubmit}
          // onChange={(e) => null} //not implemented yet
        > </input> */}
       
        {!isPending && <button>Send Friend Request</button>}
        {isPending && <button disabled>Sending Request</button>}
      </form>
      <br></br>
      <hr></hr>

      {/* display map of friends posts  */}
      <h2 style={{color:"navy", fontSize:30, paddingTop:20}}> Your Friends: </h2>
      <FriendIcons></FriendIcons>
      
    </div>
    {/* <div className="sidebar">
      <Sidebar></Sidebar>
  </div> */}
  </div>
  );
}
 
export default Friends;

