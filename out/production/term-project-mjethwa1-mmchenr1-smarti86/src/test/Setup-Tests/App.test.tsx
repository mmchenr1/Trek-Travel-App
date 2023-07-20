import React from 'react';
import { render, screen } from '@testing-library/react';
import App from '../../App';

test('renders learn react link', () => {
  render(<App />);
  const linkElement = screen.getByText("Welcome to Sprint 3: Terminal!");
  expect(linkElement).toBeInTheDocument();
});

