import React from 'react';
import App from '../../App';
import { fireEvent, render, screen } from '@testing-library/react';
import { statsCSV } from '../../REPLCommands';
import * as mockCSVs from '../../csv-files/mockCSVs';
import userEvent from '@testing-library/user-event';
import { TEXT_button_accessible_name, TEXT_input_accessible_name, TEXT_input_label, TEXT_output_label } from '../../REPL';

// Testing conceptual, technical, and setup help from karankashyap04

// INTEGRATION TESTS

// Testing if stats works on an empty csv
test('stats after two valid gets returns counts for second CSV', async() => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});

    // second valid get command
    userEvent.type(inputBox, "get /Users/poseydurling/Desktop/cs32/sprint-3-mdurling-mmchenr1/src/csv-files/empty.csv");
    userEvent.click(submitButton);
    await screen.findByText("5.36884"); // make sure an element frmo the SECOND CSV is there

    userEvent.type(inputBox, "stats");
    userEvent.click(submitButton);

    const renderedElement = await screen.findByText("0 Rows, 0 Columns");
    expect(renderedElement).toBeInTheDocument();
})

// Testing if stats works on an empty csv
test('stats after two valid gets returns counts for second CSV', async() => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});

    // second valid get command
    userEvent.type(inputBox, "get /Users/poseydurling/Desktop/cs32/sprint-3-mdurling-mmchenr1/src/csv-files/noRows.csv");
    userEvent.click(submitButton);

    userEvent.type(inputBox, "stats");
    userEvent.click(submitButton);

    const renderedElement = await screen.findByText("0 Rows, 3 Columns");
    expect(renderedElement).toBeInTheDocument();
})

// Testing if stats works on the second csv loaded after getting loading two csvs
test('stats after two valid gets returns counts for second CSV', async() => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});

    userEvent.type(inputBox, "get /Users/poseydurling/Desktop/cs32/sprint-3-mdurling-mmchenr1/src/csv-files/noRows.csv");
    userEvent.click(submitButton);

    // second valid get command
    userEvent.type(inputBox, "get /Users/poseydurling/Desktop/cs32/sprint-3-mdurling-mmchenr1/src/csv-files/simpleStars.csv");
    userEvent.click(submitButton);
    await screen.findByText("5.36884"); // make sure an element frmo the SECOND CSV is there

    userEvent.type(inputBox, "stats");
    userEvent.click(submitButton);

    const renderedElement = await screen.findByText("5 Rows, 5 Columns");
    expect(renderedElement).toBeInTheDocument();
})