import { useState, useEffect} from "react";
import {Link, useHistory } from 'react-router-dom'; //Redirect
import {GoogleLogin, GoogleLogout} from 'react-google-login';
import {gapi} from 'gapi-script';
import {clientID} from './clientID'


/* Variables to access the information of a user from Google Authentication to make ux more personal
    - opted to used variables over the states because useState is async so the user information wasn't
      updating for a given user until they signed out :( */
let userGivenName = ""
export {userGivenName}
 
let userFamilyName = ""
export {userFamilyName}

let userImageUrlConst = ""
export {userImageUrlConst}

let userNameConst = ""
export {userNameConst}

let userEmailConst = ""
export {userEmailConst}


/**
 * Handles the login/logout functionality of the web application
 * Defensive Programming: 
 *  Users can only access all of the navigation bar when they are logged in. This means that the 'Friends',
 * 'Account', 'Create', and 'Posts' pages are only available when a user is signed in. 
 * @returns access to the navigation bar when the user is logged in
 */
export default function Login (){
    const clientId = clientID
    const [profile, setProfile] = useState(null);

      useEffect(() => {
        console.log("useEffect entered!")
          const initClient = () => {
                  gapi.auth2.init({
                  clientId: clientId,
                  scope: ''
              });
          }
          gapi.load('client:auth2', initClient);
      })
    
      let history = useHistory()

      const onSuccess = (res: any) => {
        console.log('success:', res);
        setProfile(res.profileObj)
        history.push('/');

        // setUserID(res.googleId)
        // setUserEmail(res.profileObj.email)
        userGivenName = res.profileObj.givenName
        userFamilyName = res.profileObj.familyName
        userNameConst = res.profileObj.name
        userImageUrlConst = res.profileObj.imageUrl
        userEmailConst = res.profileObj.email

        let urlFirstNameLower = userGivenName.toLowerCase()
        let urlLastNameLower = userFamilyName.toLowerCase()

        //ex: http://localhost:3232/userinitialization?userID=molly_mchenry&&name=Molly
        let apiCall = "http://localhost:3232/userinitialization?userID=" + urlFirstNameLower + "_" + urlLastNameLower + "&&name=" + userGivenName

        console.log(apiCall)
       
        fetch(apiCall)
        .then(response => 
            response.json()
            // console.log(response.json())
            )
        .then(response =>{
            if(response.result === "success"){
                console.log("success passing to backend")
            } else {
                console.log("error loggin in")
            }
        })
      }

      const onFailure = (err: any) => {
          console.log('failed', err);
      }
    
      // https://stackoverflow.com/questions/68111452/google-authentication-with-react-google-login-issue-when-redirecting 
      const logOut = () => {
        setProfile(null);
        history.push('/');
      }
    

      // https://stackoverflow.com/questions/24502898/show-or-hide-element-in-react
      return (
          <div className="navbar-container-bigger" style={profile ? ({position: "fixed", backgroundColor: "rgba(255, 255, 255, 0.59)",
            backdropFilter: "blur(14px)", WebkitBackdropFilter: "blur(14px)",
            border: "1px solid rgba(255, 255, 255, 0.01)", minHeight: "80px"}):({position:"sticky"})}>
             {profile ? (
                    <div className="navbar-container" >
                        <ul >
                            <ol >
                                <div className="links">
                                    <Link to="/settings" aria-label="Settings"> Settings</Link>
                                    <Link to="/account" aria-label="Account"> Account</Link>
                                    <Link to="/posts" aria-label="Posts"> Posts</Link>
                                    <Link to="/create" aria-label="Create">Create </Link>
                                    <Link to="/friends" aria-label="Friends">Friends </Link>
                                    <Link to="/aboutus" aria-label="About Us">About Us </Link>
                                </div>  
                            </ol>
                        </ul>

                        <GoogleLogout 
                            clientId={clientId} 
                            buttonText = "Log Out"  
                            onLogoutSuccess={logOut}
                            className="google-button"
                            aria-label="logout button"
                            style={{position:"absolute", right:0, flex:1}}
                        />
                    </div>
                        
                   ) :(
                    <div className="navbar-container" style={{position: "unset"}}>
                        <GoogleLogin   
                            className="google-signin-button" 
                            aria-label="login button"
                            clientId = {clientId}
                            buttonText = "Continue with Google"
                            onSuccess={onSuccess}
                            onFailure={onFailure}
                            cookiePolicy={'single_host_origin'}
                            isSignedIn={true}
                            style={{paddingRight: 20, width:800, height:400, margin: 40}} 
                        />
                     </div>
                        
                )}
          </div>
          )
}

