import React, {JSX} from "react";
import Square from "./Square";
import "../style.css";

interface SegmentProps {
  segmentIndex: number;
  values: string[];
  solvingSequences: string[];
  onSquareChange: (segmentIndex: number, squareIndex: number, value: string) => void;
}

export default function Segment({segmentIndex, values, solvingSequences, onSquareChange = () => {}}: SegmentProps) {
  
  let rows: JSX.Element[] = [];
  
  /*
  values.forEach((value, squareIndex) => {
    rows.push(
      <Square key={squareIndex}
              segmentIndex={segmentIndex}
              squareIndex={squareIndex}
              value={value}
              enabled={value == ''}
              onSquareChange={(segmentIndex, squareIndex, value) => onSquareChange(segmentIndex, squareIndex, value)}
      />
    );
  });
  */
  
  rows = values.map((value, squareIndex) => (
    <Square key={squareIndex}
            segmentIndex={segmentIndex}
            squareIndex={squareIndex}
            value={value}
            // enabled={value == ''}
            enabled={solvingSequences[squareIndex] != '0'}
            onSquareChange={(segmentIndex, squareIndex, value) => onSquareChange(segmentIndex, squareIndex, value)}
    />
  ));
  
  return (
    <div className="baseStyle segment">
      {rows}
    </div>
  );
}