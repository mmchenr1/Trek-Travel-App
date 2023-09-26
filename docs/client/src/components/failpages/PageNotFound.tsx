import React from "react";
import { Link } from "react-router-dom"

/**
 * Routes the user to 'PageNotFound' page if they go to url that doesn't have a route
 * @returns 'PageNotFound' page that allows the user to go back to the 'Home' page
 */
 const NotFound = () => {
   return (
     <div className="page-not-found">
       <h2>...Oh Noo!</h2>
       <p>The page can't be followed</p>
       <Link to="/">Back to Homepage</Link>
     </div>
   );
 }

 export default NotFound;