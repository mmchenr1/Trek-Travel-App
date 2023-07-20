import { render, screen } from '@testing-library/react';
import REPL, { TEXT_button_accessible_name, TEXT_input_button_text, TEXT_input_label } from '../../REPL';
import '@testing-library/jest-dom';
import userEvent from '@testing-library/user-event';
import App from '../../App';

// Testing (unit) inspired by ocmcnally
// Aanya Hudda https://github.com/cs0320-f2022/sprint-3-ahudda1-ssompal1/blob/master/src/REPL.test.tsx

//UNIT TESTS

test('renders submit button fields', () => {
  render(<REPL />);    
  const input = screen.getByText((new RegExp(TEXT_input_button_text))); 
  expect(input).toBeInTheDocument()
});

test('renders guess input fields', () => {
    render(<REPL />);    
    const input = screen.getByRole("input", {name: TEXT_button_accessible_name})  
    expect(input).toBeInTheDocument()
});