import React, {JSX} from 'react';
import './App.css';
import Segment from "./components/Segment";
import "./style.css";

const initIndexes: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9];
const initValues: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9];

function App() {
  
  let segments: JSX.Element[] = [];
  let board: JSX.Element[] = [];
  
  initIndexes.forEach((value, segmentIndex) => {
    segments.push(
      <Segment segmentIndex={segmentIndex} values={initValues}/>
    );
  });
  
  board.push(
    <div className="baseStyle board">
      {segments}
    </div>
  )
  
  return (
    <div className="baseStyle board">
      {segments}
    </div>
  );
}

export default App;
