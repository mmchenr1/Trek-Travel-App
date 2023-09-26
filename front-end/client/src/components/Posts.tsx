import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import IData from "../utils/IPostData";
import { userFamilyName, userGivenName } from "./auth/Login";

//http://localhost:3232/postrecs?userID=molly_mchenry&&numRecs=2

const Posts = () => {
  const [numRecs, setNumRecs] = useState(5); //to update number of generated recs when scrolling
  const apiCallRecs = 'http://localhost:3232/postrecs?userID=' +  userGivenName.toLowerCase() + '_' + userFamilyName.toLowerCase() + '&&numRecs=' + numRecs
  const [postRecs, setPostRecs] = useState([]);
  const [isPending, setPendingStatus] = useState(true);
  const [noFriendsStatus, setNoFriendsStatus] = useState(false);
  const [errorStatus, setErrorStatus] = useState(false);

  //function to recall api server to get more recommendations when requested (button click)
  function loadMorePosts() {
    setNumRecs(numRecs + 5);
    fetchRecs();
  }

  //fetches recommendations for however many recs are needed! (recalled each time the load more button is clicked)
  function fetchRecs() {
    console.log("api call: " + apiCallRecs)
  
    fetch(apiCallRecs)
    .then(response => 
    response.json())
    .then(json =>{
        if(json.response === "success"){
          console.log("success getting post recs")
          console.log("posts recs: " + json.post_recommendations)
          let recsConst = json.post_recommendations
          var recsList = recsConst.map(mapHelper)
          setPostRecs(recsList) //TRY!!!
          setPendingStatus(false)
          } else if (json.response === "error_no_friends") {
            setNoFriendsStatus(true)
            setPendingStatus(false)
          }
          else {
            console.log(json.response)
            setErrorStatus(true)
            setPendingStatus(false)
          }
      })
  
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
      returnList.push(post.username)

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
  }

  //fetch first 5 recs when panel is initially loaded
  useEffect(() => {fetchRecs();}, []);


const handleSubmit = (e: { preventDefault: () => void; }) => {
  e.preventDefault();
}
  
function PostRecsPanel() {
  if (noFriendsStatus){
    return <div className="post-panel" >
      Oops... looks like you don't have any friended users yet! Navigate to the Friends panel to add friends! <br></br>
      <Link to="/Friends">~~~To Friends!~~~~~</Link>
    </div> 
  }

  else if (errorStatus){
    return <div className="post-panel" >
      Error Loading Posts
    </div> 
  }

  else if (!isPending){ //TODO -- is there a way to only render elements necessary for the post depending on the type/fields actually filled out?

    return  <div className="post-panel" >
      
      <>{postRecs ? postRecs.map((post: Array<string>) => (
      // --> array
       <div className="post" key={post[0]} > 
           <div className="post-header"> <strong>@{post[10]}</strong></div>

            {/* //format: ORIGIN -> LOCATION */}
            <div className="post-locations">
              <strong >{post[2]} &nbsp;&nbsp; {post[11]} &nbsp; &nbsp; {post[1]} </strong>
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
      <button className="load-more-button" aria-label="load more" onClick={loadMorePosts}>Load more</button>
      </div> 
  } else {
    return <div className="post-panel" >
      Loading Your Friends' Posts...
    </div> 
  }

}

return (
  <div className="posts-page">
    
    <div className="interpanel-header">
    <h2 className="page-header">Friends' Posts</h2>
    </div>

    <br></br>
    <hr color= "#8035f1" ></hr>
    <br></br>
    
    
    <PostRecsPanel></PostRecsPanel>

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
 
export default Posts;