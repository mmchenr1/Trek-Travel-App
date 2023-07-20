import Navbar from '../components/Navbar';
import Home from '../graveyard/Home';
import Posts from '../components/Posts';
import Account from '../components/Account'
import Create from '../components/Create'
import PostDetails from '../graveyard/PostDetails';
import Friends from '../components/Friends';
import PageNotFound from '../components/failpages/PageNotFound';
import AboutUs from '../components/AboutUs';
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom'
import Settings from '../components/Settings';
import CreateFail from '../components/failpages/CreateFail';
import profile from '../components/auth/Login'


//generic test to see if jest is working
test('is 1 + 1 = 2?', function () {
    expect(1 + 1).toBe(2);
});


/**
 * injection testing of handleClick call when get is entered along with a filepath:
 *  to test logic statements when deciding what to do when
 * the submit button is clicked with inputs in the repl-input box.
 */
 test('test handleClick logic for get', function () {
    var replDescription = document.getElementById("description-text-box");
    var replTravelingFrom = document.getElementById("travelingFrom-text-box")
    var replTravelingTo = document.getElementById("travelingTo-text-box")
    var replStartDates = document.getElementById("startDates-text-box")

    var replEndDates = document.getElementById("endDates-text-box")
    var replModeTravel = document.getElementById("modeTravel-text-box")
    var replToFlightTrains = document.getElementById("toFlightTrains-text-box")
    var replFromFlightTrains = document.getElementById("fromFlightTrains-text-box")
    
     //when "get mockData1" is run, logic goes into get case --> hands off to handleCSVParsing and return the contents of the file
    if (replDescription == null) {
            console.log("Couldn't find repl input");
    }
    else if (!(replDescription instanceof HTMLInputElement)) {
        console.log("Found repl input but it wasn't the right type!");
    }
    else if (replDescription == null) {
        console.log("Couldn't find repl history");
    }
    else {
        replDescription.value = "Winter Break!";
        // expect((main.handleClick())).toBe("");
    }
});
// /**
//  * injection testing of handleClick call when stats is run:
//  * to test logic statements when deciding what to do when
//  * the submit button is clicked with inputs in the repl-input box.
//  */
// test('test handleClick logic for stats call', function () {
//     var replInput = document.getElementById("repl-command-box");
//     var replHistory = document.getElementById("viewableHistoryID");
//     if (replInput == null) {
//         console.log("Couldn't find repl input");
//     }
//     else if (!(replInput instanceof HTMLInputElement)) {
//         console.log("Found repl input but it wasn't the right type!");
//     }
//     else if (replHistory == null) {
//         console.log("Couldn't find repl history");
//     }
//     else {
//         //when first file parsed (only one file in fileHistory), stats command returns the stats for that file
//         replInput.value = "get mockData1";
//         main.handleClick();
//         replInput.value = "stats";
//         expect((main.handleClick())).toBe("Stats: 3 Rows, 1 Columns");
//         //check that when a second file is parsed stats returns for that file and not the previous one
//         replInput.value = "get mockData2";
//         main.handleClick();
//         replInput.value = "stats";
//         expect((main.handleClick())).toBe("Stats: 4 Rows, 1 Columns");
//         //check when inconsistent rows
//         replInput.value = "get mockData3";
//         main.handleClick();
//         replInput.value = "stats";
//         expect((main.handleClick())).toBe("Stats: 3 Rows, 3 Columns");
//         //test that handles empty csv contents correctly
//         replInput.value = "get emptyMock";
//         main.handleClick();
//         replInput.value = "stats";
//         expect((main.handleClick())).toBe("Stats: 0 Rows, 0 Columns");
//     }
// });
// /**
//  * injection testing of handleClick call when stats is run before files parsed
//  */
// test('test stats call edge case', function () {
//     var replInput = document.getElementById("repl-command-box");
//     var replHistory = document.getElementById("viewableHistoryID");
//     if (replInput == null) {
//         console.log("Couldn't find repl input");
//     }
//     else if (!(replInput instanceof HTMLInputElement)) {
//         console.log("Found repl input but it wasn't the right type!");
//     }
//     else if (replHistory == null) {
//         console.log("Couldn't find repl history");
//     }
//     else if (!(replHistory instanceof HTMLParagraphElement)) {
//         console.log("Found repl history but it wasn't the right type!");
//     }
//     else {
//         //when stats is input before any files are parsed (fileHistory is empty)
//         replInput.value = "stats";
//         expect(main.handleClick()).toBe("Parse a file before getting stats.");
//         //if invalid call is sent, then stats called, should still act same as above case
//         replInput.value = "invalid call";
//         main.handleClick();
//         replInput.value = "stats";
//         expect(main.handleClick()).toBe("Parse a file before getting stats.");
//         //make sure stats call is case sensitive
//         replInput.value = "STATS";
//         expect(main.handleClick()).toBe(undefined);
//         //when stats passed in as first argument with other arguments after, should fail
//         //when demoing, only outputs first two arguments in history window
//         replInput.value = "stats get mockData1";
//         expect(main.handleClick()).toBe(undefined);
//         //when stats passed in multiple times, causes handleClick logic to break!
//         //when demoing, won't even output anything in the history window
//         replInput.value = "stats stats stats";
//         expect(main.handleClick()).toBe(undefined);
//     }
// });