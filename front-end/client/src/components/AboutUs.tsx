/**
 * The 'About Us' page linked in the navigation bar that introduces Trek. There is only static text on this page. 
 * UIUX Component:
 *  - Accessibility: Explain the to user the purpose of the webapp and how to use it
 * @returns The 'About Us' page in the web application 
 */
const AboutUs = () => {
  return (
    <div className="aboutUs" aria-label="About Us">
      <h2 className="page-header"> About Us</h2>
      {/* <h4 style={{color:"navy", fontSize:40,  textAlign:"center", paddingBottom:5}}> <strong>Great Travel with Even Greater Company!</strong></h4> */}
      <p style={{paddingBottom:30}}>
        Trek is a social media app focused on making your travel planning easier so that you're always in great company! You can use Trek as a tool to ...
        <div style={{paddingLeft:"100px", paddingTop:"10px"}}>
        <li style={{paddingTop:"10px"}}>
          Add your friends to view their upcoming travel plans
        </li> 
        <li style={{paddingTop:"10px"}}>
            Create a post of your own detailing where you're headed next
        </li>
        <li style={{paddingTop:"10px"}}>
          View your account page with all of your past and future travel plans
        </li>
        <li style={{paddingTop:"10px"}}>
          View your friends posts in a custom order generated by our recommendation algorithm
        </li>
        </div>
      </p>
      <br></br>
      <h4 style={{color:"navy", fontSize:40,  textAlign:"center", paddingBottom:10}}> The Team</h4>
      <p style={{ textAlign:"center"}}> Trek was created by Molly McHenry, Mithi Jethwa, and Sandra Martinez ❤️</p> <br></br>
      <p style={{paddingBottom:5}}> We are students at Brown Univeristy taking CSCI0320: Introduction to Software Engineering. While finalizing our Thanksgiving and Winter Break travel plans, 
        we were all frustrated that there was no efficient way to share travel plans with friends. It was then that we decided to create 
        Trek as a tool to share travel plans with your network of friends to find travel buddies and see where your friends are going next!
      </p>

      <br></br><br></br>
      {/* citation: https://github.com/NicholasBottone/Wordify/blob/main/styles/globals.css */}
      <h4 style={{color:"navy", fontSize:40,  textAlign:"center", paddingBottom:10, paddingTop:10}}> Privacy Policy</h4>
      <p style={{paddingBottom:5}}> Trek will never sell your personal information to anyone. We only collect basic information about your travel plans, such as your destination, flight/train number, and arrival/departure dates. </p>
      <br></br>
      <p style={{paddingBottom:5}}> For user account registration and authentication, we used Google OAuth2. When you log in with Google, we receive a profile object that contains your email address, name, profile picture, and a numerical identifer. 
        We only retrive this information from Google, we don't modify it. We also do not receive or collect any of your sensative information. For more information regarding how Google handles your information, please visit{" "}
        <a href="https://policies.google.com/privacy">
          Google&apos;s Privacy Policy.
        </a>  </p>
        <br></br>
        <p style={{paddingBottom:80}}> For data storage, we used Firebase. In our database, users are located by a unique identifier and their usernames. Users can, at any point, request to see all the information stored on them and 
        request the deletion of their information by contacting the{" "}
        <a href="mailto: mithi_jethwa@brown.edu,molly_mchenry@brown.edu,sandra_martinez@brown.edu">
           Trek Team.
        </a> 
        
    
        </p>
    </div>
  );
}
 
export default AboutUs;