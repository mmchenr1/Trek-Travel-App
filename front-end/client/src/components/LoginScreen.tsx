import React from "react";
import { useState } from "react";
import Login from './auth/Login';

/**
 * The 'Home' page is what is liked to the name of the application, Trek. There is only static text on this page
 * that welcomes the user to Trek! The 'Home' page is also used as the central point through the app that the user
 * is redireted to if they land themselves in a page that doesn't exist
 */
const LoginScreen = () => {
  return (
    <div className="home-header" aria-label="homepage">
      <div className="slogan">
        <h1>Great travel with even
          <br></br>better company </h1>
      </div>
      <div className="login-instructions">
        <text>Login to Trek using any Google Account. If you don't already have a Trek account, don't worry, an account will automatically be created with the Google information used to log in.</text>
      </div>
      {/* insert about us scroll button */}
    </div>
  );
}
 
export default LoginScreen;