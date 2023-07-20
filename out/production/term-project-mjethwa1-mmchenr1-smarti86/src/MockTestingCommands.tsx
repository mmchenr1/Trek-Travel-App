import { TEXT_invalid_command_message } from './REPL'
import {REPLFunction} from './REPLCommands'

/**
 * for the mock testing portion of the Sprint 3 Review and Reflection:
 *    creates mock REPLFunctions that don't call on an API, and overwrites
 *    the original REPLCommand function when mockTestingCommands is called.
 */

export const TEXT_mock_success_reponse = "successfull mock command call!"
export let REPLMockCommandMap: Map<string, REPLFunction> = new Map<string, REPLFunction>()

/**
 * adds a new REPL function to the REPLMockCommandMap along with the command call necessary to call that function
 * @param command the call to invoke the function
 * @param newFunction the function that implements REPLFunction that should be invoked when the command is entered
 */
export function createMockCommand(command: string, newFunction: REPLFunction) {
    if (!(REPLMockCommandMap.has(command))) {
        REPLMockCommandMap.set(command, newFunction)
    }
}

/**
 * gets csv contents from mock CSV previously fetched
 * directly adapted from code in REPLCommands.tsx
 */
async function mockGetCSV(args: Array<string>): Promise<string> {
    if (args.length !== 1){
        return TEXT_invalid_command_message
    }
    return TEXT_mock_success_reponse
}

    
/**
 * gets csv stats from mock CSV previously fetched
 * directly adapted from statsCSV code in REPLCommands.tsx
 */
async function mockStatsCSV(args: Array<string>): Promise<string> {
    if(args.length !== 0){
        return TEXT_invalid_command_message
    }
    return TEXT_mock_success_reponse
}

/**
 * gets csv stats from mock CSV previously fetched
 * directly adapted from statsCSV code in REPLCommands.tsx
 */
async function mockWeather(args: Array<string>): Promise<string> {
    if(args.length !== 2){
        return TEXT_invalid_command_message
    }
    return TEXT_mock_success_reponse
}


    
const getcsvFunction: REPLFunction = (inputs) => mockGetCSV(inputs)
const statsFunction: REPLFunction = (inputs) => mockStatsCSV(inputs)
const weatherFunction: REPLFunction = (inputs) => mockWeather(inputs)
createMockCommand("get", getcsvFunction)
createMockCommand("stats", statsFunction)
createMockCommand("weather", weatherFunction)
  
  