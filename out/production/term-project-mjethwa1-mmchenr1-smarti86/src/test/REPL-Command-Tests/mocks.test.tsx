import React from 'react';
import App from '../../App';
import {render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import REPL, { TEXT_button_accessible_name, TEXT_input_accessible_name, TEXT_input_label, TEXT_output_label, TEXT_invalid_command_message } from '../../REPL';
import {TEXT_mock_success_reponse} from "../../MockTestingCommands"
import {REPLCommandMap} from '../../REPLCommands'
import {setAsMock} from '../../REPL'

/**
 * SPRINT 3 REFLECTION: MOCK QUESTION TESTING EXAMPLE
 * These example tests are just (very) brief outlines of how the tests can be written using the mocks added during the review portion of Sprint 3.
 */

test('get mock successful call', async() => {
    render(<App />)
    setAsMock() //load the mock commands
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});

    console.log("REPLCommandMap:  " + REPLCommandMap.entries())

    // make sure that the get call returns the mock command
    userEvent.type(inputBox, "get /Users/poseydurling/Desktop/cs32/sprint-3-mdurling-mmchenr1/src/csv-files/empty.csv");
    userEvent.click(submitButton);
    await screen.findByRole("output", {name: TEXT_output_label})
    expect(TEXT_mock_success_reponse).toBeInTheDocument()
})

// Testing if stats works on an empty csv
test('get mock unsuccesful call', async() => {
    render(<App />)
    setAsMock() //load the mock commands
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});

    // make sure that the get call returns the mock command
    userEvent.type(inputBox, "get");
    userEvent.click(submitButton);
    await screen.findByText(TEXT_output_label); // make sure an element frmo the SECOND CSV is there
    expect(TEXT_invalid_command_message).toBeInTheDocument()

})