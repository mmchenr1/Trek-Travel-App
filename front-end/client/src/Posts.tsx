import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { userFamilyName, userGivenName } from "./components/auth/Login";
import IData from "./utils/IPostData";
// import { userFamilyName, userGivenName } from "./Login";

//http://localhost:3232/postrecs?userID=molly_mchenry&&numRecs=2

const Posts = () => {
  const [numRecs, setnumRecs] = useState(5); //to update number of generated recs when scrolling
  const apiCallRecs = 'http://localhost:3232/postrecs?userID=' +  userGivenName.toLowerCase() + '_' + userFamilyName.toLowerCase() + '&&numRecs=' + numRecs
  const [postRecs, setPostRecs] = useState([]);
  const [isPending, setPendingStatus] = useState(true);
  const [noFriendsStatus, setNoFriendsStatus] = useState(false);
  const [errorStatus, setErrorStatus] = useState(false);

  //function to recall api server to get more recommendations when requested (button click)
  function loadMorePosts() {
    setnumRecs(numRecs + 5);
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
      return returnList
    }
  }

  //fetch first 5 recs when panel is initially loaded
  useEffect(() => {fetchRecs();}, []);


// const RenderPostField = (label: string, value: string | Array<string>) => {
//   if(!({value} === '') && !({value}.length == 0)){
//     return <text><strong>{label}:  </strong> {value}</text>
//   } else { 
//     return <a></a>
//   }
// }


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
           <div className="post-header" style={{color:"white", marginBottom: 10}}> <strong>@{post[10]}</strong></div>
           <text className="post-description">{ post[0] }</text>
           <div className="post-fields">

           <strong>Traveling to:  </strong> { post[1]} <br></br>
           <strong>Traveling from:  </strong> { post[2]} <br></br>
           <strong>Start Date:  </strong> { post[3].replace(" 00:00:00 ", "").replace("EDT", "").replace("EST", "") } <br></br>
           <strong>End Date:  </strong>  { post[4].replace(" 00:00:00 ", "").replace("EDT", "").replace("EST", "") } <br></br>
           <strong>Type of Travel:  </strong> {post[5]} <br></br>
           <strong>To Flight:  </strong>  {post[6]} <br></br>
           <strong>Return Flights:  </strong>  {post[7]} <br></br>
           <strong>Lines To Destination:  </strong>  {post[8]} <br></br>
           <strong>Lines From Destination:  </strong>  {post[9]} 
           {/* <button onClick={handleClick}>delete</button> */}
       </div>
       </div>
      )): null }
      </>
      <button className="load-more-button" aria-label="load more" onClick={loadMorePosts}>Load more</button>
      </div> 
  } else {
    return <div className="post-panel" >
      Loading Friends' Posts...
    </div> 
  }

}

return (
  <div className="posts-page">
    
    <div className="interpanel-header">
      FRIENDS' POSTS
    </div>

    <br></br>
    <hr color= "#8035f1" ></hr>
    <br></br>
    
    
    <PostRecsPanel></PostRecsPanel>

  </div>
  
);

}
 
export default Posts;