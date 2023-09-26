import {useEffect, useState } from "react";
import {userGivenName, userFamilyName, userNameConst} from "./auth/Login"
import IData from "../utils/IPostData";
import { Link } from "react-router-dom";

/**
 * The 'Account' page within the navigation bar. Acts as the personal profile page for the user.  
 * UIUX Component:
 *  - Accessibility: Allows the user to access their previous posts
 * Algorithmic Component: 
 *  - Works in conjunction with the 'Create' page and the backend to fetch the created posts of a user
 *    from the backend
 *  - If there the user hasn't created any posts, a message will be displayed encouraging to click on the
 *    linked 'Create' page so that they can view posts on the 'Account' page
 * @returns The 'Account' page in the web application 
 */
const Account = () => {
  let urlFirstNameLower = userGivenName.toLowerCase()
  let urlLastNameLower = userFamilyName.toLowerCase()
  const [data, setData] = useState([]);
  const [isPending, setPendingStatus] = useState(true);
  const [noPostStatus, setNoPostStatus] = useState(false);

  
  useEffect(() => {

  // sample api call: http://localhost:3232/personalposts?userID=sandra_martinez
  /* api call to get the posts of the user from the backend */
  const apiCall = 'http://localhost:3232/personalposts?userID=' + urlFirstNameLower + '_' + urlLastNameLower
  console.log(apiCall)

  /*map helper to turn posts json output into 2d array */
  function mapHelper(post: IData): Array<Object | undefined> {
    let returnList: (Object | undefined)[] = [];
    returnList.push(post.text)
    returnList.push(post.toLocation)
    returnList.push(post.fromLocation)
    returnList.push(post.startDate)
    returnList.push(post.endDate)
    returnList.push(post.type) 
    returnList.push(post.toFlights)
    returnList.push(post.returnFlights)
    returnList.push(post.linesToDest)
    returnList.push(post.linesFromDest)
     //determine travel mode emoji
     switch(post.type){
      case "PLANE":
        returnList.push("✈️");
        break;
      case "CAR":
        returnList.push("🚘");
        break;
      case "TRAIN":
        returnList.push("🚆");
        break;
    }
    return returnList
  }

  /*  fetching the posts of the user from the backend - works with success/failure responses */
  fetch(apiCall)
  .then(response => 
  response.json())
  .then(json =>{
      if(json.response === "success"){
        let postConst = json.posts
        var postList = postConst.map(mapHelper)
        setData(postList) 
        setPendingStatus(false)
      } else if (json.response === "error_no_posts_yet") {
        setNoPostStatus(true)
      }
      else {
        console.log(json.response)
      }
    })
  }, []);

  /**
   * Takes the posts from the mapHelper and displays them on the screen. Displays a loading
   * message will posts are being retrived from backend 
   * @returns the post panel of the 'Account' page
   */
  function PostPanel() {
   
    if (noPostStatus){
      return <div className="post-panel" aria-label="Post Panel" >
         No posts yet! Navigate to the Create panel to make your first post! <br></br>
        <Link to="/Create">~~~To Create!~~~~~</Link>
      </div> 
    }
    else if (!isPending){ //TODO -- is there a way to only render elements necessary for the post depending on the type/fields actually filled out?
      return  <div className="post-panel" aria-label="Post Panel" >
        
        <>{data ? data.map((post: Array<string>) => (
        // --> array
        <div className="post" key={post[0]} > 
            <div className="post-header"> <strong>@{urlFirstNameLower}_{urlLastNameLower}</strong></div>

            {/* //format: ORIGIN -> LOCATION */}
            <div className="post-locations">
              <strong >{post[2]} &nbsp;&nbsp; {post[10]} &nbsp; &nbsp; {post[1]} </strong>
            </div>

            {/* //format: start - end */}
            <div className="post-dates">
              { post[3].replace(" 00:00:00 ", "").replace("EDT", "").replace("EST", "") } - 
              { post[4].replace(" 00:00:00 ", "").replace("EDT", "").replace("EST", "") } 
            </div>

            <br/>
            <text className="post-description">{ post[0] }</text>
            <br/>
            <br/>

            <AdditionalInfo post6={post[6]} post7={post[7]} post8={post[8]} post9={post[9]}></AdditionalInfo>

            {/* <button onClick={handleClick}>delete</button> */}
        </div>
        )): null }
        </>
        </div> 
    } else {
      return <div className="post-panel" aria-label="Post Panel">
        Loading Your Posts...
      </div> 
    }

  }

  return (
    <div className="account-page">
      <h2 className="page-header">Account</h2>
      <div className="account-header" color="grey">
        {/* <span className="dot" aria-label="Profile Picture"> </span> */}
        <img className="profile-picture" aria-label="Profile Picture" src="../public/user_icon.png"></img>
        <div className="user-info-box">
        <div className="name"> {userGivenName} {userFamilyName}</div>
        <br></br>
        <div className="username">{urlFirstNameLower}_{urlLastNameLower} </div>
        </div>
      </div>

      <br></br>
      <hr color= "#8035f1" ></hr>
      <br></br>
      
      <PostPanel></PostPanel>

    </div>
  );
}

interface AdditionalInfoProps {
  post6: string|undefined;
  post7: string|undefined;
  post8: string|undefined;
  post9: string|undefined;
}

function AdditionalInfo(props: AdditionalInfoProps) {
  var display6 = false;
  var display7 = false;
  var display8 = false;
  var display9 = false;

  console.log("HI:" + props.post6)
  console.log(props.post6 == undefined)

  if(props.post6 !== undefined){
    display6 = true;
  }
  if(props.post7 !== undefined){
    display7 = true;
  }
  if(props.post8 !== undefined){
    display8 = true;
  }
  if(props.post9 !== undefined){
    display9 = true;
  }

    return(
    <div className="post-fields">
      <strong style={{display: display6?"default":"none"}}>To Flight:  </strong>  {props.post6}
      <br style={{display: display6?"default":"none"}}></br>
      <strong style={{display: display7?"default":"none"}}>Return Flights:  </strong>  {props.post7}
      <br style={{display: display7?"default":"none"}}></br>
      <strong style={{display: display8?"default":"none"}}>Lines To Destination:  </strong>  {props.post8}
      <br style={{display: display8?"default":"none"}}></br>
      <strong style={{display: display9?"default":"none"}}>Lines From Destination:  </strong>  {props.post9} 
    </div>
  )
}

export default Account;