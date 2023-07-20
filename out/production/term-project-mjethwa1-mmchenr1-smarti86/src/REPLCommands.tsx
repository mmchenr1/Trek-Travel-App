import {TEXT_invalid_command_message} from './REPL'

/**
 * A command-processor function for our REPL. The function returns a Promise   
 * which resolves to a string, which is the value to print to history when 
 * the command is done executing.
 * 
 * The arguments passed in the input (which need not be named "args") should 
 * *NOT*contain the command-name prefix. 
 */
 export interface REPLFunction {    
    (args: Array<string>): Promise<string>
}

//CITE: myoseph and omcnally https://github.com/cs0320-f2022/sprint-3-myoseph-omcnally/blob/master/frontend/src/replfunction.ts for this idea of creating a map of REPL functions
export let REPLCommandMap: Map<string, REPLFunction> = new Map<string, REPLFunction>()
    

/**
 * adds a new REPL function to the REPLCommandMap along with the command call necessary to call that function
 * @param command the call to invoke the function
 * @param newFunction the function that implements REPLFunction that should be invoked when the command is entered
 */
export function createCommand(command: string, newFunction: REPLFunction) {
    if (!(REPLCommandMap.has(command))) {
        REPLCommandMap.set(command, newFunction)
    }
}

/**
 * gets csv contents from a CSV file in one step --> this is the funciton that will be added to the Map
 */
async function getCSV(args:Array<string>) : Promise<string> {
    //if extraneous arguments
    if (args.length > 1){
        return TEXT_invalid_command_message
    }

    //first load file
    console.log("file load attempt entered")
    return await fetch('http://localhost:2020/loadcsv?filepath=' + args[0])
    // .then(loadReseponse => loadReseponse.json())
    .then(response => {
        let loadResp = response.json()
        return loadResp.then(r => {
            let respType = r.response_type
            if (respType === "success") {
                return fetch('http://localhost:2020/getcsv')
                .then(r => {
                    let getResp = r.json()
                    return getResp.then(data => {
                        let respType = data.response_type
                        let contents = data.contents.toString().replaceAll(",", " ") //formats correctly
                        if (respType === "success") {
                            return contents
                        } else {
                            return respType
                        }
                    })
                })
            } else {
                return "Oops! that filepath is invalid :("
            }
        })        
    })
}

/**
 * gets csv stats from file previously loaded into API
 */
export async function statsCSV(args: Array<string>): Promise<string> {
    if(args.length != 0){
        return TEXT_invalid_command_message
    }
    return await fetch('http://localhost:2020/stats')
        .then(response =>  {
            // return response.json()
            let statsResp = response.json() //TODO FIX THISSSSSS!!!
            return statsResp.then(data => {
                let respType = data.response_type;
                if (respType === "success") {
                    return data.stats.toString().substring(6) //formats correctly
                } else{
                    return "Oops! Get a csv file before calling stats!"
                }
        } )})
}

/**
 * gets current temperature of a location by latitude and longitude coordinates
 */
export async function weather(args: Array<string>): Promise<string> {
    if(args.length != 2){
        return TEXT_invalid_command_message
    }
    return await fetch('http://localhost:1234/weather?latitude=' + args[0] + '&longitude=' + args[1])
    .then(response => {
        let weatherResp = response.json()
        return weatherResp.then(data => {
            let respType = data.response_type
            if (respType === "success") {
                let respData: String = data.responseHandlerOutput
                //problem going into the inner Json!
                //CITE: substring method https://www.tutorialspoint.com/typescript/typescript_string_substring.htm
                let units = respData.substring(8,9)
                let temp = respData.substring(respData.length - 3, respData.length -1)
                return (temp + " " + units)
            } else if (respType = "error_bad_input") {
                return "Oops! Those coordinates aren't valid :("
            } else {
                return "Oops! Error while trying to get weather"
            }
        })
})
}

//add the previosuly created REPLFunctions to the Map of all functions:
const getcsvFunction: REPLFunction = (inputs) => getCSV(inputs)
const statsFunction: REPLFunction = (inputs) => statsCSV(inputs)
const weatherFunction: REPLFunction = (inputs) => weather(inputs)
createCommand("get", getcsvFunction)
createCommand("stats", statsFunction)
createCommand("weather", weatherFunction)
