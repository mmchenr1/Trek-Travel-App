import React from 'react';
import REPL from './REPL';
// import MapApp from '../mapdemo_src/MapApp'
import './styles/App.css' //css

export default function App() {
  return (
    <div className="App">
      <p className="App-header">
        Welcome to Sprint 3: Terminal!
      </p>

     
      <p className = "App-instructions">
        <p className = "instructions-line-1">
        On this page, you can:
        </p>
            
        <p className = "instructions-line-2">
        1.   Get the content of a CSV file and viewing the scrollable history display by entering <strong>get csv-file-path</strong> with the file path of the CSV file and then clicking the <strong>"Submit"</strong> button
        </p>

        <p className = "instructions-line-3">
        2.   Only after completing step 1, you will be able to get the number of rows and columns in the CSV file by entering <strong>stats</strong> in the command prompt and then clicking the <strong>"Submit"</strong> button
        </p>

        <p className = "instructions-line-4">
        3.   Get the current temperature at coordinates by entering <strong>weather latitude-coordinate longitude-coordinate</strong> with the desired coordinates and then clicking the <strong>"Submit"</strong> button
        </p>
          
      </p>

      <REPL />  
      {/* <MapApp /> */}
    </div>
  );
}