import React from "react";
import { useState } from "react";
import Posts from "../components/Posts";

/**
 * The 'Home' page is what is liked to the name of the application, Trek. There is only static text on this page
 * that welcomes the user to Trek! The 'Home' page is also used as the central point through the app that the user
 * is redireted to if they land themselves in a page that doesn't exist
 */
const Home = () => {
  return (
    <div className="home-header" aria-label="homepage">
      <h1>Welcome to Trek!</h1>
      <hr></hr>
    </div>
  );
}
 
export default Home;