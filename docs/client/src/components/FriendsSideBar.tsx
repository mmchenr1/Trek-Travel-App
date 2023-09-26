import { userGivenName, userFamilyName } from "./auth/Login";

/**
 * Sidebar with mocked components 
 * @returns static sidebar that would allow the user to add or decline friend requests, as well as 
 * add friends from a recommendation algorithm. This is all static and implementing this would be our
 * next steps
 */
export default function Sidebar(){
    return(
        <aside className="side-bar" aria-label="side bar">
    
                <h1>Friend Requests</h1> 
                
                    <div className="request1" aria-label="request1">
                    krusty_krabs
                        {/* <img className="profile-picture" aria-label="Profile Picture" src="style/user_icon.png"></img> */}
                        <div className="decline-request" aria-label="decline request"> </div>
                        <div className="accept-request" aria-label="accept request"></div>
                    </div>
                    <div className="request2" aria-label="request2">
                    patrick_star
                        {/* <img className="profile-picture" aria-label="Profile Picture" src="style/user_icon.png"></img> */}
                        <div className="decline-request" aria-label="decline request"> </div>
                        <div className="accept-request" aria-label="accept request"></div>
                    </div>
                    <div className="request3" aria-label="request3">
                    mrs_squarepants
                        {/* <img className="profile-picture" aria-label="Profile Picture" src="style/user_icon.png"></img> */}
                        <div className="decline-request" aria-label="decline request"> </div>
                        <div className="accept-request" aria-label="accept request"></div>
                    </div>
                

            <div className="friend-recommendation" aria-label="friend-recommendation">
                <h3>People You May Like!</h3>
                <div className="request1" >
                    sandy_cheeks
                </div>
                <div className="request2">
                    phineas_flynn
                </div>
                <div className="request3">
                    ferb_flynn
                </div>
                <div className="request3">
                    blue_no
                </div>
                <div className="request3">
                    shark_girl
                </div>
                <div className="request3">
                    tom_jerry
                </div>
            </div>
        </aside>
    )
}