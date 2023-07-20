import React from "react";
import { useState } from "react";
import Posts from "./Posts";
import userEmailConst from './auth/Login'
import userImageUrlConst from './auth/Login'
import {userGivenName, userFamilyName, userNameConst} from "./auth/Login"


/*

*/
const Settings = () => {
  return (
    
    <div className="settings" aria-label="settings">
      <div className="settings-title"aria-label="settings">
        <h2 className="page-header">Settings</h2>
     </div>
     <div className="profile-information" color="grey" aria-label="profile information">
        {/* <span className="dot" aria-label="Profile Picture"> </span> */}
        <img className="profile-picture" aria-label="Profile Picture" src="style/user_icon.png"></img>
        <div className="user-info-box">
        <div className="name"> {userGivenName} {userFamilyName}</div>
        <br></br>
        <div className="username">{userGivenName.toLowerCase()}_{userFamilyName.toLowerCase()} </div>
        </div>
      </div>

      <br></br>
      <hr color= "#8035f1" ></hr>
      <br></br>

     {/* <div className="profile-information" aria-label="profile information">
        <span>
         <span className="dot" aria-label="Profile Picture"> </span>
        <h1> username  </h1>
        <text>name of user </text>
        </span>
      </div> */}
      <div className="user-profile">
        {/* <img src={userImageUrlConst} alt="userImage" width="42" height="42"></img> */}
      </div>
      <div className="user-identifiers">
        <section className="user-name"> 
          {/* {userNameConst} */}
        </section>
        <section className="user-email"> 
          {/* {userEmailConst} */}
        </section>
      </div>
      <center className="user-profile-actions">
      
      <button className="change-profile-picture" aria-label="change-profile-picture">
        <strong>change profile picture</strong>
      </button>
      
      <button className="change-username" aria-label="change-username"> 
        <strong>change username</strong>
      </button>

      <button className="change-personal-info" aria-label="change-personal-info"> 
        <strong>change personal info</strong>
      </button>

      <button className="change-password" aria-label="change-password"> 
        <strong>change password</strong>
      </button>

      <button className="delete-account" aria-label="delete-account"> 
        <strong>delete account</strong>
      </button>

      </center>
      
      
    </div>
  );
}
 
export default Settings;