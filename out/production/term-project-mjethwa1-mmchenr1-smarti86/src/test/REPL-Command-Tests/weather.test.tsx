import React from 'react';
import App from '../../App';
import { render, screen } from '@testing-library/react';
import { weather } from '../../REPLCommands';
import userEvent from '@testing-library/user-event';
import '@testing-library/jest-dom';
import { TEXT_button_accessible_name, TEXT_invalid_command_message } from '../../REPL';

// Testing conceptual, technical, and setup help from karankashyap04

//INTEGRATION TESTS

//Testing that weather works with valid coord
test('weather component rendered after valid weather command (1)', async () => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});
    userEvent.type(inputBox, "weather 39.74 -89.86");
    userEvent.click(submitButton);

    const weatherCommandOutput = await screen.findByText("F");
    expect(weatherCommandOutput).toBeInTheDocument();
})

// Testing that weather works works with valid coord
test('weather component rendered after valid weather command (2)', async () => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});
    userEvent.type(inputBox, "weather 25.7617 -80.1918");
    userEvent.click(submitButton);

    const weatherCommandOutput = await screen.findByText("F");
    expect(weatherCommandOutput).toBeInTheDocument();
})

// Testing that weather works works with invalid coord
test('error component rendered instead of weather after invalid weather command (latitude and longitude not in U.S.) (1)', async () => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});
    userEvent.type(inputBox, "weather 0 0");
    userEvent.click(submitButton);

    const weatherCommandErrorOutput = await screen.findByText("Oops! Those coordinates aren't valid :(");
    expect(weatherCommandErrorOutput).toBeInTheDocument();
})

// Testing that weather works works with invalid coord/input
test('error component rendered instead of weather after invalid weather command (one arg provided)', async () => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});
    userEvent.type(inputBox, "weather 0");
    userEvent.click(submitButton);

    const weatherCommandErrorOutput = await screen.findByText(TEXT_invalid_command_message);
    expect(weatherCommandErrorOutput).toBeInTheDocument();
})

// Testing that weather works works with invalid coord/input
test('error component rendered instead of weather after invalid weather command (too many args provided)', async () => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});
    userEvent.type(inputBox, "weather 0 0 0");
    userEvent.click(submitButton);

    const weatherCommandErrorOutput = await screen.findByText(TEXT_invalid_command_message);
    expect(weatherCommandErrorOutput).toBeInTheDocument();
})

// Testing that weather works works with invalid coord/input
test('error component rendered instead of weather after invalid weather command (coordinates out of potential valid range) (1)', async () => {
    render(<App />)
    const submitButton = screen.getByText("Submit");
    const inputBox = screen.getByRole("input", {name: TEXT_button_accessible_name});
    userEvent.type(inputBox, "weather 10 1020");
    userEvent.click(submitButton);

    const weatherCommandErrorOutput = await screen.findByText("Oops! Those coordinates aren't valid :(");
    expect(weatherCommandErrorOutput).toBeInTheDocument();
})