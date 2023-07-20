import React from 'react';
import { screen, render, within } from '@testing-library/react';
import { statsCSV } from '../../REPLCommands';
import App from '../../App';
import * as mockCSVs from '../../csv-files/mockCSVs';
import userEvent from '@testing-library/user-event';
import REPL, { TEXT_button_accessible_name, TEXT_input_accessible_name } from '../../REPL';

// Testing conceptual, technical, and setup help from karankashyap04

//INTEGRATION TESTS

//ocmcnally https://github.com/cs0320-f2022/sprint-3-myoseph-omcnally/blob/master/frontend/src/Terminal.test.tsx

test ("test single csv", async () => {
    render(<App />); 
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input");
    userEvent.type(inputBox, 'get /Users/poseydurling/Desktop/cs32/sprint-3-mdurling-mmchenr1/src/csv-files/noRows.csv');
    userEvent.click(submitButton);
    const correctResponse = await screen.findByText("[[hi],[hi2],[bye]]");
    expect(correctResponse).toBeInTheDocument();
    userEvent.clear(inputBox)
})

  test('entering valid get command', async () => {
    render(<App />); 
    const command0 = screen.getByRole("input", {name: TEXT_button_accessible_name})
    const submitButton = screen.getByText("Submit");
  
    userEvent.type(command0, 'get /Users/poseydurling/Desktop/cs32/sprint-3-mdurling-mmchenr1/src/csv-files/simpleStars.csv')
    userEvent.click(submitButton)
  
    var output = await screen.findByText("[1,,282.43485,0.00449,5.36884]");
    expect(output).toBeInTheDocument()
    userEvent.clear(command0)
    
  })

// Testing that load throws an error with an invalid file path and the user tries to call stats with no input
test('error component rendered by stats command if after invalid get command', async () => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});

    userEvent.type(inputBox, "stats");
    userEvent.click(submitButton);



    const command = await screen.findByText("Command: stats")
    const renderedElement = await screen.findByText("Oops! Get a csv file before calling stats!");

    console.log("renderelt: " + renderedElement.toString())
    expect(command).toBeInTheDocument();
    expect(renderedElement).toBeInTheDocument();
    userEvent.clear(inputBox)
})

// Testing that load throws an error with an invalid file path
test('Error component rendered if get command with invalid filepath', async () => {
    render(<App />);

    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});

    userEvent.type(inputBox, "get invalid-filepath");
    userEvent.click(submitButton);

    const errorComponent = await screen.findByText("Oops! that filepath is invalid :(");
    expect(errorComponent).toBeInTheDocument();
    userEvent.clear(inputBox)
})
