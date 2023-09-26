import { SetStateAction, useState } from "react"
import { useHistory } from "react-router-dom"
import { userFamilyName, userGivenName } from './auth/Login'
import "react-datepicker/dist/react-datepicker.css";


/// repo referenced to manage post request: https://github.com/paawak/blog/blob/master/code/reactjs/library-ui-secured-with-google/src/Genre.tsx

/**
 * The 'Create' page within the navigation bar where users make their own travel posts  
 * UIUX Component:
 *  - Accessibility: Users are instructed on how to submit post information for each given field
 * Algorithmic Component: 
 *  - Works in conjunction with the backend and Firebase to store the post information of a user that 
 *    will later be displayed in the 'Account' page for the user and the 'Posts' page of their friends
 *  - There are certain required fields that the user has to fill out before the button is enabled for
 *    them to submit the post
 * @returns The 'Account' page in the web application 
 */
const Create = () => {
  const [startDates, setStartDates] = useState('')
  const [travelingTo, setTravelingTo] = useState('')
  const [endDates, setEndDates] = useState('')
  const [travelingFrom, setTravelingFrom] = useState('')
  const [text, setText] = useState('')
  const [modeTravel, setModelTravel] = useState('')

  //PLANE
  const [toFlightTrain, setToFlightTrain] = useState('')
  const [fromFlightTrain, setFromFlightTrain] = useState('')

  //Trains
  const [isPending, setIsPending] = useState(false)
  const history = useHistory()

    const handleSubmit = (e: { preventDefault: () => void; }) => {
      e.preventDefault();

    let urlFirstNameLower = userGivenName.toLowerCase() //ex: sandra
    let urlLastNameLower = userFamilyName.toLowerCase() //ex: martinez

    let urlModeTravel = modeTravel.toUpperCase() //ex: PLANE
    let urlDescription = text.toLowerCase()
    let urlTravelingFrom = travelingFrom.toLowerCase()
    let urlTravelingTo = travelingTo.toLowerCase()
    let urlToFlightTrain = toFlightTrain.toUpperCase() //ex: BA1234 or AMTRACK
    let urlFromFlightTrain = fromFlightTrain.toUpperCase() //ex: BA1234 or AMTRACK

 
      var typeSpecificEnding = "";
      if(modeTravel === "PLANE") {
        typeSpecificEnding = "&&toFlights=" + toFlightTrain + "&&returnFlights=" + fromFlightTrain;
      } else if (modeTravel === "TRAIN") {
        typeSpecificEnding = "&&linesToDest=" + toFlightTrain + "&&linesFromDest=" + fromFlightTrain;
      }

      //change fields with spaces to a form that is passable to the server: I replaced with "<<<") characters (case specific to 3) and then parsed them out in the backend
      const apiCall = "http://localhost:3232/newpost?userID=" +  urlFirstNameLower + "_" + urlLastNameLower + "&&type=" + urlModeTravel + "&&text=" +
      text.replaceAll(" ", "<<<") + "&&fromLocation=" + travelingFrom.replaceAll(" ", "<<<") + "&&toLocation=" + travelingTo.replaceAll(" ", "<<<") + "&&startDate=" + startDates
      + "&&endDate=" + endDates + typeSpecificEnding.replaceAll(" ", "<<<");

      console.log("apiCall: " + apiCall)

      setIsPending(true)

      fetch(apiCall)
        .then(response => response.json())
        .then(json =>{
            if(json.response === "success"){
                console.log("success passing to backend")
                setIsPending(false)
                history.push('/account')
            } else {
                console.log(json.response)
                setIsPending(true)
                history.push('/createfail')
            }
        })
    }

  return (
    <div className="create-post" aria-label="Create" aria-description="Create a post">
      <h2 className="page-header">Create A New Post</h2>
      <div>
      <form onSubmit={handleSubmit}>

        <label>Trip Description:</label>
        <textarea
          // required
          value={text}
          onChange={(e) => setText(e.target.value)}
          maxLength={200}
          className="description-text-box"
          aria-label="Trip Description"
        
        ></textarea>
      <br></br>

        <label>Traveling From:</label>
        <input 
          type="text" 
          // required 
          value={travelingFrom}
          onChange={(e) => setTravelingFrom(e.target.value)}
          className="description-text-box"
          aria-label="Traveling From"
        />

        <br></br>

        <label>Traveling To:</label>
        <input 
          type="text" 
          required 
          value={travelingTo}
          onChange={(e) => setTravelingTo(e.target.value)}
          className="description-text-box"
          aria-label="Traveling To"
        />
  <br></br>
      <label>Start Dates: MM/DD/YYYY</label>
        <input
          type="text" 
          required 
          value={startDates}
          onChange={(e) => setStartDates(e.target.value)}
          className="description-text-box"
          aria-label="Start Date"
        />
  <br></br>
        <label>End Dates: MM/DD/YYYY</label>
        <input 
          type="text" 
          // required 
          value={endDates}
          onChange={(e) => setEndDates(e.target.value)}
          className="description-text-box"
          aria-label="End Date"
        />
        {/* <DatePicker selected={myDate} onChange={(myDate: Date) => setDate(myDate)} /> //TODO */}
  <br></br>
        <label>Mode of Travel:</label>
        <select className="description-text-box" value={modeTravel} onChange={(e=> setModelTravel(e.target.value))} >
          <option value="CAR">Car</option>
          <option value="PLANE">Plane</option>
          <option value="TRAIN">Train</option>
       </select>
       

<br></br>

        <label> Flight Number/Train Line to Destination. Separate inputs with commas. </label>
        <input 
          type="text" 
          // required 
          value={toFlightTrain}
          onChange={(e) => setToFlightTrain(e.target.value)}
          className="description-text-box"
          aria-label="Flight Number or Train Line to Destination"
        />
  <br></br>
        <label> Flight Number/Train Line From Destination. Separate inputs with commas. </label>
        <input 
          type="text" 
          // required 
          value={fromFlightTrain}
          onChange={(e) => setFromFlightTrain(e.target.value)}
          className="description-text-box"
          aria-label="Flight Number or Train Line from Destination"
        />

        {/* <IconButton>
            <Button></Button>
        </IconButton> */}

        {!isPending && <button>Add Post</button>}
        {isPending && <button disabled>Adding Post...</button>}
      </form>
    </div>
    </div>
  );
  
}

 
export default Create;