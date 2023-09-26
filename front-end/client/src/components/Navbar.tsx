import Login from './auth/Login';

/**
 * The navigation bar in web application that provides the access to all the pages, provided that the user
 * has logged in
 * @returns the link to the homepage and and calls on the login functionality 
 */
function Navbar() {
  return (
      <nav className="navbar" aria-label="Navigation Bar">
          <Login></Login>        
      </nav>
  );
}
export default Navbar;