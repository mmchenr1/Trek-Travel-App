import React, { useState, Dispatch, SetStateAction } from 'react';
import {REPLCommandMap, REPLFunction} from './REPLCommands'
import './styles/REPL.css' //import the REPL CSS file
import {REPLMockCommandMap} from './MockTestingCommands'

//aria-label names:
export const TEXT_input_button_text = "Submit"
export const TEXT_input_accessible_name = "Input command"
export const TEXT_button_accessible_name = "Your command goes here"
export const TEXT_command_history = "Command History"
export const TEXT_input_label = "Command: "
export const TEXT_output_label = "Output: "
export const TEXT_invalid_command_message = "Oops! You entered an invalid command!"

//boolean to keep track if a REPLFunction is succesfully used for a command
//CITE: aremels-whavens repo https://github.com/cs0320-f2022/sprint-3-aremels-whavens/blob/master/sprint-3-aremels-whavens/src/CommandFunctions.tsx 
// let succesfulCommandCall: boolean = true;


//global variable added for mock testing:
var mockTest: boolean = false

/**
 * takes the input and runs the appropriate back-end processing on it
 * analogous to pattern function in Vanilla NYT Example
 */
 export async function handleInput(input: string, setOutput: (output: string) => any) {
  console.log("handleInput entered")

  //split input into seperate strings
  var inputs = input.split(' ')
  var command = inputs[0]
  var ariaLabel = "command " + command;
  let response = "";

  let FunctionMap = REPLCommandMap
  //added for mock testing: reroutes REPLCommandMap functions to mocked versions
  if(mockTest == true){
    FunctionMap = REPLMockCommandMap //change to Mocks!
  } 

  //check to see if the command is in the REPLCommandMap
  if (FunctionMap.has(inputs[0])) {
    let commandFunction : REPLFunction | undefined = FunctionMap.get(inputs[0])!
    if (commandFunction != undefined) {
    inputs.shift() //the rest of the args are passed into the function
    setOutput(await commandFunction(inputs))
    // succesfulCommandCall = true
    }
    else {
      response = "error: function is undefined"
    }
  } else {
    setOutput(TEXT_invalid_command_message)
  }

  //CITE: adeendar-ocarson1 for idea to return in this format 
    // https://github.com/cs0320-f2022/sprint-3-adeendar-ocarson1/blob/master/terminal-app/src/Terminal.tsx
  return ({
    input: command,
    output: response,
    ariaLabel: ariaLabel
  })
}

// from vanilla NYT livecode
interface ControlledInputProps {
    value: string, 
    setValue: Dispatch<SetStateAction<string>>,
    ariaLabel: string 
}

// from vanilla NYT livecode
function ControlledInput({value, setValue, ariaLabel}: ControlledInputProps) {
    return (
      <input value={value} 
             onChange={(ev) => setValue(ev.target.value)}
             aria-label={ariaLabel}
             role={"input"}
      ></input>
    );
  }

interface NewInputProps {
    addInput: (input:string) => any;
    setOutput: (output :string) => any;
}

// adapted from vanilla NYT livecode
function NewInput({addInput, setOutput}: NewInputProps) {
    // Remember: let React manage state in your webapp. The current guesses are string fields.
    // You don't always need the <...> annotation, but I like to include it for clarity.
    const [value, setValue] = useState<string>('');  //need to change this to account for value being multiple strings

    return (
      <div className="new-input">
        {/* I opted to use this HTML tag; you don't need to. It structures multiple input fields
            into a single unit, which makes it easier for screenreaders to navigate. */}
        <fieldset>
          <legend> <strong>Enter a command:</strong> </legend>
          <ControlledInput value={value} setValue={setValue} ariaLabel={TEXT_button_accessible_name}/>
        </fieldset>   
        <div>
          <button 
          className="Submit-Button" 
          onClick={async () => {  
            console.log("button clicked!")
            console.log("value: " + value)
            // if(succesfulCommandCall == true)
              //when the button is clicked with nothing entered --> nothing happens
            if(!(value === '')) {
                addInput(value)
                // succesfulCommandCall = false
                await handleInput(value, setOutput) //this line shoudl setOutput (if there's no error)
                // succesfulCommandCall = true
                setValue('')
            } else {
              console.log("empty value entered")
              addInput(value) //added to fix bug of mismatched 
              setOutput(TEXT_invalid_command_message)
              setValue('')
            } 
          } 
        }
        aria-label={TEXT_input_accessible_name}>
          {/* The text displayed on the button */}
          {TEXT_input_button_text}
          </button>
        </div>
      </div>
     );  
  }

// We don't always need an interface for props; without one we need to use this
// syntax, which expects an object with a "guess" field of string[] type.
//   (This is NOT the same as accepting a string[]).
function OldInput( {input, output}: {input: string, output:string}) {
  return (
    <div 
    role = {"input-history-item-"+input+"-"+output} //formatted this way for integration testing
    className={"old-input"}
    aria-label={TEXT_command_history}>
        
      <p>
        <div role={("input-" + input)} aria-label={TEXT_input_label}>
          <strong>{TEXT_input_label}</strong>  {input}
        </div>
        <div role={("output-" + input)} aria-label={TEXT_output_label}>
        <strong>{TEXT_output_label}</strong>  {output}
        </div>
      </p>
    </div>
  );  
}

/**
 * run this function to reroute the REPl to run with the mocked functions rather than the actual ones!
 * for mock testing (Sprint 3 Reflection!!!)
 */
export function setAsMock() {
  mockTest = true //sets as true!
}
/**
 * main method of this file -- takes in inputs and updates the REPL accordingly, keeping a history panel of input-output pairs
 */
export default function REPL() {
  const [inputs, setInputs] = useState<string[]>([]);
  const [outputs, setThisOutput] = useState<string[]>([]);
  return (
    <div className="REPL-Section">   
      <NewInput         
        addInput={(input: string) => {          
          const newInputs = inputs.slice(); 
          newInputs.push(input)
          setInputs(newInputs) }} 
        setOutput={ (output:string) => {
          const newOutputs = outputs.slice()
          newOutputs.push(output)
          setThisOutput(newOutputs)
        }
      }/>
      
      <div className="History-Window">
      {inputs.map( (input, inputNumber) => 
      <OldInput    
          input={input}
          key={inputNumber}
          output={outputs[inputNumber]} //bc stored at the same index (inputs and outputs added at same time so their indexs match)
           />)} 
      </div>
    </div>
  );
}
