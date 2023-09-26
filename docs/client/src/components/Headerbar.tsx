import {Link} from 'react-router-dom'

/**
 * The navigation bar in web application that provdes the access to all the pages, provided that the user
 * has logged in
 * @returns the link to the homepage and and calls on the login functionality 
 */
function Navbar() {
  return (
    <div className= "top-panel" aria-label= "top-panel">
       <div id="appTitle" className="appTitle">
          <Link className="Trek-link" to="/">TREK</Link>
        </div>
    </div>
  );
}
export default Navbar;